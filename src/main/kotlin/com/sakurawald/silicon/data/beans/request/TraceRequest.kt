package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.annotation.AUTO
import com.sakurawald.silicon.annotation.NECESSARY

class TraceRequest @AUTO constructor(@field:NECESSARY var submitRequest: SubmitRequest) : RequestBean() {
    override fun toString(): String {
        return "TraceRequest{" +
                "submitRequest=" + submitRequest +
                '}'
    }
}