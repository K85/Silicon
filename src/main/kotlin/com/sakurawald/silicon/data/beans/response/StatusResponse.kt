package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY
import java.util.*

class StatusResponse(submitResponses: ArrayList<SubmitResponse>?) : ResponseBean() {
    @NECESSARY
    var submitResponses: ArrayList<SubmitResponse>? = null
    override fun toString(): String {
        return "StatusResponse{" +
                "submitResponses=" + submitResponses +
                '}'
    }

    init {
        this.submitResponses = submitResponses
    }
}