package com.ekhuilibrary.base

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Ekhui on 2020/8/1.
 */
open class BasePageInfoBean() :Parcelable{
    var pageNum: Int? = null
    var pageSize: Int? = null
    var total: Int? = null
    var size: Int? = null
    var startRow: Int? = null
    var endRow: Int? = null
    var pages: Int? = null
    var prePage: Int? = null
    var nextPage: Int? = null
    var hasNextPage: Boolean? = false
    var hasPreviousPage: Boolean? = false
    var isLastPage: Boolean? = false
    var isFirstPage: Boolean? = false

    constructor(parcel: Parcel) : this() {
        pageNum = parcel.readValue(Int::class.java.classLoader) as? Int
        pageSize = parcel.readValue(Int::class.java.classLoader) as? Int
        total = parcel.readValue(Int::class.java.classLoader) as? Int
        size = parcel.readValue(Int::class.java.classLoader) as? Int
        startRow = parcel.readValue(Int::class.java.classLoader) as? Int
        endRow = parcel.readValue(Int::class.java.classLoader) as? Int
        pages = parcel.readValue(Int::class.java.classLoader) as? Int
        prePage = parcel.readValue(Int::class.java.classLoader) as? Int
        nextPage = parcel.readValue(Int::class.java.classLoader) as? Int
        hasNextPage = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        hasPreviousPage = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isLastPage = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        isFirstPage = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(pageNum)
        parcel.writeValue(pageSize)
        parcel.writeValue(total)
        parcel.writeValue(size)
        parcel.writeValue(startRow)
        parcel.writeValue(endRow)
        parcel.writeValue(pages)
        parcel.writeValue(prePage)
        parcel.writeValue(nextPage)
        parcel.writeValue(hasNextPage)
        parcel.writeValue(hasPreviousPage)
        parcel.writeValue(isLastPage)
        parcel.writeValue(isFirstPage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BasePageInfoBean> {
        override fun createFromParcel(parcel: Parcel): BasePageInfoBean {
            return BasePageInfoBean(parcel)
        }

        override fun newArray(size: Int): Array<BasePageInfoBean?> {
            return arrayOfNulls(size)
        }
    }
}