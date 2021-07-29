package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.annotation.AUTO_USE
import com.sakurawald.silicon.data.beans.request.SourceDetailRequest
import com.sakurawald.silicon.data.beans.response.SourceDetailResponse

abstract class SourceDetailAction : Action<SourceDetailRequest, SourceDetailResponse>() {
    abstract override fun execute(requestBean: SourceDetailRequest): SourceDetailResponse

    @AUTO_USE
    abstract fun supportAccountClone(): Boolean
}