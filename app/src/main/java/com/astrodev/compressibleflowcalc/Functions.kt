package com.astrodev.compressibleflowcalc

import kotlin.math.sqrt

class Functions {

    fun TtTtSR(g: Double, m: Double): Double {
        return 2 * (1 + g) * Math.pow(m, 2.0) / Math.pow(
            1 + g * Math.pow(m, 2.0),
            2.0
        ) * (1 + ((g - 1) / 2) * Math.pow(m, 2.0))
    }

    fun TTSR(g: Double, m: Double): Double {
        return Math.pow(m, 2.0) * Math.pow(1 + g, 2.0) / Math.pow((1 + g * Math.pow(m, 2.0)), 2.0)
    }

    fun PPSR(g: Double, m: Double): Double {
        return (1 + g) / (1 + g * Math.pow(m, 2.0))
    }

    fun PtPtSR(g: Double, m: Double): Double {
        return (1 + g) / (1 + g * Math.pow(m, 2.0)) * Math.pow(
            (1 + (g - 1) / 2 * Math.pow(
                m,
                2.0
            )) / ((g + 1) / 2), g / (g - 1)
        )
    }

    fun VVSR(g: Double, m: Double): Double {
        return (1 + g) * Math.pow(m, 2.0) / (1 + g * Math.pow(m, 2.0))
    }

    fun SmaxRR(g: Double, m: Double): Double {
        return -g / (g - 1) * Math.log(
            Math.pow(m, 2.0) * Math.pow(
                (g + 1) / (1 + g * Math.pow(
                    m,
                    2.0
                )), (g + 1) / g
            )
        )
    }

    fun TTSF(g: Double, m: Double): Double {
        return ((g + 1) / 2) / (1 + ((g - 1) / 2) * Math.pow(m, 2.0))
    }

    fun PPSF(g: Double, m: Double): Double {
        return (1 / m) * Math.sqrt(((g + 1) / 2) / (1 + ((g - 1) / 2) * Math.pow(m, 2.0)))
    }

    fun PTPTSF(g: Double, m: Double): Double {
        return (1.0 / m) * Math.pow(
            (1.0 + ((g - 1.0) / 2.0) * Math.pow(
                m,
                2.0
            )) / ((g + 1.0) / 2.0), ((g + 1.0) / (2.0 * (g - 1.0)))
        )
    }

    fun VVSF(g: Double, m: Double): Double {
        return m * Math.sqrt(((g + 1.0) / 2.0) / (1.0 + ((g - 1.0) / 2.0) * Math.pow(m, 2.0)))
    }

    fun fLmaxD(g: Double, m: Double): Double {
        return ((g + 1) / (2 * g)) * Math.log(
            ((g + 1) / 2) * Math.pow(
                m,
                2.0
            ) / (1 + ((g - 1) / 2) * Math.pow(m, 2.0))
        ) + (1 / g) * (1 / Math.pow(m, 2.0) - 1)
    }

    fun SmaxRF(g: Double, m: Double): Double {
        return Math.log(
            (1 / m) * Math.pow(
                (1.0 + ((g - 1) / 2) * Math.pow(
                    m,
                    2.0
                )) / (1 + ((g - 1) / 2)), (g + 1) / (2 * (g - 1))
            )
        )
    }


    fun tt0(g: Double, m: Double): Double {
        return Math.pow(((1 + (g - 1) / 2 * m * m)), (-1.0))
    }

    fun pp0(g: Double, m: Double): Double {
        return Math.pow(((1 + (g - 1) / 2 * m * m)), (-g / (g - 1)))
    }

    fun rr0(g: Double, m: Double): Double {
        return Math.pow(((1 + (g - 1) / 2 * m * m)), (-1 / (g - 1)))
    }


    fun tts(g: Double, m: Double): Double {
        return tt0(g, m) * (g / 2 + 0.5)
    }

    fun pps(g: Double, m: Double): Double {
        return pp0(g, m) * Math.pow((g / 2 + .5), (g / (g - 1)))
    }

    fun rrs(g: Double, m: Double): Double {
        return rr0(g, m) * Math.pow((g / 2 + .5), (1 / (g - 1)))
    }

    fun aas(g: Double, m: Double): Double {
        return 1 / rrs(g, m) * sqrt(1 / tts(g, m)) / m
    }

    fun m2(g: Double, m1: Double): Double {
        return Math.sqrt((1 + 0.5 * (g - 1.0) * m1 * m1) / (g * m1 * m1 - 0.5 * (g - 1)))
    }


    fun nu(g: Double, m: Double): Double {
        var n =
            Math.sqrt(((g + 1) / (g - 1))) * Math.atan(Math.sqrt(((g - 1) / (g + 1) * (m * m - 1))))
        n -= Math.atan(Math.sqrt((m * m - 1)))
        n = n * 180 / 3.14159265359
        return n
    }

