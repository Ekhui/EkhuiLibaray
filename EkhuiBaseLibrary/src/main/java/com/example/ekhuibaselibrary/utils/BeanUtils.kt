package com.example.ekhuibaselibrary.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by Ekhui on 2019/11/21.
 */
object BeanUtils {

    /**
     * Created by Ekhui on 2020/4/22.
     * 作用：解析接口返回数据含有map
     */

    inline fun <reified T> fetchResponseMap(dataList: List<*>): MutableList<T> {
        val newList: MutableList<T> = ArrayList()
        dataList.forEach {
            val gson: Gson = GsonBuilder().enableComplexMapKeySerialization().create()
            val jsonString = gson.toJson(it)
            val bean: T =
                gson.fromJson(jsonString, T::class.java)
            newList.add(bean)
        }
        return newList
    }

    /**
     * Created by Ekhui on 2020/4/25.
     * 作用：字符串转数组 符号隔开
     */
    fun <T> joinToString(
        collection: Collection<T>,
        separator: String = ","
    ): String {
        val sb = StringBuffer()

        for ((index, element) in collection.withIndex()) {
            if (index > 0) {
                sb.append(separator)
            }
            sb.append(element)
        }

        return sb.toString()
    }

    fun hideKeyBoard(view: View, context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    fun toFixFloat(number: Float): Float {
        return ((number * 1000000).roundToInt()) / 1000000.toFloat()
    }


    //    判断经纬度 小数点前三位，小数点后6位
    fun judgeLnglat(data: Float): Boolean {
        val index = data.toString().indexOf(".")
        if (index != -1) {
            if (data.toString().substring(0, index).length > 3)
                return false
            if (data.toString().substring(index + 1, data.toString().length).length > 6)
                return false
            return true
        }
        return true
    }

    //    对象序列化为字符串
    @Throws(Exception::class)
    fun object2Serialize(bean: Any): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val out = ObjectOutputStream(byteArrayOutputStream)
        out.writeObject(bean)
        //必须是ISO-8859-1
        val objectStr = byteArrayOutputStream.toString("ISO-8859-1")
        out.close()
        byteArrayOutputStream.close()
        return objectStr
    }

    //    字符串序列化为对象
    @Throws(Exception::class)
    fun serialize2Object(objectStr: String): Any {
        val byteArrayInputStream =
            ByteArrayInputStream(objectStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val bean = objectInputStream.readObject()
        objectInputStream.close()
        byteArrayInputStream.close()
        return bean
    }

    //    字符串转数组
    fun stringToList(string: String): List<Any> {
        var listText = string
        if (listText == "") {
            return emptyList()
        }
        listText = listText.substring(1)
        val list: MutableList<Any> = ArrayList()
        val text: Array<String> = listText.split("#").toTypedArray()
        for (str in text) {
            if (str[0] == 'L') {
                val lists = stringToList(str)
                list.add(lists)
            } else {
                list.add(str)
            }
        }
        return list
    }
}



