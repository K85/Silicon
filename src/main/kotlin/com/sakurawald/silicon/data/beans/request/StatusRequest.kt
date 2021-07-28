package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

class StatusRequest @AUTO constructor(requestAccount: Account?, problemID: String?, userID: String?, page: String?) :
    RequestBean() {
    /**
     * 根据ProblemID 和 UserID进行过滤, 若全部为空, 则不进行过滤.
     */
    @NECESSARY
    var requestAccount: Account? = null

    @NECESSARY
    var problemID: String? = null

    @NECESSARY
    var userID: String? = null

    /**
     * 在支持跳页查询的系统中, page参数直接表示页码数.
     * 在不支持跳页查询的系统中, page参数表示操作类型.
     */
    @NECESSARY
    var page: String? = null
    override fun toString(): String {
        return "StatusRequest{" +
                "requestAccount=" + requestAccount +
                ", problemID='" + problemID + '\'' +
                ", userID='" + userID + '\'' +
                ", page='" + page + '\'' +
                '}'
    }

    init {
        this.requestAccount = requestAccount
        this.problemID = problemID
        this.userID = userID
        this.page = page
    }
}