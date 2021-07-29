package com.sakurawald.silicon.data.beans.request

import com.sakurawald.silicon.data.beans.Account

/**
 * @param requestAccount 发起状态请求的账号
 * @param problemID 用于过滤的problemID, 为null代表不过滤.
 * @param userID 用于过滤的userID, 为null代表不过滤.
 * @param page 在支持跳页查询的系统中, page参数直接表示页码数. 在不支持跳页查询的系统中, page参数表示操作类型.
 */
open class StatusRequest(var requestAccount: Account?, var problemID: String?, var userID: String?, var page: String?) :
    RequestBean()