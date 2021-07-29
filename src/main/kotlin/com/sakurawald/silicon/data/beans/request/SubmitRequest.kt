package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.data.beans.Account

@Suppress("unused")
open class SubmitRequest(var submitAccount: Account, var problemID: String, var language: String, var code: String) :
    RequestBean()