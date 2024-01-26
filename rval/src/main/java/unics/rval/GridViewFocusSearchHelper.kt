
//note:包名不能改，否则无法访问执行包内访问操作
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

internal abstract class GridViewFocusSearchHelper<T : BaseGridView>(val gridView: T) {

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
        onLayoutManagerChanged()
    }

    internal fun onLayoutManagerChanged() {
        try {
            val layoutManager = gridView.layoutManager
            if (layoutManager == null || layoutManager !is GridLayoutManager
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

    protected val itemCount: Int get() = gridView.adapter?.itemCount ?: 0

    protected abstract fun getNextFocusAdapterPosition(
        currentAdapterPosition: Int,
        direction: Int
    ): Int

    fun onRtlPropertiesChanged(layoutDirection: Int) {
        isRtl = layoutDirection == View.LAYOUT_DIRECTION_RTL
    }

    /**
     * 寻焦优化
     * @param focused 当前获取焦点的view
     * @param direction 寻焦方向
     * @param next 调用[gridView#focusSearch]查询到的结果
     */
    fun optFocusSearch(focused: View?, direction: Int, next: View?): View? {
        if (gridView.isFocusSearchDisabled || !isEnable) {
            logd { "optFocusSearch disabled,return next($next).(isFocusSearchDisabled=${gridView.isFocusSearchDisabled} isEnable=$isEnable)" }
            return next
        }

        if (next == null || next == focused || next == gridView) {
            logd { "optFocusSearch next == null || next == focused || next == gridView,performFocusSearch" }
            return performFocusSearch(focused, direction, next)
        }


        if (gridView is VerticalGridView) {
            if (((direction == View.FOCUS_UP && focusOutFront)
                        || (direction == View.FOCUS_DOWN && focusOutEnd)
                        || (direction == View.FOCUS_LEFT && focusOutSideStart)
                        || (direction == View.FOCUS_RIGHT && focusOutSideEnd)) && gridView.findContainingViewHolder(
                    next
                ) == null
            ) {
                return performFocusSearch(focused, direction, next)
            }
        } else {
            if (((direction == View.FOCUS_UP && focusOutSideStart)
                        || (direction == View.FOCUS_DOWN && focusOutSideEnd)
                        || (direction == View.FOCUS_LEFT && focusOutFront)
                        || (direction == View.FOCUS_RIGHT && focusOutEnd)) && gridView.findContainingViewHolder(
                    next
                ) == null
            ) {
                return performFocusSearch(focused, direction, next)
            }
        }
        return next
    }


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
     * @param superNext Group预先找到的下一个焦点
     */
    private fun performFocusSearch(focused: View?, direction: Int, superNext: View?): View? {
        if (focused == null) {
            return superNext ?: findFirstFocusableChild()
        }
        val itemView = gridView.findContainingItemView(focused)
        if (itemView == null) {
            logd { "[focusSearch] findContainingItemView == null，return next." }
            return superNext
        }
        val focusedAdapterPosition = gridView.getChildAdapterPosition(itemView)
        if (focusedAdapterPosition == NO_POSITION) {
            //no position 直接返回
            logd { "[focusSearch] getChildAdapterPosition(itemView) == NO_POSITION，return next." }
            return superNext
        }

        logd { "[focusSearch] getChildAdapterPosition(itemView) ==${focusedAdapterPosition},continue)" }
        val nextFocusAdapterPosition =
            getNextFocusAdapterPosition(focusedAdapterPosition, direction)
        if (nextFocusAdapterPosition == focusedAdapterPosition || nextFocusAdapterPosition == NO_POSITION) {
            logd { "[focusSearch] :nextFocusAdapterPosition == ${nextFocusAdapterPosition}, focusedAdapterPosition=$focusedAdapterPosition interrupt )" }
            return superNext
        }

        logd { "[focusSearch] :nextFocusAdapterPosition ==${nextFocusAdapterPosition},continue)" }
        val nextChildViewHolder =
            gridView.findViewHolderForAdapterPosition(nextFocusAdapterPosition)
        return if (nextChildViewHolder == null) {
            logd { "[focusSearch] :nextChildViewHolder == null,interrupt )" }
            superNext
        } else {
            nextChildViewHolder.itemView
        }
    }

    /**
     * @param superNext 调用super
     */
    @Deprecated("方法没有意义，里面的判断很return null的情况外层实际已经处理了", replaceWith = ReplaceWith("optFocusSearch(focused,direction,superNext)"))
    fun focusSearch(focused: View?, direction: Int, superNext: View?): View? {
        if (gridView.isFocusSearchDisabled)
            return superNext

        if (gridView is VerticalGridView) {
            //开始侧可以焦点移除，不做处理
            if (focusOutSideStart && ((!isRtl && direction == View.FOCUS_LEFT) || (isRtl && direction == View.FOCUS_RIGHT))) {
                return null
            }
            //末侧边可以焦点移出，则不用做寻焦处理
            if (focusOutSideEnd && ((!isRtl && direction == View.FOCUS_RIGHT) || (isRtl && direction == View.FOCUS_LEFT))) {
                return null
            }
        }
        if (gridView is HorizontalGridView) {
            //开始侧可以焦点移除，不做处理
            if (focusOutSideStart && direction == View.FOCUS_UP) {
                return null
            }
            //末侧边可以焦点移出，则不用做寻焦处理
            if (focusOutSideEnd && direction == View.FOCUS_DOWN) {
                return null
            }
        }

        //在gridView中查找下一个可获取焦点的view
        val next = FocusFinder.getInstance().findNextFocus(gridView, focused, direction)
        return if (next == null || next == gridView || next == focused) {
            logd { "[focusSearch] FocusFinder find null next, invokeFocusSearchOpt. (focused=${focused})" }
            performFocusSearch(focused, direction, superNext)
        } else {
            next
        }
    }

    private inline fun logd(msg: () -> String) {
        Log.d(TAG, msg())
    }

    companion object {

        private const val TAG = "GVFocusSearchHelper"

        internal const val NO_POSITION = BaseGridView.NO_POSITION

        internal const val HORIZONTAL = BaseGridView.HORIZONTAL

        internal const val VERTICAL = BaseGridView.VERTICAL

        inline fun <T : BaseGridView> new(gridView: BaseGridView): GridViewFocusSearchHelper<*> {
            return if (gridView is HorizontalGridView) {
                HorizontalGridViewFocusSearchHelper(gridView)
            } else if (gridView is VerticalGridView) {
                VerticalGridViewFocusSearchHelper(gridView)
            } else {
                throw IllegalStateException("不支持的类型$gridView")
            }
        }
    }
}