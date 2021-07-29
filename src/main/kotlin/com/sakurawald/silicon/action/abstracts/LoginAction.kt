package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.LoginRequest
import com.sakurawald.silicon.data.beans.response.LoginResponse
import com.sakurawald.silicon.debug.LoggerManager
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import okhttp3.Response

abstract class LoginAction : Action<LoginRequest, LoginResponse>() {
    abstract override fun execute(requestBean: LoginRequest): LoginResponse

    companion object {
        @JvmStatic
        fun getLoginMessage(response: Response): String {
            return response.code.toString() + " " + response.message
        }

        /**
         * 根据HTTP请求中Headers中的set-cookie参数获取cookie.
         */
        @JvmStatic
        fun getLoginToken(response: Response, cookie_name: String): String? {
            /** Analyse Cookies.  */
            val setCookie = response.headers("set-cookie").toString()

            logDebug("Login Action: set-cookie = $setCookie")
            val offset = cookie_name.length + 1
            var token: String? = null
            try {
                token = setCookie.substring(setCookie.indexOf("$cookie_name=") + offset, setCookie.indexOf(";"))
            } catch (e: StringIndexOutOfBoundsException) {
                LoggerManager.reportException(e)
            }
            return token
        }
    }
}