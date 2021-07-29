package com.sakurawald.silicon.data.beans

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.debug.LoggerManager

@Suppress("unused", "NAME_SHADOWING", "PropertyName")
class Problem {
    var problemStatus = ProblemStatus.UNKNOWN

    @NECESSARY
    var problemID: String? = null
    var problemTitle: String? = null
    var ratio = 0.0
    var AC = 0
    var submit = 0
    var difficulty = 0.0
    var date: String? = null

    constructor() {
        // Do nothing.
    }

    override fun toString(): String {
        return "Problem{problemStatus=$problemStatus, problemID='$problemID', problemTitle='$problemTitle', ratio=$ratio, AC=$AC, submit=$submit, difficulty=$difficulty, date='$date'}"
    }

    constructor(problemID: String?) {
        this.problemID = problemID
    }

    val difficultyPercentage: String?
        get() = if (difficulty == 0.0) null else transPercentage(difficulty)
    val rAS: String?
        get() = if (ratio == 0.0 && AC == 0 && submit == 0) null else String.format(
            "%.0f",
            ratio * 100
        ) + "%(" + AC + "/" + submit + ")"

    companion object {
        @JvmStatic
        fun transPercentage(percentage: Double): String {
            return String.format("%.2f", percentage * 100) + "%"
        }

        @JvmStatic
        fun transPercentage(percentage: String): Double {
            var percentage = percentage
            var ret = 0.0
            percentage = percentage.replace("%", "")
            try {
                ret = percentage.toDouble() / 100
            } catch (e: NumberFormatException) {
                LoggerManager.reportException(e)
            }
            return ret
        }

        @JvmStatic
        fun transNumber(number: String): Int {
            var ret = 0
            try {
                ret = number.toInt()
            } catch (e: NumberFormatException) {
                LoggerManager.reportException(e)
            }
            return ret
        }
    }
}