package com.sakurawald.silicon.data.beans

open class ActionBean {
    var actionError: ActionError? = null

    fun isActionError(): Boolean {
        return actionError != null && actionError!!.isError
    }

    override fun toString(): String {
        return "ActionBean(actionError=$actionError)"
    }
}