    fun mdb(g: Double, m1: Double, d: Double, i: Int): Double {
        val p = -(m1 * m1 + 2) / m1 / m1 - g * Math.sin(d) * Math.sin(d)
        val q = (2.0 * m1 * m1 + 1.0) / Math.pow(
            m1,
            4.0
        ) + ((g + 1.0) * (g + 1) / 4 + (g - 1) / m1 / m1) * Math.sin(d) * Math.sin(d)
        val r = -Math.cos(d) * Math.cos(d) / Math.pow(m1, 4.0)

        val a = (3 * q - p * p) / 3
        val b = (2.0 * p * p * p - 9.0 * p * q + 27.0 * r) / 27.0

        val test = b * b / 4 + a * a * a / 27
        var x1 = 0.0
        var x2 = 0.0
        var x3 = 0.0
        val phi: Double
        val s1: Double
        val s2: Double
        val s3: Double


        if (test > 0.0) {
            return -1.0
        } else {
            if (test == 0.0) {
                x1 = Math.sqrt(-a / 3.0)
                x2 = x1
                x3 = 2 * x1
                if (b > 0.0) {
                    x1 *= -1
                    x2 *= -1
                    x3 *= -1
                }
            }
            if (test < 0.0) {
                phi = Math.acos(Math.sqrt(-27 * b * b / 4 / a / a / a))
                x1 = 2 * Math.sqrt(-a / 3) * Math.cos(phi / 3)
                x2 = 2 * Math.sqrt(-a / 3) * Math.cos(phi / 3 + 3.14159265359 * 2 / 3)
                x3 = 2 * Math.sqrt(-a / 3) * Math.cos(phi / 3 + 3.14159265359 * 4 / 3)
                if (b > 0.0) {
                    x1 *= -1.0
                    x2 *= -1.0
                    x3 *= -1.0
                }
            }
            val t1: Double
            val t2: Double
            var t3: Double
            s1 = x1 - p / 3.0
            s2 = x2 - p / 3.0
            s3 = x3 - p / 3.0

            if (s1 < s2 && s1 < s3) {
                t1 = s2
                t2 = s3
            } else if (s2 < s1 && s2 < s3) {
                t1 = s1
                t2 = s3
            } else {
                t1 = s1
                t2 = s2
            }

            val b1 = Math.asin(Math.sqrt(t1))
            val b2 = Math.asin(Math.sqrt(t2))

            var betas = b1
            var betaw = b2
            if (b2 > b1) {
                betas = b2
                betaw = b1
            }

            if (i == 0) {
                return betaw
            } else {
                return betas
            }
        }
    }

    fun mbd(g: Double, m1: Double, b: Double): Double {
        return Math.atan(
            (m1 * m1 * Math.sin(2.0 * b) - 2.0 / Math.tan(b)) / (2 + m1 * m1 * (g + Math.cos(
                2 * b
            )))
        )
    }

    fun mbs(g: Double, m1: Double, beta: Double): Array<Double> {
        // Calculates cone angle and mach number at surface of cone given
        //gamma, free-stream mach and wave angle

        var theta0 = beta

        // Calculate delta from mbd relation, check for detachment
        val delta = mbd(g, m1, beta)
        if (delta < 0.0) {
            val temp = arrayOf(-1.0, -1.0)
            return temp
        }

        // Calculate M2
        val m1n = m1 * Math.sin(beta)
        val m2n = m2(g, m1n)
        val m22 = m2n / Math.sin(beta - delta)

        // Calculate nondimmensional components in theta and r direction
        // These are ODE initial values
        val V0 = Math.pow((1.0 + 2.0 / ((g - 1.0) * m22 * m22)), (-0.5))
        var Vr0 = V0 * Math.cos(theta0 - delta)
        var Vtheta0 = -V0 * Math.sin(theta0 - delta)

        // Step theta down in increments of h using a Runge-Kutta 4th order
        // estimation. Stop when Vtheta is zero, which occurs at the cone surface
        val h = -0.0005
        val pi = 3.14159265359
        while (Vtheta0 < 0.0 && theta0 < pi / 2) {

            val k1 = dvr(theta0, Vr0, Vtheta0, g)
            val j1 = dvtheta(theta0, Vr0, Vtheta0, g)
            val k2 = dvr(theta0 + h / 2.0, Vr0 + (h / 2.0) * k1, Vtheta0 + (h / 2.0) * j1, g)
            val j2 = dvtheta(theta0 + h / 2.0, Vr0 + (h / 2.0) * k1, Vtheta0 + (h / 2.0) * j1, g)
            val k3 = dvr(theta0 + h / 2.0, Vr0 + (h / 2.0) * k2, Vtheta0 + (h / 2.0) * j2, g)
            val j3 = dvtheta(theta0 + h / 2.0, Vr0 + (h / 2.0) * k2, Vtheta0 + (h / 2.0) * j2, g)
            val k4 = dvr(theta0 + h, Vr0 + h * k3, Vtheta0 + h * j3, g)
            val j4 = dvtheta(theta0 + h, Vr0 + h * k3, Vtheta0 + h * j3, g)

            Vr0 += (h / 6.0) * (k1 + 2.0 * k2 + 2.0 * k3 + k4)
            Vtheta0 += (h / 6.0) * (j1 + 2.0 * j2 + 2.0 * j3 + j4)

            theta0 += h
        }

        val sigma = theta0
        val mc = (Math.sqrt(2.0) * Vr0) / Math.sqrt(g - g * Vr0 * Vr0 + Vr0 * Vr0 - 1.0)
        val temp = arrayOf(sigma, mc)
        return temp
    }

    fun dvr(beta: Double, Vr: Double, Vtheta: Double, gamma: Double): Double {
        return Vtheta
    }

    fun dvtheta(beta: Double, Vr: Double, Vtheta: Double, gamma: Double): Double {

        return (Vtheta * Vtheta * Vr - (gamma - 1.0) / 2.0 * (1.0 - Vr * Vr - Vtheta * Vtheta) * (2.0 * Vr + Vtheta * (1.0 / Math.tan(
            beta
        )))) / ((gamma - 1.0) / 2.0 * (1.0 - Vr * Vr - Vtheta * Vtheta) - Vtheta * Vtheta)
    }
}