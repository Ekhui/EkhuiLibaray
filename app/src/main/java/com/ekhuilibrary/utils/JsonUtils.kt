package com.ekhuilibrary.utils

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * Created by Ekhui on 2020/4/17.
 */
 class JsonUtils {
    companion object {

        @JvmStatic
        fun getJsonFile(inputStream: InputStream): String {
            val stringBuilder = StringBuilder()
            val isr = InputStreamReader(inputStream)
            val reader = BufferedReader(isr)
            var jsonLine: String? = ""
            while (reader.readLine().also { jsonLine = it } != null) {
                stringBuilder.append(jsonLine)
            }
            reader.close()
            isr.close()
            inputStream.close()
            return stringBuilder.toString()
        }
    }
}

