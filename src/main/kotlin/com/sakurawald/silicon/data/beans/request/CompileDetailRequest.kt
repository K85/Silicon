package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO_USE
import com.sakurawald.silicon.data.beans.Account

@AUTO_USE
open class CompileDetailRequest(var submitAccount: Account?, var runID: String?) : RequestBean()