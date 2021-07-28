package com.sakurawald.silicon.data.beans

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.request.LoginRequest

class Account(@field:NECESSARY var userID: String?, @field:NECESSARY var password: String?) {

    var nickname: String? = null
    var school: String? = null
    var email: String? = null

    /**
     * 登陆后的身份验证凭证.
     */
    private var token: String? = null

    /**
     * 账号缓存管理: 未登录状态调用该方法则会自动登录并保存账号状态.
     */
    fun getToken(): String? {
        if (userID != null && password != null) {
            if (token != null) return token
            val loginResponse = currentActionSet.loginAction!!.execute(LoginRequest(this))
            setToken(loginResponse!!.token)
        }
        return token
    }

    fun setToken(token: String?) {
        this.token = token
    }

    override fun toString(): String {
        return "Account{" +
                "userID='" + userID + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", school='" + school + '\'' +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}'
    }

    companion object {
        fun isEmpty(account: Account?): Boolean {
            return (account == null || account.userID == null || account.password == null || account.userID!!.trim { it <= ' ' }.isEmpty()
                    || account.password!!.trim { it <= ' ' }.isEmpty())
        }
    }
}