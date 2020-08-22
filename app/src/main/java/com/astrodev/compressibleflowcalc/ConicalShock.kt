package com.astrodev.compressibleflowcalc

import android.os.Bundle
import android.text.Html
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


class ConicalShock : Fragment() {

    private lateinit var A: TextInputLayout
    private lateinit var INPUT: TextInputLayout
    private lateinit var GAMMA: TextInputLayout
    private lateinit var MC: TextView
    private lateinit var C_ANG: TextView
    private lateinit var W_ANG: TextView
    private lateinit var S_TURNANG: TextView
    private lateinit var P02_P01: TextView
    private lateinit var P2_P1: TextView
    private lateinit var P0C_P1: TextView
    private lateinit var PC_P1: TextView
    private lateinit var T2_T1: TextView
    private lateinit var TC_T1: TextView
    private lateinit var RHO2_RHO1: TextView
    private lateinit var RHOC_RHO1: TextView


    private lateinit var calc: Button
    private var input: Double = 0.0
    private var gamma = 1.4
    private var delta = 0.0
    private var m1 = 0.0
    private var beta = 0.0
    private var sigma = 0.0
    private var m1n = 0.0
    private var m2n = 0.0
    private var pc_p1 = 0.0
    private var p2_p1 = 0.0
    private var t2_t1 = 0.0
    private var p02_p01 = 0.0
    private var rho2_rho1 = 0.0
    private var rhoc_rho1 = 0.0
    private var a = 0.0
    private var mc = 0.0
    private var p0c_p01 = 0.0
    private var tc_t1 = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conical_shock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val csrLayout = view.findViewById(R.id.ConicalShockLayout) as ConstraintLayout
        INPUT = csrLayout.findViewById(R.id.input)
        GAMMA = csrLayout.findViewById(R.id.gamma)
        A = csrLayout.findViewById(R.id.a)
        MC = csrLayout.findViewById(R.id.mc)
        C_ANG = csrLayout.findViewById(R.id.c_ang)
        W_ANG = csrLayout.findViewById(R.id.w_ang)
        S_TURNANG = csrLayout.findViewById(R.id.s_turnangle)
        P02_P01 = csrLayout.findViewById(R.id.p02_p01)
        P2_P1 = csrLayout.findViewById(R.id.p2_p1)
        T2_T1 = csrLayout.findViewById(R.id.t2_t1)
        RHO2_RHO1 = csrLayout.findViewById(R.id.rho2_rho1)
        TC_T1 = csrLayout.findViewById(R.id.tc_t1)
        PC_P1 = csrLayout.findViewById(R.id.pc_p1)
        P0C_P1 = csrLayout.findViewById(R.id.p0c_p01)
        RHOC_RHO1 = csrLayout.findViewById(R.id.rhoc_rho1)


        val mctext: TextView = csrLayout.findViewById(R.id.mctext)
        mctext.text = Html.fromHtml(getString(R.string.mc))

        val pcp1text: TextView = csrLayout.findViewById(R.id.pctext)
        pcp1text.text = Html.fromHtml(getString(R.string.pcp1))


        val p0ctext: TextView = csrLayout.findViewById(R.id.p0ctext)
        p0ctext.text = Html.fromHtml(getString(R.string.p_u2080c_p_u2080_u2081))

        val rhoctext: TextView = csrLayout.findViewById(R.id.rhoctext)
        rhoctext.text = Html.fromHtml(getString(R.string.rhoc))

        val tctext = csrLayout.findViewById<TextView>(R.id.tctext)
        tctext.text = Html.fromHtml(getString(R.string.tc_t_u2081))

        val spinner: Spinner = csrLayout.findViewById(R.id.selector_csr)
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

        calc = csrLayout.findViewById(R.id.calc)
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
        if (A.editText?.text.toString().isNotEmpty()) {
            a = (A.editText?.text.toString()).toDouble()
        } else {
            A.isErrorEnabled = true
            A.error = "Cannot be empty"
            return
        }

