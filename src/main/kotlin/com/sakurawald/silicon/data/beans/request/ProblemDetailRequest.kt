package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Problem

class ProblemDetailRequest @AUTO constructor(problem: Problem?) : RequestBean() {
    @NECESSARY
    var problem: Problem? = null
    override fun toString(): String {
        return "ProblemDetailRequest{" +
                "problem=" + problem +
                '}'
    }

    init {
        this.problem = problem
    }
}