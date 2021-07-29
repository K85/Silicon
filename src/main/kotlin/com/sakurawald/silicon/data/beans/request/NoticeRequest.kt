package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO_USE
import com.sakurawald.silicon.annotation.NECESSARY
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent

open class NoticeRequest : RequestBean {
    @NECESSARY
    val noticeLabel: Label
    var noticeMouseEvent: MouseEvent? = null
        private set

    /**
     * 标记该次的公告更新请求是否由Silicon自动发起.
     */
    var isAutoUpdate = false
        private set

    @AUTO_USE
    constructor(notice_label: Label, autoUpdate: Boolean) {
        this.noticeLabel = notice_label
        isAutoUpdate = autoUpdate
    }

    @AUTO_USE
    constructor(notice_label: Label, notice_mouse_event: MouseEvent?) {
        this.noticeLabel = notice_label
        this.noticeMouseEvent = notice_mouse_event
    }

}