        val pi = 3.14159265359
        m1 = input
        when (i) {
            0 -> {
                sigma = a * pi / 180.0
                if (sigma > pi / 2) {
                    A.error = "Cone angle must be less than 90 degrees."
                    return
                }
                if (sigma < 0) {
                    A.error = "Cone angle must be greater than 0 degrees."
                    return
                }

                // Guess a wave angle, use mach angle since (mach angle)<(wave angle)<(90deg)
                var theta0 = Math.asin(1.0 / m1) + .0001
                var sigma0 = obj.mbs(g, m1, theta0)[0]

                // Increase wave angle guess by h and calculate cone angle
                // until calculated cone angle matches input cone angle
                val h = 0.0005
                while (sigma0 < sigma) {

                    theta0 += h
                    sigma0 = obj.mbs(g, m1, theta0)[0]

                    // Check for detachment
                    if (sigma0 < 0.0) {
                        MC.text = "Shock Detached"
                        return
                    }
                }

                beta = theta0
                delta = obj.mbd(g, m1, beta)
                mc = obj.mbs(g, m1, beta)[1]
            }
            1 -> {
                beta = a * pi / 180.0
                if (beta > pi / 2) {
                    A.error = "Wave angle must be less than 90 degrees."
                    return
                }

                if (beta > pi / 2.0) {
                    A.error = ("Wave angle must be less than 90 deg.")
                    return
                }
                if (beta - Math.asin(1.0 / m1) <= 0.0) {
                    A.error = ("Wave angle must be greater than Mach angle")
                    return
                }

                delta = obj.mbd(g, m1, beta)
                sigma = obj.mbs(g, m1, beta)[0]
                mc = obj.mbs(g, m1, beta)[1]

            }

            2 -> {
                mc = a
                if (mc < 0.0) {
                    A.error = ("Surface Mach number must be positive")
                    return
                }
                if (mc > m1) {
                    A.error = ("Mc must be less than M1")
                    return
                }

                // Guess a wave angle, use Mach angle since (Mach angle)<(wave angle)<(90deg)
                var theta0 = Math.asin(1.0 / m1) + .0001
                var mc0 = obj.mbs(g, m1, theta0)[1]
                var sigma0 = obj.mbs(g, m1, theta0)[0]

                // Increase wave angle guess by h and calculate mc
                // until calculated mc matches input mc
                val h = 0.0005
                while (mc0 > mc) {
                    val sigmaold = sigma0
                    theta0 = theta0 + h
                    mc0 = obj.mbs(g, m1, theta0)[1]
                    sigma0 = obj.mbs(g, m1, theta0)[0]

                    // Check for shock detachment
                    if (sigma0 < sigmaold || mc0 < 0) {
                        MC.text = "Shock Detached"
                        return
                    }
                }

                beta = theta0
                delta = obj.mbd(g, m1, beta)
                sigma = obj.mbs(g, m1, beta)[0]
                mc = obj.mbs(g, m1, beta)[1]

            }
        }
        m1n = m1 * Math.sin(beta)
        m2n = obj.m2(g, m1n)
        val m22 = m2n / Math.sin(beta - delta)

        // Calculate p, rho, T ratios just after shock
        p2_p1 = 1 + 2 * g / (g + 1) * (m1n * m1n - 1)
        p02_p01 = obj.pp0(g, m1n) / obj.pp0(g, m2n) * p2_p1
        rho2_rho1 = obj.rr0(g, m2n) / obj.rr0(g, m1n) * p02_p01
        t2_t1 = obj.tt0(g, m2n) / obj.tt0(g, m1n)
        // Calculate p, rho, T ratios at cone surface
        pc_p1 = obj.pp0(g, mc) / obj.pp0(g, m22) * p2_p1
        p0c_p01 = p02_p01
        rhoc_rho1 = obj.rr0(g, mc) / obj.rr0(g, m22) * rho2_rho1
        tc_t1 = obj.tt0(g, mc) / obj.tt0(g, m22) * t2_t1

        MC.text = mc.toString()
        C_ANG.text = (sigma * 180.0 / pi).toString()
        W_ANG.text = (beta * 180.0 / pi).toString()
        S_TURNANG.text = (delta * 180.0 / pi).toString()
        P2_P1.text = p2_p1.toString()
        P02_P01.text = p02_p01.toString()
        RHO2_RHO1.text = rho2_rho1.toString()
        T2_T1.text = t2_t1.toString()

        PC_P1.text = pc_p1.toString()
        P0C_P1.text = p0c_p01.toString()
        RHOC_RHO1.text = rhoc_rho1.toString()
        TC_T1.text = tc_t1.toString()
    }
}