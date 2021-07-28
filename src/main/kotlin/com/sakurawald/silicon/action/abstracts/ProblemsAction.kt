package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.data.beans.request.ProblemsRequest
import com.sakurawald.silicon.data.beans.response.ProblemsResponse

abstract class ProblemsAction : Action<ProblemsRequest?, ProblemsResponse?>() {
    abstract fun execute(problemsRequest: ProblemsRequest): ProblemsResponse?
    @AUTO
    abstract fun supportProblemSearch(): Boolean
}