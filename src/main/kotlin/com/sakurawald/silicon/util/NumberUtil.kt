package com.sakurawald.silicon.util

import java.util.*

object NumberUtil {
    /**
     * @return 输入double, 保留指定digit位小数.
     */
    /**
     * @return 输入double, 保留2位小数.
     */
    @JvmOverloads
    fun formatDigit(number: Double, digit: Int = 2): Double {
        return String.format("%." + digit + "f", number).toDouble()
    }

    val bigEnoughNumber: Int
        get() = 1000000

    /**
     * @return 输入小数, 返回百分数文本.
     */
    fun getFormatedPercentage(number: Double): String {
        return formatDigit(number * 100).toString() + "%"
    }

    /**
     * @return 随机获取1~30000的整数.
     */
    val randomNumber: Int
        get() = getRandomNumber(1, 30000)

    fun getRandomNumber(min: Double, max: Double): Int {
        val random = Random()
        return (random.nextInt((max - min + 1).toInt()) + min).toInt()
    }

    fun getRandomNumber(min: Int, max: Int): Int {
        val random = Random()
        return random.nextInt(max - min + 1) + min
    }
}