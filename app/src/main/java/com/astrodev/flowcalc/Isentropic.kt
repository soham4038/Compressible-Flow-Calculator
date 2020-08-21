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
import java.lang.Math.asin


class Isentropic : Fragment() {


    private lateinit var layout: ConstraintLayout


    private var mach = 0.0
    private var gamma = 1.4
    private var p_p0 = 0.0
    private var t_t0 = 0.0
    private var pstar = 0.0
    private var tstar = 0.0
    private var astar = 0.0
    private var pmangle = 0.0
    private var machangle = 0.0
    private var rhostar = 0.0
    private var rho_rho0 = 0.0

    private lateinit var calc: Button
    private lateinit var INPUT: TextInputLayout
    private lateinit var GAMMA: TextInputLayout
    private lateinit var MACH: TextView
    private lateinit var P_P0: TextView
    private lateinit var T_T0: TextView
    private lateinit var PSTAR: TextView
    private lateinit var TSTAR: TextView
    private lateinit var ASTAR: TextView
    private lateinit var PMANGLE: TextView
    private lateinit var MANGLE: TextView
    private lateinit var RHOSTAR: TextView
    private lateinit var RHO_RHO0: TextView

    private var input: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_isentropic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layout = view.findViewById(R.id.isentropicLayout) as ConstraintLayout
        MACH = layout.findViewById(R.id.mach)
        GAMMA = layout.findViewById(R.id.gamma)
        P_P0 = layout.findViewById(R.id.p_p0)
        T_T0 = layout.findViewById(R.id.t_t0)
        PSTAR = layout.findViewById(R.id.pstar)
        TSTAR = layout.findViewById(R.id.tstar)
        ASTAR = layout.findViewById(R.id.astar)
        PMANGLE = layout.findViewById(R.id.pmangle)
        MANGLE = layout.findViewById(R.id.mangle)
        RHOSTAR = layout.findViewById(R.id.rhostar)
        RHO_RHO0 = layout.findViewById(R.id.rho_rho0)
        INPUT = layout.findViewById(R.id.input)
        val spinner: Spinner = layout.findViewById(R.id.selector)

        var selectedOption = 0

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                if (parent != null) {
                    selectedOption = parent.selectedItemPosition
                    INPUT.hint = spinner.selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        calc = layout.findViewById(R.id.calc)
        calc.setOnClickListener {
            calculate(selectedOption)
        }
    }

    private fun calculate(selectedOption: Int) {

        val obj = Functions()

        if ((GAMMA.editText?.text.toString()).isNotEmpty() && (GAMMA.editText?.text.toString()
                .toDouble()) > 1.0
        ) {
            GAMMA.isErrorEnabled = false
            gamma = (GAMMA.editText?.text.toString()).toDouble()
        } else {
            GAMMA.isErrorEnabled = true
            GAMMA.error = "Gamma must be greater than 1"
            return
        }
        if ((INPUT.editText?.text.toString()
                .isNotEmpty() && (INPUT.editText?.text.toString()).toDouble() > 0)
        ) {
            input = (INPUT.editText?.text.toString()).toDouble()

            when (selectedOption) {
                0 -> mach = input

                1 -> {
                    if (input > 0 && input < 1) {
                        INPUT.isErrorEnabled = false
                        p_p0 = input
                        mach = Math.sqrt(
                            2 * ((1 / Math.pow(
                                input,
                                (gamma - 1) / gamma
                            )) - 1) / (gamma - 1)
                        )
                    } else {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " P/P0 must be between 0 and 1"
                        return
                    }
                }

                2 -> {

                    if (input > 0 && input < 1) {
                        INPUT.isErrorEnabled = false
                        t_t0 = input
                        mach = Math.sqrt(2 * ((1 / input) - 1) / (gamma - 1))
                    } else {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " T/T0 must be between 0 and 1"
                        return
                    }
                }


                3 -> {

                    if (input > 0 && input < 1) {
                        INPUT.isErrorEnabled = false
                        rho_rho0 = input
                        mach = Math.sqrt(2 * ((1 / Math.pow(input, (gamma - 1))) - 1) / (gamma - 1))
                    } else {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " rho/rho0 must be between 0 and 1"
                        return
                    }
                }
                4 -> {
                    if (input > 0 && input < 90) {
                        machangle = input
                        mach = 1 / Math.sin(input * 3.14159265359 / 180)
                        INPUT.isErrorEnabled = false
                    } else {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " Must be between 0 and 90"
                        return
                    }
                }
                5 -> {
                    INPUT.isErrorEnabled = false
                    val numax = (Math.sqrt((gamma + 1) / (gamma - 1)) - 1) * 90
                    if (input <= 0 && input >= numax) {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " Must be between 0 and $numax"
                        return
                    }
                    pmangle = input
                    var mnew = 2.0
                    mach = 0.0
                    while (Math.abs(mnew - mach) > 0.00001) {
                        mach = mnew
                        val fm = (obj.nu(gamma, mach) - input) * 3.14159265359 / 180
                        val fdm =
                            Math.sqrt(mach * mach - 1) / (1 + 0.5 * (gamma - 1) * mach * mach) / mach
                        mnew = mach - fm / fdm
                    }
                }
                6 -> {
                    if (input > 1) {
                        INPUT.isErrorEnabled = false
                        astar = input
                        var mnew = 2.0
                        mach = 0.0
                        while (Math.abs(mnew - mach) > 0.000001) {
                            mach = mnew
                            val phi = obj.aas(gamma, mach)
                            val s = (3 - gamma) / (1 + gamma)
                            mnew = mach - (phi - input) / (Math.pow(phi * mach, s) - phi / mach)
                        }
                    } else {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " Must be greater than 1"
                        return
                    }
                }
                7 -> {
                    if (input > 1) {
                        INPUT.isErrorEnabled = false
                        astar = input
                        var mnew = 0.00001
                        mach = 0.0
                        while (Math.abs(mnew - mach) > 0.000001) {
                            mach = mnew
                            val phi = obj.aas(gamma, mach)
                            val s = (3 - gamma) / (1 + gamma)
                            mnew = mach - (phi - input) / (Math.pow(phi * mach, s) - phi / mach)
                        }
                    } else {
                        INPUT.isErrorEnabled = true
                        INPUT.error = " Must be greater than 1"
                        return
                    }
                }
            }
        }

        machangle = asin(1.0 / mach) * 180 / 3.14159265359
        pmangle = obj.nu(gamma, mach)
        t_t0 = obj.tt0(gamma, mach)
        p_p0 = obj.pp0(gamma, mach)
        rho_rho0 = obj.rr0(gamma, mach)
        tstar = obj.tts(gamma, mach)
        pstar = obj.pps(gamma, mach)
        rhostar = obj.rrs(gamma, mach)
        astar = obj.aas(gamma, mach)

        MACH.text = mach.toString()
        T_T0.text = t_t0.toString()
        TSTAR.text = tstar.toString()
        P_P0.text = p_p0.toString()
        PSTAR.text = pstar.toString()
        RHO_RHO0.text = rho_rho0.toString()
        RHOSTAR.text = rhostar.toString()
        ASTAR.text = astar.toString()
        MANGLE.text = machangle.toString()
        PMANGLE.text = pmangle.toString()

    }

}