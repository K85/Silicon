package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.SubmitResult

/**
 * 由单次Submit返回的单个SubmitResponse.
 * SubmitResponse对象可由Status获得.
 * 如果在SubmitAction中无法返回SubmitResponse对象, 则直接返回null即可.
 */
open class SubmitResponse : ResponseBean {
    var runID: String = ""
    var submitAccount: Account = Account(null, null)
    var problemID: String = ""
    var language: String = ""
    var memory: String? = null
    var time: String? = null
    var codeLength: String? = null
    var submitTime: String? = null
    var submitResult: SubmitResult = SubmitResult.UNKNOWN

    constructor(
        runID: String,
        submitAccount: Account,
        problemID: String,
        language: String,
        memory: String?,
        time: String?,
        codeLength: String?,
        submitTime: String?,
        submitResult: SubmitResult
    ) {
        this.runID = runID
        this.submitAccount = submitAccount
        this.problemID = problemID
        this.language = language
        this.memory = memory
        this.time = time
        this.codeLength = codeLength
        this.submitTime = submitTime
        this.submitResult = submitResult
    }

    /**
     * 用于SubmitRequestAction的返回.
     */
    constructor()

    val formatedString: String
        get() = """
               Run ID: $runID
               User ID: ${submitAccount.userID}
               Problem ID: $problemID
               Language: $language
               Time: $time
               Memory: $memory
               Code Length: $codeLength
               SubmitTime: $submitTime
               Result: $submitResult
               """.trimIndent()


}