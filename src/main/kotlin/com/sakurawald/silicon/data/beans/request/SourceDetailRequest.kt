package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

class SourceDetailRequest @AUTO constructor(submitAccount: Account?, runID: String?) : RequestBean() {
    @NECESSARY
    var submitAccount: Account? = null

    @NECESSARY
    var runID: String? = null
    override fun toString(): String {
        return "SourceDetailRequest{" +
                "submitAccount=" + submitAccount +
                ", runID='" + runID + '\'' +
                '}'
    }

    init {
        this.submitAccount = submitAccount
        this.runID = runID
    }
}