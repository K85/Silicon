package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.SubmitResult

/**
 * 由单次Submit返回的单个SubmitResponse.
 * SubmitResponse对象可由Status获得.
 * 如果在SubmitAction中无法返回SubmitResponse对象, 则直接返回null即可.
 */
open class SubmitResponse(
    var runID: String,
    var submitAccount: Account,
    var problemID: String,
    var language: String,
    var memory: String?,
    var time: String?,
    var codeLength: String?,
    var submitTime: String?,
    var submitResult: SubmitResult
) : ResponseBean() {

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