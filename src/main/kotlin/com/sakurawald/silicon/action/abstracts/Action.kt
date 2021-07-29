package com.sakurawald.silicon.action.abstracts

import com.sakurawald.silicon.data.beans.request.RequestBean
import com.sakurawald.silicon.data.beans.response.ResponseBean

/**
 * RequestBean和ResponseBean均不允许传递null值, 而应当使用空对象来代替.
 */
abstract class Action<ReqBean : RequestBean, ResBean : ResponseBean> {
    abstract fun execute(requestBean: ReqBean): ResBean
}