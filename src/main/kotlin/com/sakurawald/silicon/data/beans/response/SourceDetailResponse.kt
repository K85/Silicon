package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.SubmitResult

class SourceDetailResponse(
    submitAccount: Account?,
    runID: String?,
    HTML: String?,
    submitResult: SubmitResult?,
    source: String?
) : ResponseBean() {
    @NECESSARY
    var submitAccount: Account? = null

    @NECESSARY
    var runID: String? = null

    @NECESSARY
    var hTML: String? = null

    @NECESSARY
    var submitResult: SubmitResult? = null

    /**
     * 提取后的纯文本代码.
     * 注意: source值在非空情况下, 才可支持 Account Clone 功能.
     */
    @NECESSARY
    var source: String? = null
    override fun toString(): String {
        return "SourceDetailResponse{" +
                "submitAccount=" + submitAccount +
                ", runID='" + runID + '\'' +
                ", HTML='" + hTML + '\'' +
                ", submitResult=" + submitResult +
                ", source='" + source + '\'' +
                '}'
    }

    init {
        this.submitAccount = submitAccount
        this.runID = runID
        hTML = HTML
        this.submitResult = submitResult
        this.source = source
    }
}