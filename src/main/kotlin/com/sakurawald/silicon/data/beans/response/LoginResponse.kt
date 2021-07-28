package com.sakurawald.silicon.data.beans.response

import com.sakurawald.silicon.annotation.NECESSARY

class LoginResponse : ResponseBean {
    @NECESSARY
    var token: String

    /**
     * 登陆时附带的返回信息.
     */
    var loginMessage: String? = null
    override fun toString(): String {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                '}'
    }

    constructor(token: String) {
        this.token = token
    }

    constructor(token: String, loginMessage: String?) {
        this.token = token
        this.loginMessage = loginMessage
    }
}