package com.sakurawald.silicon.data.beans

/**
 * 当某个Action在执行时遭到任何形式的错误时, 可设置ActionError对象为非null.
 */
class ActionError {
    var isError: Boolean
    var errorMessage: String? = null
    override fun toString(): String {
        return "ActionError{" +
                "error=" + isError +
                ", errorMessage='" + errorMessage + '\'' +
                '}'
    }

    constructor(error: Boolean) {
        isError = error
    }

    constructor(error: Boolean, errorMessage: String?) {
        isError = error
        this.errorMessage = errorMessage
    }
}