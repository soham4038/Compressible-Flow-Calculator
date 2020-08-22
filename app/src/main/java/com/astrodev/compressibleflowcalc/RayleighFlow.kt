package com.astrodev.compressibleflowcalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_rayleigh_flow.*


class RayleighFlow : Fragment() {

    private lateinit var INPUT: TextInputLayout
    private lateinit var GAMMA: TextInputLayout
    private lateinit var M: TextView
    private lateinit var PPSR: TextView
    private lateinit var TTSR: TextView
    private lateinit var PTPTSR: TextView
    private lateinit var VVSR: TextView
    private lateinit var TtTtSR: TextView
    private lateinit var SmaxRR: TextView


    private lateinit var calc: Button
    private var input: Double = 0.0
    private var gamma = 1.4
    private var m = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rayleigh_flow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        INPUT = RayleighFlowLayout.findViewById(R.id.input)
        GAMMA = RayleighFlowLayout.findViewById(R.id.gamma)
        M = RayleighFlowLayout.findViewById(R.id.m)
        PPSR = RayleighFlowLayout.findViewById(R.id.p0_p0star)
        TTSR = RayleighFlowLayout.findViewById(R.id.t_tstar)
        PTPTSR = RayleighFlowLayout.findViewById(R.id.p_pstar)
        VVSR = RayleighFlowLayout.findViewById(R.id.u_ustar)
        SmaxRR = RayleighFlowLayout.findViewById(R.id.ssr)
        TtTtSR = RayleighFlowLayout.findViewById(R.id.t0_t0star)
        val spinner: Spinner = RayleighFlowLayout.findViewById(R.id.selector_rayleigh)
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

