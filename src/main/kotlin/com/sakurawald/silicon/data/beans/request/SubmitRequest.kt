package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

class SubmitRequest @AUTO constructor(
    @field:NECESSARY var submitAccount: Account, @field:NECESSARY var problemID: String,
    /**
     * language使用字符串, 以方便不同平台进行通用.
     */
    var language: String, @field:NECESSARY var code: String
) : RequestBean() {

    override fun toString(): String {
        return "SubmitRequest{" +
                "submitAccount=" + submitAccount +
                ", problemID=" + problemID +
                ", code='" + code + '\'' +
                ", language='" + language + '\'' +
                '}'
    }
}