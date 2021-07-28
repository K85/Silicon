package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY
import javafx.scene.control.*
import javafx.scene.input.MouseEvent

class NoticeRequest : RequestBean {
    @NECESSARY
    val notice_label: Label
    var notice_mouse_event: MouseEvent? = null
        private set

    /**
     * 标记该次的公告更新请求是否由Silicon自动发起.
     */
    var isAutoUpdate = false
        private set

    @AUTO
    constructor(notice_label: Label, autoUpdate: Boolean) {
        this.notice_label = notice_label
        isAutoUpdate = autoUpdate
    }

    @AUTO
    constructor(notice_label: Label, notice_mouse_event: MouseEvent?) {
        this.notice_label = notice_label
        this.notice_mouse_event = notice_mouse_event
    }

    override fun toString(): String {
        return "NoticeRequest{" +
                "notice_label=" + notice_label +
                ", notice_mouse_event=" + notice_mouse_event +
                ", autoUpdate=" + isAutoUpdate +
                '}'
    }
}