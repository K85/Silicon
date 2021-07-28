package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.data.beans.request.SourceDetailRequest
import com.sakurawald.silicon.data.beans.response.SourceDetailResponse

abstract class SourceDetailAction : Action<SourceDetailRequest?, SourceDetailResponse?>() {
    abstract fun execute(sourceDetailRequest: SourceDetailRequest): SourceDetailResponse?
    @AUTO
    abstract fun supportAccountClone(): Boolean
}