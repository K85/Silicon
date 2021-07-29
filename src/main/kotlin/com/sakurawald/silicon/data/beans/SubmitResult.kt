package com.sakurawald.silicon.data.beans

@Suppress("MemberVisibilityCanBePrivate", "unused")
enum class SubmitResult(val result_cn: String, val result_en: String) {
    ACCEPTED("通过", "Accepted"), TIME_LIMIT_EXCEED("时间超限", "Time Limit Exceed"), MEMORY_LIMIT_EXCEED(
        "空间超限",
        "Memory Limit Exceed"
    ),
    WRONG_ANSWER("错误答案", "Wrong Answer"), RUNTIME_ERROR("运行时错误", "Runtime Error"), COMPILE_ERROR(
        "编译时错误",
        "Compile Error"
    ),
    PRESENTATION_ERROR("输出格式错误", "Presentation Error"), OUTPUT_LIMIT_EXCEED(
        "输出超限",
        "Output Limit Exceed"
    ),
    WAITING("等待中", "Waiting"), UNKNOWN("未知", "Unknown");

    fun same(submitResult: String): Boolean {
        return result_en == submitResult
    }

    override fun toString(): String {
        return result_en
    }
}