package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.data.beans.Account

open class SourceDetailRequest(var submitAccount: Account?, var runID: String?) : RequestBean()