package com.sakurawald.silicon.data.beans

import com.sakurawald.silicon.annotation.NECESSARY

class Language(@NECESSARY val language_name: String, @NECESSARY val language_id: String) {
    override fun toString(): String {
        return language_name
    }
}