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

class NormalShock : Fragment() {

    private var gamma = 1.4
    private var m2 = 0.0
    private var p2_p1 = 0.0
    private var t2_t1 = 0.0
    private var p02_p01 = 0.0
    private var p1_p02 = 0.0
    private var rho2_rho1 = 0.0
    private lateinit var calc: Button
    private lateinit var M1: TextView
    private lateinit var M2: TextView
    private lateinit var P02_P01: TextView
    private lateinit var T2_T1: TextView
    private lateinit var P2_P1: TextView
    private lateinit var P1_P02: TextView
    private lateinit var INPUT: TextInputLayout
    private lateinit var GAMMA: TextInputLayout
    private lateinit var RHO2_RHO1: TextView
    private var input: Double = 0.0
    private var m1 = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_normal_shock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nsrLayout = view.findViewById(R.id.NormalShockLayout) as ConstraintLayout
        INPUT = nsrLayout.findViewById(R.id.input)
        GAMMA = nsrLayout.findViewById(R.id.gamma)
        M1 = nsrLayout.findViewById(R.id.m1)
        M2 = nsrLayout.findViewById(R.id.m2)
        P02_P01 = nsrLayout.findViewById(R.id.p02_p01)
        P1_P02 = nsrLayout.findViewById(R.id.p1_p02)
        P2_P1 = nsrLayout.findViewById(R.id.p2_p1)
        T2_T1 = nsrLayout.findViewById(R.id.t2_t1)
        RHO2_RHO1 = nsrLayout.findViewById(R.id.rho2_rho1)

        val spinner: Spinner = nsrLayout.findViewById(R.id.selector_nsr)
        var selectedOption = 1

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                if (parent != null) {
                    selectedOption = parent.selectedItemPosition
                    INPUT.hint = spinner.selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        calc = nsrLayout.findViewById(R.id.calc)
        calc.setOnClickListener {

            calculate(selectedOption)
        }
    }

    private fun calculate(i: Int) {
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

        when (i) {

            1 -> {
                if (input >= 1.0 || input <= Math.sqrt((g - 1) / 2 / g)) {
                    INPUT.isErrorEnabled = true
                    val e = Math.sqrt((g - 1) / 2 / g)
                    INPUT.error = " M2 must be between $e and 1"
                    return
                } else {
                    INPUT.isErrorEnabled = false
                    m1 = obj.m2(gamma, input)
                }
            }

            2 -> {
                if (input > 1) {
                    m1 = Math.sqrt((input - 1.0) * (gamma + 1.0) / 2.0 / gamma + 1)
                    INPUT.isErrorEnabled = false
                } else {
                    INPUT.isErrorEnabled = true
                    return
                }
            }

            3 -> {
                m1 = Math.sqrt(2.0 * input / (gamma + 1 - input * (gamma - 1.0)))
            }

            4 -> {
                val aa = 2.0 * gamma * (gamma - 1.0)
                val bb =
                    4.0 * gamma - (gamma - 1.0) * (gamma - 1.0) - input * (gamma + 1.0) * (gamma + 1.0)
                val cc = -2.0 * (gamma - 1.0)
                m1 = Math.sqrt((-bb + Math.sqrt(bb * bb - 4 * aa * cc)) / 2 / aa)
            }
            5 -> {
                var mnew = 2.0
                m1 = 0.0
                while (Math.abs(mnew - m1) > 0.00001) {
                    m1 = mnew
                    val al = (gamma + 1) * m1 * m1 / ((gamma - 1) * m1 * m1 + 2)
                    val be = (gamma + 1.0) / (2.0 * gamma * m1 * m1 - (gamma - 1))
                    val daldm1 = (2 / m1 - 2 * m1 * (gamma - 1) / ((gamma - 1) * m1 * m1 + 2)) * al
                    val dbedm1 = -4.0 * gamma * m1 * be / (2 * gamma * m1 * m1 - (gamma - 1))
                    val fm =
                        Math.pow(al, gamma / (gamma - 1)) * Math.pow(be, 1 / (gamma - 1)) - input
                    val fdm =
                        gamma / (gamma - 1) * Math.pow(al, 1 / (gamma - 1)) * daldm1 * Math.pow(
                            be,
                            1 / (gamma - 1)
                        ) + Math.pow(al, gamma / (gamma - 1)) / (gamma - 1) * Math.pow(
                            be,
                            (2 - gamma) / (gamma - 1)
                        ) * dbedm1
                    mnew = m1 - fm / fdm
                }
            }

            6 -> {
                val vmax = Math.pow((g + 1) / 2, -g / (g - 1.0))
                if (input >= vmax || input <= 0.0) {
                    return
                }
                var mnew = 2.0
                m1 = 0.0
                while (Math.abs(mnew - m1) > 0.00001) {
                    m1 = mnew
                    val al = (g + 1) * m1 * m1 / 2
                    val be = (g + 1) / (2 * g * m1 * m1 - (g - 1))
                    val daldm1 = m1 * (g + 1)
                    val dbedm1 = -4 * g * m1 * be / (2 * g * m1 * m1 - (g - 1))
                    val fm = Math.pow(al, g / (g - 1)) * Math.pow(be, 1 / (g - 1)) - 1 / input
                    val fdm = g / (g - 1) * Math.pow(al, 1 / (g - 1)) * daldm1 * Math.pow(
                        be,
                        1 / (g - 1)
                    ) + Math.pow(al, g / (g - 1)) / (g - 1) * Math.pow(
                        be,
                        (2 - g) / (g - 1)
                    ) * dbedm1
                    mnew = m1 - fm / fdm
                }
            }
            0 -> {
                m1 = input
            }
        }

        m2 = obj.m2(g, m1)
        p2_p1 = 1.0 + 2.0 * g / (g + 1.0) * (m1 * m1 - 1.0)
        p02_p01 = obj.pp0(g, m1) / obj.pp0(g, obj.m2(g, m1)) * p2_p1
        rho2_rho1 = obj.rr0(g, obj.m2(g, m1)) / obj.rr0(g, m1) * p02_p01
        t2_t1 = obj.tt0(g, obj.m2(g, m1)) / obj.tt0(g, m1)
        p1_p02 = obj.pp0(g, m1) / p02_p01

        M1.text = m1.toString()
        M2.text = m2.toString()
        P2_P1.text = p2_p1.toString()
        P02_P01.text = p02_p01.toString()
        P1_P02.text = p1_p02.toString()
        RHO2_RHO1.text = rho2_rho1.toString()
        T2_T1.text = t2_t1.toString()

    }

}