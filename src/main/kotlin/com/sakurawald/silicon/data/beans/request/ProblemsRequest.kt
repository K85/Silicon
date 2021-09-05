package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.NECESSARY
import com.sakurawald.silicon.annotation.OPTIONAL
import com.sakurawald.silicon.data.beans.Account

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class ProblemsRequest(var requestAccount: Account?, var page: String?) : RequestBean() {

    /**
     * 在支持搜索的系统中, 该变量表示搜索参数.
     */
    @OPTIONAL
    var problemSearchKey: String? = null

    @NECESSARY
    constructor(account: Account?, page: String?, problemSearchKey: String?) : this(account, page) {
        this.problemSearchKey = problemSearchKey
    }
}