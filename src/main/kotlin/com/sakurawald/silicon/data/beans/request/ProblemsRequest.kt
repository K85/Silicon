package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.data.beans.Account

class ProblemsRequest : RequestBean {
    @NECESSARY
    var requestAccount: Account? = null

    @NECESSARY
    var page: String? = null

    /**
     * 在支持问题搜索的系统中, 该变量表示搜索参数.
     */
    var problemSearchKey: String? = null

    @AUTO
    constructor(account: Account?, page: String?) {
        requestAccount = account
        this.page = page
    }

    override fun toString(): String {
        return "ProblemsRequest{" +
                "requestAccount=" + requestAccount +
                ", page='" + page + '\'' +
                ", problemSearchKey='" + problemSearchKey + '\'' +
                '}'
    }

    @AUTO
    constructor(account: Account?, page: String?, problemSearchKey: String?) {
        requestAccount = account
        this.page = page
        this.problemSearchKey = problemSearchKey
    }
}