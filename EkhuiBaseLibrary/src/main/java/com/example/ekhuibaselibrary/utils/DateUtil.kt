package com.example.ekhuibaselibrary.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ekhui on 2019/11/22.
 */
object DateUtil {

    //    @TypeConverter
    @JvmStatic
    fun long2Date(value: Long): Date {
        return Date(value)
    }


    //    @TypeConverter
    @JvmStatic
    fun date2Lone(value: Date): Long {
        return value.time
    }

    /*
        * Created by Ekhui on 2019/11/22.
        * 作用：日期字符串转换Date实体
        * 返回值：Date
        * 参数：serverTime - string 时间字符串
        *       format - 日期格式
        */
    fun changeStr2Date(date: String?): Date {
//        val format1 = SimpleDateFormat("MM-dd HH:mm")
        if (date.isNullOrBlank())
            return Date()
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(date)!!
    }

    /*
    * Created by Ekhui on 2019/11/22.
    * 作用：Date对象获取时间字符串
    * 返回值：String
    * 参数：date - Date date对象
    *       format - 日期格式
    */
    fun changeDate2Str(date: Date): String? {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }

    fun changeDate2DayStr(date: Date): String? {
        return SimpleDateFormat("HH:mm:ss").format(date)
    }

//    fun changeData2Str(date: Date): String? {
//        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
//    }

    /*
    * Created by Ekhui on 2019/11/22.
    * 作用：当前日期往前月份
    * 参数：delay- int 操作的月份
    */
    fun minDay(delay: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MONTH, -delay)
        return calendar.time
    }

    /*
    * Created by Ekhui on 2019/11/22.
    * 作用：根据date获取年份
    * 返回: int 年份
    */
    fun dateGetYear(startTime: Date): Int {
        return SimpleDateFormat("yyyy").format(startTime).toInt()
    }

    /*
    * Created by Ekhui on 2019/11/22.
    * 作用：根据date获取月份
    * 返回: int 月份
    */
    fun dateGetMonth(startTime: Date): Int {
        return SimpleDateFormat("MM").format(startTime).toInt() - 1
    }

    /*
    * Created by Ekhui on 2019/11/22.
    * 作用：根据date获取日期
    * 返回: int 日期
    */
    fun dateGetDay(startTime: Date): Int {
        return SimpleDateFormat("dd").format(startTime).toInt()
    }

    /*
     * Created by Ekhui on 2019/11/22.
     * 作用：根据date获取小时
     * 返回: int 小时
     */
    fun dateGetHour(startTime: Date): Int {
        return SimpleDateFormat("HH").format(startTime).toInt()
    }

    /*
    * Created by Ekhui on 2019/11/22.
    * 作用：根据date获取分钟
    * 返回: int 分钟
    */
    fun dateGetMin(startTime: Date): Int {
        return SimpleDateFormat("mm").format(startTime).toInt()
    }

    /**
     * Created by Ekhui on 2020/3/17.
     * 作用：将选择器返回的日期和服务器的日期格式统一
     */
    fun formatPickTimeWithServer(monthOfYear: Int, dayOfMonth: Int, year: Int): String {
        var tempMonth = monthOfYear.toString()
        var tempDay = dayOfMonth.toString()
        if (monthOfYear + 1 < 10)
            tempMonth = "0${monthOfYear + 1}"
        if (dayOfMonth < 10)
            tempDay = "0$dayOfMonth"
        return "${year}-${tempMonth}-${tempDay}"
    }

    /**
     * Created by Ekhui on 2020/3/18.
     * 作用：计算2个时间戳
     */
    fun getDiffTime(startTime: Long, endTime: Long, index: String): Int {
        val day: Long
        val hour: Long
        val min: Long
        val sec: Long
        val diff: Long = endTime - startTime

        day = diff / (24 * 60 * 60 * 1000)
        hour = diff / (60 * 60 * 1000) - day * 24
        min = diff / (60 * 1000) - day * 24 * 60 - hour * 60
        sec = diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60

        return when (index) {
            "day" -> day.toInt()
            "hour" -> hour.toInt()
            "min" -> min.toInt()
            "sec" -> sec.toInt()
            else -> 0
        }

    }
}