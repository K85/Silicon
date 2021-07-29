package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.SubmitRequest
import com.sakurawald.silicon.data.beans.response.SubmitResponse

abstract class SubmitAction : Action<SubmitRequest, SubmitResponse>() {
    abstract override fun execute(requestBean: SubmitRequest): SubmitResponse
}