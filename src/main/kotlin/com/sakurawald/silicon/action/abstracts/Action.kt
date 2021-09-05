package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.RequestBean
import com.sakurawald.silicon.data.beans.response.ResponseBean

abstract class Action<ReqBean : RequestBean, ResBean : ResponseBean> {
    abstract fun execute(requestBean: ReqBean): ResBean
}