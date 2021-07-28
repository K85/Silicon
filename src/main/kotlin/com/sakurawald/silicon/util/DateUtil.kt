package com.sakurawald.silicon.util

import com.sakurawald.silicon.debug.LoggerManager
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    /**
     * @return 返回本地当前时间的详细文本, 并且该文本是符合Windows文件命名规则的.
     */
    val currentDateDetailString: String
        get() {
            var result = getDateDetail(Calendar.getInstance())
            result = result.replace(":", "-")
            return result
        }

    /**
     * 将<某个时间>与<当前时间>比较
     *
     * @return 输入的日期在现在之后，则返回1. 输入的日期在现在之前，则返回-1. 若输入日期与现在一样，则返回0.
    </当前时间></某个时间> */
    fun compareDate(date: Calendar): Int {
        return compareDate(date, Calendar.getInstance())
    }

    fun compareDate(date1: Calendar, date2: Calendar): Int {
        val date_clone = date1.clone() as Calendar
        val date2_clone = date2.clone() as Calendar
        setZero(date_clone)
        setZero(date2_clone)
        return date_clone.compareTo(date2_clone)
    }

    /**
     * 获取两个Date的时间差.
     *
     * @param date1 小时间
     * @param date2 大时间
     * @return 时间差
     */
    fun differentDaysByMillisecond(date1: Date, date2: Date): Int {
        return ((date2.time - date1.time) / (1000 * 3600 * 24)).toInt()
    }

    /**
     * @return 计算<两个日期>相差多少分钟.
    </两个日期> */
    fun diffMinutes(big: Calendar, small: Calendar): Long {
        val nm = (1000 * 60).toLong() // 每分钟毫秒数
        val diff = big.time.time - small.time.time // 获得两个时间的毫秒时间差异
        return diff / nm
    }

    /**
     * @return 计算两个日期相差多少秒.
     */
    fun diffSeconds(big: Calendar, small: Calendar): Long {
        val nm: Long = 1000
        val diff = big.time.time - small.time.time // 获得两个时间的毫秒时间差异
        return diff / nm
    }

    fun getDataDetail(date: String?): Calendar {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d: Date? = null
        try {
            d = format.parse(date)
        } catch (e: ParseException) {
            LoggerManager.reportException(e)
        }
        val c = Calendar.getInstance()
        c.time = d
        return c
    }

    fun getDataSimple(date: String?): Calendar {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        var d: Date? = null
        try {
            d = format.parse(date)
        } catch (e: ParseException) {
            LoggerManager.reportException(e)
        }
        val c = Calendar.getInstance()
        c.time = d
        return c
    }

    fun getDateDetail(c: Calendar): String {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return format.format(c.time)
    }

    fun getDateSimple(c: Calendar): String {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return format.format(c.time)
    }

    val nowDate: Date
        get() = Calendar.getInstance().time
    val nowDay: Int
        get() = Calendar.getInstance()[Calendar.DAY_OF_MONTH]
    val nowDayOfWeek: Int
        get() = Calendar.getInstance()[Calendar.DAY_OF_WEEK] - 1
    val nowHour: Int
        get() = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
    val nowMinute: Int
        get() = Calendar.getInstance()[Calendar.MINUTE]
    val nowMonth: Int
        get() = Calendar.getInstance()[Calendar.MONTH] + 1
    val nowSecond: Int
        get() = Calendar.getInstance()[Calendar.SECOND]
    val nowYear: Int
        get() = Calendar.getInstance()[Calendar.YEAR]
    val unixTime_Ms: Long
        get() = System.currentTimeMillis()
    val unixTime_S: Long
        get() = System.currentTimeMillis() / 1000

    /**
     * 将传入的Calendar的<小时>和<分钟>设定为0.
    </分钟></小时> */
    fun setZero(c: Calendar): Calendar {
        c[Calendar.HOUR_OF_DAY] = 0
        c[Calendar.MINUTE] = 0
        return c
    }

    fun translate_Date_To_Calendar(date: Date?): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    fun translate_TimeStamp_Ms_To_Calendar(timestamp_Ms: Long): Calendar {
        return translate_Date_To_Calendar(translate_TimeStamp_Ms_To_Date(timestamp_Ms))
    }

    fun translate_TimeStamp_Ms_To_Date(timestamp_Ms: Long): Date {
        return Date(timestamp_Ms)
    }
}