package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Problem
import java.util.*

class ProblemsResponse(@field:NECESSARY var problems: ArrayList<Problem>) : ResponseBean() {
    override fun toString(): String {
        return "ProblemsResponse{" +
                "problems=" + problems +
                '}'
    }
}