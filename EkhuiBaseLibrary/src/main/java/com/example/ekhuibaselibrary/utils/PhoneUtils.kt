    package com.example.ekhuibaselibrary.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object PhoneUtils {
    fun isMobile(str: String?): Boolean {


        var p: Pattern? = null
        var m: Matcher? = null
        var b = false
        p = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$") // 验证手机号
        m = p.matcher(str)
        b = m.matches()
        return b
    }

}