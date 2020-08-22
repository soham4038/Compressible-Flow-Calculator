package com.astrodev.compressibleflowcalc

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


class FannoFlow : Fragment() {


    private lateinit var INPUT: TextInputLayout
    private lateinit var GAMMA: TextInputLayout
    private lateinit var M: TextView
    private lateinit var PPSF: TextView
    private lateinit var TTSF: TextView
    private lateinit var PTPTSF: TextView
    private lateinit var VVSF: TextView
    private lateinit var fLmaxD: TextView
    private lateinit var SmaxRF: TextView


    private lateinit var calc: Button
    private var input: Double = 0.0
    private var gamma = 1.4
    private var m = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fanno_flow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fannoLayout = view.findViewById(R.id.fannoFlowLayout) as ConstraintLayout
        INPUT = fannoLayout.findViewById(R.id.input)
        GAMMA = fannoLayout.findViewById(R.id.gamma)
        M = fannoLayout.findViewById(R.id.m)
        PPSF = fannoLayout.findViewById(R.id.p0_p0star)
        TTSF = fannoLayout.findViewById(R.id.t_tstar)
        PTPTSF = fannoLayout.findViewById(R.id.p_pstar)
        VVSF = fannoLayout.findViewById(R.id.u_ustar)
        fLmaxD = fannoLayout.findViewById(R.id.fld)
        SmaxRF = fannoLayout.findViewById(R.id.ssr)

        val spinner: Spinner = fannoLayout.findViewById(R.id.selector_fanno)
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

        calc = fannoLayout.findViewById(R.id.calc)
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

        var x: Double
        val g = gamma
        m = input
        var y: Double
        when (i) {
            1 -> {
                if (input >= 1.2 || input <= 0.0) {
                    INPUT.isErrorEnabled = true
                    INPUT.error = ("T/T* must be between 0 and 1.2")
                    return
                }
                INPUT.isErrorEnabled = false
                m = Math.sqrt(-input * (g - 1.0) * (2.0 * input - g - 1.0)) / (input * g - input)
            }

            2 -> m = Math.sqrt(
                -input * (g - 1.0) * (input - Math.sqrt(
                    Math.pow(input, 2.0) + Math.pow(
                        g,
                        2.0
                    ) - 1.0
                ))
            ) / (input * g - input)

            3 -> {
                if (input <= 1.0) {
                    INPUT.isErrorEnabled = true
                    INPUT.error = ("Pt/Pt* must be greater than 1")
                    return
                }
                INPUT.isErrorEnabled = false
                var fdiff = 5.0
                var xlo = 0.00000001
                var xhi = 1.0

                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = (1.0 / x) * Math.pow(
                        (1.0 + ((g - 1.0) / 2) * Math.pow(
                            x,
                            2.0
                        )) / ((g + 1.0) / 2.0), ((g + 1.0) / (2.0 * (g - 1.0)))
                    )
                    if (y > input) {
                        xlo = x
                    } else {
                        xhi = x
                    }
                    fdiff = Math.abs(input - y)

                    m = x
                }
            }