        calc = RayleighFlowLayout.findViewById(R.id.calc)
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
        val v = input
        val g = gamma
        var x: Double
        var y: Double
        INPUT.isErrorEnabled = false
        when (i) {


            1 -> {
                if (input >= 1.0 || input <= 0.0) {

                    INPUT.error = ("To/To* must be between 0 and 1")

                    return
                }
                m = Math.sqrt(
                    -(v * Math.pow(g, 2.0) + 1 - Math.pow(
                        g,
                        2.0
                    )) * (v * g - 1 - g + Math.sqrt(
                        -2.0 * v * g - v * Math.pow(
                            g,
                            2.0
                        ) + 1.0 + 2.0 * g + Math.pow(g, 2.0) - v
                    ))
                ) / (v * Math.pow(g, 2.0) + 1 - Math.pow(g, 2.0))
            }

            2 -> {
                if (v >= 1.0 || v <= 0.0) {
                    INPUT.error = ("To/To* must be between 0 and 1")
                    return
                }
                m = Math.sqrt(
                    -(v * Math.pow(g, 2.0) + 1 - Math.pow(
                        g,
                        2.0
                    )) * (v * g - 1.0 - g - Math.sqrt(
                        -2.0 * v * g - v * Math.pow(
                            g,
                            2.0
                        ) + 1 + 2 * g + Math.pow(g, 2.0) - v
                    ))
                ) / (v * Math.pow(g, 2.0) + 1 - Math.pow(g, 2.0))
            }

            3 -> {
                if (v >= 1.03 || v <= 0.0) {
                    INPUT.error = ("T/T* (M<1/sqrt(gamma)) must be between 0 and 1.03")
                    return
                }
                m = Math.sqrt(
                    -2 * v * (2 * v * g - 1 - 2 * g - Math.pow(
                        g,
                        2.0
                    ) + Math.sqrt(
                        1 - 4.0 * v * g - 8.0 * v * Math.pow(g, 2.0) - 4 * v * Math.pow(
                            g,
                            3.0
                        ) + 4 * g + 6 * Math.pow(g, 2.0) + 4 * Math.pow(g, 3.0) + Math.pow(g, 4.0)
                    ))
                ) / (2.0 * v * g)
            }

            4 -> {
                if (v > 1.03 || v <= 0.0) {
                    INPUT.error = ("T/T* (M>1/sqrt(gamma)) must be between 0 and 1.03")
                    return
                }
                m = Math.sqrt(
                    -2 * v * (2 * v * g - 1 - 2 * g - Math.pow(
                        g,
                        2.0
                    ) - Math.sqrt(
                        1 - 4 * v * g - 8 * v * Math.pow(g, 2.0) - 4 * v * Math.pow(
                            g,
                            3.0
                        ) + 4 * g + 6 * Math.pow(g, 2.0) + 4.0 * Math.pow(g, 3.0) + Math.pow(g, 4.0)
                    ))
                ) / (2 * v * g)
            }
            5 -> {
                if (v > 2.4) {
                    INPUT.error = ("P/P* must be less than 2.4")
                    return
                }
                m = Math.sqrt(v * g * (1 + g - v)) / (v * g)
            }

            6 -> {
                if (v <= 1.0 || v >= (g + 1) * Math.pow(2 / (g + 1), g / (g - 1))) {
                    INPUT.error = ("Po/Po* must be > 1 and <=Po/Po* at M=0")
                    return
                }
                var fdiff = 5.0
                var xlo = 0.0000000001
                var xhi = 1.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = (1 + g) / (1 + g * Math.pow(
                        x,
                        2.0
                    )) * Math.pow(
                        (1.0 + (g - 1.0) / 2.0 * Math.pow(x, 2.0)) / ((g + 1.0) / 2.0),
                        g / (g - 1.0)
                    )
                    if (y > v) {
                        xlo = x
                    } else {
                        xhi = x
                    }
                    fdiff = Math.abs(v - y)

                    m = x
                }
            }
            7 -> {
                if (v <= 1.0) {
                    INPUT.error = ("Po/Po* must be greater than 1")
                    return
                }
                var fdiff = 5.0
                var xlo = 1.0
                var xhi = 100.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = (1 + g) / (1 + g * Math.pow(
                        x,
                        2.0
                    )) * Math.pow(
                        (1 + (g - 1.0) / 2.0 * Math.pow(x, 2.0)) / ((g + 1.0) / 2.0),
                        g / (g - 1.0)
                    )
                    if (y > v) {
                        xhi = x
                    } else {
                        xlo = x
                    }
                    fdiff = Math.abs(v - y)
                    m = x
                }
            }
            8 -> {
                if (v > 1.7144) {
                    INPUT.error = ("V/V* must be less than 1.7144")
                    return
                }
                m = -Math.sqrt(-(v * g - 1 - g) * v) / (v * g - 1 - g)
            }
            9 -> {
                if (v < 0.0) {
                    INPUT.error = ("Smax/R must be positive")
                    return
                }
                var fdiff = 5.0
                var xlo = 0.00000001
                var xhi = 1.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = -g / (g - 1) * Math.log(
                        Math.pow(
                            x,
                            2.0
                        ) * Math.pow((g + 1.0) / (1 + g * Math.pow(x, 2.0)), (g + 1.0) / g)
                    )
                    if (y > v) {
                        xlo = x
                    } else {
                        xhi = x
                    }
                    fdiff = Math.abs(v - y)

                    m = x
                }
            }
            10 -> {
                if (v < 0.0) {
                    INPUT.error = ("Smax/R must be positive")
                    return
                }
                var fdiff = 5.0
                var xlo = 1.0
                var xhi = 100.0
                while (fdiff >= 0.000000001) {
                    x = (xlo + xhi) / 2.0
                    y = -g / (g - 1) * Math.log(
                        Math.pow(
                            x,
                            2.0
                        ) * Math.pow((g + 1.0) / (1 + g * Math.pow(x, 2.0)), (g + 1.0) / g)
                    )
                    if (y > v) {
                        xhi = x
                    } else {
                        xlo = x
                    }
                    fdiff = Math.abs(v - y)

                    m = x
                }
            }

            0 -> {
                if (v <= 0.0) {
                    INPUT.error = ("M must be greater than 0")
                    return
                }
                m = v
            }
        }


        M.text = Math.abs(m).toString()
        TtTtSR.text = obj.TtTtSR(g, m).toString()
        TTSR.text = obj.TTSR(g, m).toString()
        PPSR.text = obj.PPSR(g, m).toString()
        PTPTSR.text = obj.PtPtSR(g, m).toString()
        VVSR.text = obj.VVSR(g, m).toString()
        SmaxRR.text = obj.SmaxRR(g, m).toString()
    }
}

