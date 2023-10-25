package androidx.leanback.widget

import android.graphics.Rect
import android.util.Log
import android.view.FocusFinder
import android.view.View
import unics.rva.utils.canTakeFocusCompat

/**
 * Created by Lucio on 2021/11/28.
 * [BaseGridView]焦点查询帮助类
 *
 * @param gridView
 * @param callback 查询回调，用于判断[BaseGridView]是否已找到下一个焦点位置
 */

abstract class GridViewFocusSearchHelper<T : BaseGridView>(val gridView: T) {

    protected var isRtl: Boolean
    protected var focusOutFront: Boolean = false
    protected var focusOutEnd: Boolean = false

    //是否允许焦点转移
    protected var focusOutSideStart: Boolean = true
    protected var focusOutSideEnd: Boolean = true
    private val focusedRect = Rect()
    private var isEnable: Boolean = true
    protected val orientation: Int

    init {
        isRtl =
            gridView.context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
        orientation = if (gridView is HorizontalGridView) HORIZONTAL else VERTICAL
        try {
            val layoutManager = gridView.layoutManager
            if (layoutManager == null
                || layoutManager !is GridLayoutManager
            ) {
                isEnable = false
            } else {
                isEnable = true
                focusOutFront = layoutManager.mFlag and GridLayoutManager.PF_FOCUS_OUT_FRONT != 0
                focusOutEnd = layoutManager.mFlag and GridLayoutManager.PF_FOCUS_OUT_END != 0
                focusOutSideStart =
                    layoutManager.mFlag and GridLayoutManager.PF_FOCUS_OUT_SIDE_START != 0
                focusOutSideEnd =
                    layoutManager.mFlag and GridLayoutManager.PF_FOCUS_OUT_SIDE_END != 0
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    internal fun getGridLayoutManager(): GridLayoutManager? {
        val layoutManager = gridView.layoutManager
        if (layoutManager == null
            || layoutManager !is GridLayoutManager
        ) {
            return null
        }
        return layoutManager
    }

    protected abstract fun getNextFocusAdapterPosition(
        currentAdapterPosition: Int,
        direction: Int
    ): Int

    fun onRtlPropertiesChanged(layoutDirection: Int) {
        isRtl = layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    protected val itemCount: Int get() = gridView.adapter?.itemCount ?: 0

    private fun findFirstFocusableChild(): View? {
        for (i in 0 until gridView.childCount) {
            val child = gridView.getChildAt(i)
            if (child.canTakeFocusCompat())
                return child
        }
        return null
    }

    /**
     * 执行寻焦优化
     */
    private fun invokeFocusSearchOpt(focused: View?, direction: Int): View? {
        if (focused == null) {
            return findFirstFocusableChild()
        }
        val itemView = gridView.findContainingItemView(focused)
        if (itemView == null) {
            logd("[focusSearch] findContainingItemView == null，return next.")
            return null
        }
        val focusedAdapterPosition = gridView.getChildAdapterPosition(itemView)
        if (focusedAdapterPosition == NO_POSITION) {
            //no position 直接返回
            logd("[focusSearch] getChildAdapterPosition(itemView) == NO_POSITION，return next.")
            return null
        }

        logd("[focusSearch] getChildAdapterPosition(itemView) ==${focusedAdapterPosition},continue)")
        val nextFocusAdapterPosition =
            getNextFocusAdapterPosition(focusedAdapterPosition, direction)
        if (nextFocusAdapterPosition == focusedAdapterPosition || nextFocusAdapterPosition == NO_POSITION) {
            logd("[focusSearch] :nextFocusAdapterPosition == ${nextFocusAdapterPosition}, focusedAdapterPosition=$focusedAdapterPosition interrupt )")
            return null
        }

        logd("[focusSearch] :nextFocusAdapterPosition ==${nextFocusAdapterPosition},continue)")
        val nextChildViewHolder =
            gridView.findViewHolderForAdapterPosition(nextFocusAdapterPosition)
        return if (nextChildViewHolder == null) {
            logd("[focusSearch] :nextChildViewHolder == null,interrupt )")
            null
        } else {
            nextChildViewHolder.itemView
        }
    }

    fun focusSearch(focused: View?, direction: Int): View? {
        if (gridView.isFocusSearchDisabled)
            return null
        //在gridView中查找下一个可获取焦点的view
        val next = FocusFinder.getInstance().findNextFocus(gridView, focused, direction)
        return if (next == null || next == gridView || next == focused) {
            logd("[focusSearch] FocusFinder find null next, invokeFocusSearchOpt. (focused=${focused})")
            invokeFocusSearchOpt(focused, direction)
        } else {
            next
        }
    }

    private inline fun logd(msg: String) {
        Log.d(TAG, msg)
    }

    companion object {

        private const val TAG = "GVFocusSearchHelper"

        internal const val NO_POSITION = BaseGridView.NO_POSITION

        internal const val HORIZONTAL = BaseGridView.HORIZONTAL

        internal const val VERTICAL = BaseGridView.VERTICAL

    }
}