package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.data.beans.Account

@Suppress("unused")
open class CompileDetailResponse(var submitAccount: Account?, var runID: String?, var HTML: String?) : ResponseBean()