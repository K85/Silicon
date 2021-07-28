package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

class LoginRequest : RequestBean {
    @NECESSARY
    var userID: String?

    @NECESSARY
    var password: String?
    override fun toString(): String {
        return "LoginRequest{" +
                "userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                '}'
    }

    constructor(account: Account) {
        userID = account.userID
        password = account.password
    }

    constructor(userID: String?, password: String?) {
        this.userID = userID
        this.password = password
    }
}