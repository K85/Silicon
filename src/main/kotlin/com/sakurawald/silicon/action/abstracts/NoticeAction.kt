package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.api.HitoKotoAPI
import com.sakurawald.silicon.data.beans.request.NoticeRequest
import com.sakurawald.silicon.data.beans.response.NoticeResponse
import javafx.application.Platform

/**
 * 用于Silicon的公告板的指令集.
 */
abstract class NoticeAction : Action<NoticeRequest?, NoticeResponse?>() {
    override fun execute(requestBean: NoticeRequest?): NoticeResponse? {
        val sentence = HitoKotoAPI.randomSentence
        Platform.runLater { requestBean!!.notice_label.text = sentence.formatedString }
        return null
    }
}