package com.ekhuilibrary.utils

import java.lang.reflect.Method
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Ekhui on 2020/7/3.
 */

class ObjectUtils(
    private val obj: Any
) {
    private var cls: Class<*>? = null

    private var getMethods: Hashtable<String, Method>? = null
    private var setMethods: Hashtable<String, Method>? = null


    private fun initMethods() {
        getMethods = Hashtable()
        setMethods = Hashtable()
        cls = obj.javaClass
        val methods = cls?.methods
        // 定义正则表达式，从方法中过滤出getter / setter 函数.
        val gs = "get(\\w+)"
        val getM = Pattern.compile(gs)
        val ss = "set(\\w+)"
        val setM = Pattern.compile(ss)
        // 把方法中的"set" 或者 "get" 去掉,$1匹配第一个
        val s1 = "$1"
        var param: String
        for (i in methods?.indices!!) {
            val m = methods[i]
            val methodName = m.name
            when {
                Pattern.matches(gs, methodName) -> {
                    param =
                        getM.matcher(methodName).replaceAll(s1).toLowerCase(Locale.getDefault())
                    getMethods!![param] = m
                }
                Pattern.matches(ss, methodName) -> {
                    param =
                        setM.matcher(methodName).replaceAll(s1).toLowerCase(Locale.getDefault())
                    setMethods!![param] = m
                }
                else -> {

                }
            }
        }
    }

    fun setMethodValue(property: String, bean: Any?): Boolean {
        val m = setMethods!![property.toLowerCase(Locale.getDefault())]
        return if (m != null) {
            try {
// 调用目标类的setter函数
                m.invoke(obj, bean)
                true
            } catch (ex: Exception) {
                ex.printStackTrace()
                false
            }
        } else false
    }

    init {
        initMethods()
    }
}