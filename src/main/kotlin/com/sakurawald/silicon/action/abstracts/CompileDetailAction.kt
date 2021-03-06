package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.CompileDetailRequest
import com.sakurawald.silicon.data.beans.response.CompileDetailResponse

abstract class CompileDetailAction : Action<CompileDetailRequest, CompileDetailResponse>() {
    abstract override fun execute(requestBean: CompileDetailRequest): CompileDetailResponse
}