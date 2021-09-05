package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.request.SourceDetailRequest
import com.sakurawald.silicon.data.beans.response.SourceDetailResponse

abstract class SourceDetailAction : Action<SourceDetailRequest, SourceDetailResponse>() {
    abstract override fun execute(requestBean: SourceDetailRequest): SourceDetailResponse

    @NECESSARY
    abstract fun supportAccountClone(): Boolean
}