package com.sakurawald.silicon.data.beans

/**
 * 当某个Action在执行时遭到任何形式的错误时, 可设置ActionError对象为非null.
 * 当
 */
@Suppress("unused")
data class ActionError(var isError: Boolean, var errorMessage: String?)