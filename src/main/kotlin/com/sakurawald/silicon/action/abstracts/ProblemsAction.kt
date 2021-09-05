package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.request.ProblemsRequest
import com.sakurawald.silicon.data.beans.response.ProblemsResponse

abstract class ProblemsAction : Action<ProblemsRequest, ProblemsResponse>() {
    abstract override fun execute(requestBean: ProblemsRequest): ProblemsResponse

    @NECESSARY
    abstract fun supportProblemSearch(): Boolean
}