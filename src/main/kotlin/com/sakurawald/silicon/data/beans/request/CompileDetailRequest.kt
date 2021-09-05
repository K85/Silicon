package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

@NECESSARY
open class CompileDetailRequest(var submitAccount: Account?, var runID: String?) : RequestBean()