            4 -> {

                if (input <= 1.0) {
                    INPUT.isErrorEnabled = true
                    INPUT.error = ("Pt/Pt* must be greater than 1")
                    return
                }
                var fdiff = 5.0
                var xlo = 1.0
                var xhi = 100.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = (1.0 / x) * Math.pow(
                        (1.0 + ((g - 1) / 2) * Math.pow(
                            x,
                            2.0
                        )) / ((g + 1) / 2), ((g + 1) / (2 * (g - 1)))
                    )
                    if (y > input) {
                        xhi = x
                    } else {
                        xlo = x
                    }
                    fdiff = Math.abs(input - y)
                    m = x
                }
            }
            5 -> {
                if (input < 0.0 || input >= 2.45) {
                    INPUT.isErrorEnabled = true
                    INPUT.error = ("V/V* must be between 0 and 2.45")
                    return
                }
                INPUT.isErrorEnabled = false
                m = 2 * input / Math.sqrt(
                    2 * g + 2 - 2.0 * Math.pow(
                        input,
                        2.0
                    ) * g + 2.0 * Math.pow(input, 2.0)
                )
            }

            6 -> {
                if (input < 0.0) {
                    INPUT.error = ("4fLmax/D must be positive")
                    INPUT.isErrorEnabled = true
                    return
                }
                INPUT.isErrorEnabled = false
                var fdiff = 5.0
                var xlo = 0.00000001
                var xhi = 1.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = ((g + 1) / (2 * g)) * Math.log(
                        ((g + 1) / 2.0) * Math.pow(
                            x,
                            2.0
                        ) / (1 + ((g - 1.0) / 2.0) * Math.pow(x, 2.0))
                    ) + (1.0 / g) * (1.0 / Math.pow(x, 2.0) - 1.0)
                    if (y > input) {
                        xlo = x
                    } else {
                        xhi = x
                    }
                    fdiff = Math.abs(input - y)

                    m = x
                }
            }

            7 -> {
                if (input < 0.0 || input > 0.82154) {
                    INPUT.error = ("4fLmax/D must be between 0 and 0.82154")
                    INPUT.isErrorEnabled = true
                    return
                }
                INPUT.isErrorEnabled = false
                var fdiff = 50.0
                var xlo = 1.0
                var xhi = 100.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = ((g + 1) / (2 * g)) * Math.log(
                        ((g + 1) / 2) * Math.pow(
                            x,
                            2.0
                        ) / (1 + ((g - 1) / 2) * Math.pow(x, 2.0))
                    ) + (1 / g) * (1 / Math.pow(
                        x,
                        2.0
                    ) - 1)
                    if (y > input) {
                        xhi = x
                    } else {
                        xlo = x
                    }
                    fdiff = Math.abs(input - y)

                    m = x
                }
            }
            8 -> {
                if (input < 0.0) {
                    INPUT.error = ("Smax/R must be positive")
                    INPUT.isErrorEnabled = true
                    return
                }
                INPUT.isErrorEnabled = false
                var fdiff = 5.0
                var xlo = 0.00000001
                var xhi = 1.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = Math.log(
                        (1 / x) * Math.pow(
                            (1 + ((g - 1) / 2.0) * Math.pow(
                                x,
                                2.0
                            )) / (1 + ((g - 1) / 2.0)), (g + 1) / (2.0 * (g - 1.0))
                        )
                    )
                    if (y > input) {
                        xlo = x
                    } else {
                        xhi = x
                    }
                    fdiff = Math.abs(input - y)

                    m = x
                }
            }
            9 -> {
                if (input < 0.0) {
                    INPUT.error = ("Smax/R must be positive")
                    INPUT.isErrorEnabled = true
                    return
                }
                INPUT.isErrorEnabled = false
                var fdiff = 5.0
                var xlo = 1.0
                var xhi = 100.0

                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = Math.log(
                        (1 / x) * Math.pow(
                            (1 + ((g - 1) / 2.0) * Math.pow(
                                x,
                                2.0
                            )) / (1 + ((g - 1) / 2.0)), (g + 1) / (2 * (g - 1.0))
                        )
                    )
                    if (y > input) {
                        xhi = x
                    } else {
                        xlo = x
                    }
                    fdiff = Math.abs(input - y)

                    m = x
                }
            }
            0 -> {
                if (input <= 0.0) {
                    INPUT.error = ("M must be greater than 0")
                    INPUT.isErrorEnabled = true
                    return
                }
                INPUT.isErrorEnabled = false
                m = input


            }

        }
        M.text = m.toString()
        TTSF.text = obj.TTSF(g, m).toString()
        PPSF.text = obj.PPSF(g, m).toString()
        PTPTSF.text = obj.PTPTSF(g, m).toString()
        VVSF.text = obj.VVSF(g, m).toString()
        fLmaxD.text = obj.fLmaxD(g, m).toString()
        SmaxRF.text = obj.SmaxRF(g, m).toString()

    }
}


