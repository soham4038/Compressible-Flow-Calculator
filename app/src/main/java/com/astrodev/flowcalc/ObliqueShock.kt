package com.astrodev.flowcalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_oblique_shock.*


class ObliqueShock : Fragment() {

    private lateinit var A: TextInputLayout
    private lateinit var INPUT: TextInputLayout
    private lateinit var GAMMA: TextInputLayout
    private lateinit var M1: TextView
    private lateinit var M2: TextView
    private lateinit var M1N: TextView
    private lateinit var M2N: TextView
    private lateinit var P02_P01: TextView
    private lateinit var T2_T1: TextView
    private lateinit var P2_P1: TextView
    private lateinit var RHO2_RHO1: TextView
    private lateinit var calc: Button
    private var input: Double = 0.0
    private var gamma = 1.4
    private var delta = 0.0
    private var m1 = 0.0
    private var beta = 0.0
    private var m1n = 0.0
    private var m2n = 0.0
    private var m2 = 0.0
    private var p2_p1 = 0.0
    private var t2_t1 = 0.0
    private var p02_p01 = 0.0
    private var p2p1 = 0.0
    private var rho2_rho1 = 0.0
    private var a = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_oblique_shock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val osrLayout = view.findViewById(R.id.ObliqueShockLayout) as ConstraintLayout
        INPUT = osrLayout.findViewById(R.id.input)
        GAMMA = osrLayout.findViewById(R.id.gamma)
        A = osrLayout.findViewById(R.id.a)
        M1 = osrLayout.findViewById(R.id.m1)
        M2 = osrLayout.findViewById(R.id.m2)
        M1N = osrLayout.findViewById(R.id.m1n)
        M2N = osrLayout.findViewById(R.id.m2n)
        P02_P01 = osrLayout.findViewById(R.id.p02_p01)
        P2_P1 = osrLayout.findViewById(R.id.p2_p1)
        T2_T1 = osrLayout.findViewById(R.id.t2_t1)
        RHO2_RHO1 = osrLayout.findViewById(R.id.rho2_rho1)


        val spinner: Spinner = osrLayout.findViewById(R.id.selector_osr)
        var selectedOption = 1

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                if (parent != null) {
                    selectedOption = parent.selectedItemPosition
                    A.hint = spinner.selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        calc = osrLayout.findViewById(R.id.calc)
        calc.setOnClickListener {

            calculate(selectedOption)
        }
    }


    fun calculate(i: Int) {
        val obj = Functions()
        if ((GAMMA.editText?.text.toString()).isNotEmpty() && (GAMMA.editText?.text.toString()
                .toDouble()) > 1
        ) {
            GAMMA.isErrorEnabled = false
            gamma = (GAMMA.editText?.text.toString()).toDouble()
        } else {
            GAMMA.isErrorEnabled = true
            GAMMA.error = "Gamma must be greater than 1"
            return
        }
        INPUT.isErrorEnabled = false
        if (INPUT.editText?.text.toString().isNotEmpty()) {
            input = (INPUT.editText?.text.toString()).toDouble()
        } else {
            INPUT.isErrorEnabled = true
            INPUT.error = "Cannot be empty"
            return
        }

        val g = gamma
        A.isErrorEnabled = false
        a = A.editText?.text.toString().toDouble()

        m1 = input
        when (i) {
            0, 1 -> {
                delta = a * 3.14159265359 / 180.0
                if (delta >= 3.14159265359 / 2.0) {
                    A.isErrorEnabled = true
                    A.error = "Turning angle too large"
                    //  alert("Turning angle too large")
                    return
                }
                if (delta <= 0.0) {
                    A.isErrorEnabled = true
                    A.error = "Turning angle must be greater than zero"
                    //   alert("Turning angle must be greater than zero")
                    return
                }
                beta = obj.mdb(g, m1, delta, i)
                if (beta < 0.0) {
                    M2.text = getString(R.string.shockdetached)
                    return
                }
            }
            2 -> {
                beta = a * 3.14159265359 / 180.0
                if (beta >= 3.14159265359 / 2) {
                    A.isErrorEnabled = true
                    A.error = "Wave angle must be less than 90 deg."
                    //  alert("Wave angle must be less than 90 deg.")
                    return
                }
                if (beta - Math.asin(1 / m1) <= 0.0) {
                    A.isErrorEnabled = true
                    A.error = "Wave angle must be greater than Mach angle"
                    // alert("Wave angle must be greater than Mach angle")
                    return
                }
                delta = obj.mbd(g, m1, beta)
            }
            3 -> {
                m1n = a
                if (m1n <= 1.0 || m1n >= m1) {
                    //alert("M1n must be between 1 and M1")
                    return
                }
                beta = Math.asin(m1n / m1)
                delta = obj.mbd(g, m1, beta)
            }
        }
        m1n = m1 * Math.sin(beta)
        waveang.text = (beta * 180 / 3.14159265359).toString()
        turnangle.text = (delta * 180 / 3.14159265359).toString()
        m2n = obj.m2(g, m1n)
        m2 = m2n / Math.sin(beta - delta)
        p2_p1 = 1 + 2 * g / (g + 1) * (m1n * m1n - 1)
        p02_p01 = obj.pp0(g, m1n) / obj.pp0(g, obj.m2(g, m1n)) * p2_p1
        rho2_rho1 = obj.rr0(g, obj.m2(g, m1n)) / obj.rr0(g, m1n) * p02_p01
        t2_t1 = obj.tt0(g, obj.m2(g, m1n)) / obj.tt0(g, m1n)


        M1.text = m1.toString()
        M2.text = m2.toString()
        M1N.text = m1n.toString()
        M2N.text = m2n.toString()
        P2_P1.text = p2_p1.toString()
        P02_P01.text = p02_p01.toString()
        RHO2_RHO1.text = rho2_rho1.toString()
        T2_T1.text = t2_t1.toString()


    }
}



