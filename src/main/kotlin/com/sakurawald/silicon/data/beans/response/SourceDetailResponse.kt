package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.SubmitResult

/**
 * @param source 纯文本代码, 用于Account Clone功能. 若不支持该功能则可设null.
 */
@Suppress("unused")
open class SourceDetailResponse(
    var submitAccount: Account?,
    var runID: String?,
    var HTML: String?,
    var submitResult: SubmitResult?,
    var source: String?
) : ResponseBean()