package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.Silicon.currentActionSet
import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.data.beans.Account
import com.sakurawald.silicon.data.beans.Page
import com.sakurawald.silicon.data.beans.request.StatusRequest
import com.sakurawald.silicon.data.beans.response.StatusResponse
import com.sakurawald.silicon.data.beans.response.SubmitResponse
import com.sakurawald.silicon.debug.LoggerManager.logDebug
import java.util.*

abstract class StatusAction : Action<StatusRequest?, StatusResponse?>() {
    abstract fun execute(statusRequest: StatusRequest): StatusResponse?
    @AUTO
    abstract fun supportStatusPageSkip(): Boolean
    @AUTO
    fun getFirstSatisfiedSubmitResponse(
        submitResponseArrayList: ArrayList<SubmitResponse>,
        userID: String,
        problemID: String
    ): SubmitResponse? {
        for (submitResponse in submitResponseArrayList) {
            if (userID == submitResponse.submitAccount!!.userID && problemID == submitResponse.problemID) return submitResponse
        }
        return null
    }

    @AUTO
    fun getAllHistoryStatus(account: Account): ArrayList<SubmitResponse?> {
        logDebug("getAllHistoryStatus >> Account = $account")
        val submitResponseArrayList = ArrayList<SubmitResponse?>()
        var requestHomePage = true
        while (true) {

            // Page Control Param.
            var page: String
            if (requestHomePage) {
                page = Page.HOME_PAGE
                requestHomePage = false
            } else {
                page = Page.NEXT_PAGE
            }
            val statusRequest = StatusRequest(account, null, account.userID, page)
            val statusResponse = currentActionSet.statusAction!!.execute(statusRequest)

            // 已没有下一页时, 代表所有记录都查询完毕.
            logDebug("getAllHistoryStatus >> current Page SubmitResponse count = " + statusResponse!!.submitResponses!!.size)
            if (statusResponse!!.submitResponses!!.size == 0) break
            submitResponseArrayList.addAll(statusResponse.submitResponses!!)
        }
        return submitResponseArrayList
    }
}