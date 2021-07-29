package com.sakurawald.silicon.data.beans.response

/**
 * TraceResponse用于追踪某次Submit的结果. 如果无法成功追踪, 则SubmitResponse应为null.
 */
open class TraceResponse(var traceSubmitResponse: SubmitResponse?) : ResponseBean()