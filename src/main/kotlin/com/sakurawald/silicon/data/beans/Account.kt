package com.sakurawald.silicon.data.beans

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.request.LoginRequest

@Suppress("unused", "MemberVisibilityCanBePrivate")
data class Account(@NECESSARY var userID: String?, @NECESSARY var password: String?) {
    var nickname: String? = null
    var school: String? = null
    var email: String? = null

    /**
     * 登陆后的身份验证凭证.
     */
    var token: String? = null
        get() {
            if (userID != null && password != null) {
                if (field != null) return field
                val loginResponse = currentActionSet.loginAction.execute(LoginRequest(this))
                return loginResponse.token
            }
            return null
        }

    companion object {
        @JvmStatic
        fun isEmpty(account: Account?): Boolean {
            return (account?.userID == null || account.password == null || account.userID!!.trim { it <= ' ' }.isEmpty()
                    || account.password!!.trim { it <= ' ' }.isEmpty())
        }
    }
}