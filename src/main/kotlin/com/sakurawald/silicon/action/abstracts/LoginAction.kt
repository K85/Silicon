package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.LoginRequest
import com.sakurawald.silicon.data.beans.response.LoginResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import okhttp3.Response

abstract class LoginAction : Action<LoginRequest?, LoginResponse?>() {
    abstract fun execute(loginRequest: LoginRequest): LoginResponse?
    fun getLoginMessage(response: Response): String {
        return response.code.toString() + " " + response.message
    }

    fun getLoginToken(response: Response, cookie_name: String): String? {
        /** Analyse Cookies.  */
        val set_cookie = response.headers("set-cookie").toString()
        logDebug("Login Action: set-cookie = $set_cookie")
        val offset = cookie_name.length + 1
        var token: String? = null
        try {
            token = set_cookie.substring(set_cookie.indexOf("$cookie_name=") + offset, set_cookie.indexOf(";"))
        } catch (e: StringIndexOutOfBoundsException) {
            LoggerManager.logError(e)
        }
        return token
    }
}