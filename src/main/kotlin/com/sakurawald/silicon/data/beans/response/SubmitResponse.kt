package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.*

/**
 * 由单词Submit返回的单个SubmitResponse.
 * SubmitResponse对象可由Status获得.
 * 如果在SubmitAction中无法返回SubmitResponse对象, 则直接返回null即可.
 */
class SubmitResponse : ResponseBean() {
    @NECESSARY
    var runID: String? = null

    @NECESSARY
    var submitAccount: Account? = null

    @NECESSARY
    var problemID: String? = null
    var memory: String? = null
    var time: String? = null
    var language: String? = null
    var codeLength: String? = null
    var submitTime: String? = null
    var submitResult: SubmitResult? = null
    override fun toString(): String {
        return "SubmitResponse{" +
                "runID='" + runID + '\'' +
                ", submitAccount=" + submitAccount +
                ", problemID='" + problemID + '\'' +
                ", memory='" + memory + '\'' +
                ", time='" + time + '\'' +
                ", language='" + language + '\'' +
                ", codeLength='" + codeLength + '\'' +
                ", submitTime='" + submitTime + '\'' +
                ", submitResult=" + submitResult +
                '}'
    }

    val formatedString: String
        get() = """
               Run ID: ${runID}
               User ID: ${submitAccount!!.userID}
               Problem ID: ${problemID}
               Time: ${time}
               Memory: ${memory}
               Language: ${language}
               Code Length: ${codeLength}
               SubmitTime: ${submitTime}
               Result: ${submitResult}
               """.trimIndent()
}