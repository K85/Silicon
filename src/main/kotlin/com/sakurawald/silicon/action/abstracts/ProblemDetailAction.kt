package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.ProblemDetailRequest
import com.sakurawald.silicon.data.beans.response.ProblemDetailResponse

abstract class ProblemDetailAction : Action<ProblemDetailRequest?, ProblemDetailResponse?>() {
    abstract fun execute(problemDetailRequest: ProblemDetailRequest): ProblemDetailResponse?
}