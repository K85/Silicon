package com.sakurawald.silicon.data.beans

enum class ProblemStatus(statusID: Int, statusName: String) {
    UNKNOWN(9999, " "), ACCEPTED(1, "√"), WRONG(0, "×"), NEVER_TRY(2, " ");

    var statusID = 0
    var statusName: String? = null
    fun same(statusName: String): Boolean {
        return this.statusName == statusName
    }

    override fun toString(): String {
        return statusName!!
    }

    init {
        this.statusID = statusID
        this.statusName = statusName
    }
}