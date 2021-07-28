package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Problem

class ProblemDetailResponse(ownerProblem: Problem?) : ResponseBean() {
    @NECESSARY
    var ownerProblem: Problem? = null

    @NECESSARY
    var hTML: String? = null
    override fun toString(): String {
        return "ProblemDetailResponse{" +
                "ownerProblem=" + ownerProblem +
                ", HTML='" + hTML + '\'' +
                '}'
    }

    init {
        this.ownerProblem = ownerProblem
    }
}