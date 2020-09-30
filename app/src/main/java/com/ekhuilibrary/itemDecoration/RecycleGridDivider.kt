package com.ekhuilibrary.itemDecoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

 class RecycleGridDivider @JvmOverloads constructor(private val space: Int = 1, private val color: Int = Color.TRANSPARENT) : ItemDecoration() {
    private fun initPaint() {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = color
            style = Paint.Style.FILL
            strokeWidth = space.toFloat()
        }

    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager as GridLayoutManager?
        val childSize = parent.childCount
        val span = manager!!.spanCount
        //为了Item大小均匀，将设定分割线平均分给左右两边Item各一半
        val offset = space / 2
        //得到View的位置
        val childPosition = parent.getChildAdapterPosition(view)
        //第一排，顶部不画
//        if (childPosition  < span) {
//最左边的，左边不画
        when {
            childPosition % span == 0 -> {
                outRect[space, space, space / 2] = 0
                //最右边，右边不画
            }
            childPosition % span == span - 1 -> {
                outRect[space / 2, space, space] = 0
            }
            else -> {
                outRect[offset, 0, offset] = 0
            }
        }
        //        }
//        else {
//            //上下的分割线，就从第二排开始，每个区域的顶部直接添加设定大小，不用再均分了
//            if (childPosition  % span == 0) {
//                outRect.set(0, space, offset, 0);
//            } else if (childPosition  % span == span - 1) {
//                outRect.set(offset, space, 0, 0);
//            } else {
//                outRect.set(offset, space, offset, 0);
//            }
//        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    init {
        initPaint()
    }
}