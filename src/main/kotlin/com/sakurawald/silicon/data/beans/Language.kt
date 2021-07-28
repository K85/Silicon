package com.sakurawald.silicon.data.beans

import com.sakurawald.silicon.annotation.NECESSARY

class Language(@field:NECESSARY val language_name: String, @field:NECESSARY val language_id: String) {

    override fun toString(): String {
        return language_name
    }
}