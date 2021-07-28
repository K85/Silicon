package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

class CompileDetailResponse(submitAccount: Account?, runID: String?, HTML: String?) : ResponseBean() {
    @NECESSARY
    var submitAccount: Account? = null

    @NECESSARY
    var runID: String? = null

    @NECESSARY
    var hTML: String? = null
    override fun toString(): String {
        return "CompileDetailResponse{" +
                "submitAccount=" + submitAccount +
                ", runID='" + runID + '\'' +
                ", HTML='" + hTML + '\'' +
                '}'
    }

    init {
        this.submitAccount = submitAccount
        this.runID = runID
        hTML = HTML
    }
}