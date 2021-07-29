package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.data.beans.Account

@Suppress("unused")
open class LoginRequest(var userID: String?, var password: String?) : RequestBean() {
    constructor(account: Account) : this(account.userID, account.password)
}