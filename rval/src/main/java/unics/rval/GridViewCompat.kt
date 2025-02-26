package androidx.leanback.widget

import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import unics.rval.RvalBoundaryKeyListener
import unics.rval.shakeX
import unics.rval.shakeY

/**
 * Create by luochao
 * on 2023/10/30
 */
internal class GridViewCompat<T : BaseGridView>(
    private val gridView: T,
    private val callback: Callback
) {

    companion object {
        /**
         * 推荐的按键分发时间间隔
         */
        const val PREFERRED_DISPATCH_KEY_EVENT_TIME = 80L
    }

    internal interface Callback {

        fun superFocusSearch(focused: View?, direction: Int): View?

        fun superDispatchKeyEvent(event: KeyEvent): Boolean

    }

    private val TAG = "GridViewCompat"
    private val focusSearchHelper = GridViewFocusSearchHelper.new<T>(gridView)

    /**
     * 是否开启寻焦优化
     *
     */
    var focusSearchOptimization: Boolean = false

    /**
     * 边界按键监听：通常用于实现边界抖动
     */
    var boundaryKeyListener: RvalBoundaryKeyListener? = null

    /**
     * 是否启用边界抖动动画
     */
    var boundaryShakeEnable: Boolean = true

    /**
     * keyevent 分发的最小间隔时间
     */
    var keyEventDispatchMinTime: Long = -1

    private var lastKeyEventDispatchTime: Long = -1

    fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val updateTime = SystemClock.uptimeMillis()
        return if (keyEventDispatchMinTime > 0
            && event.action == KeyEvent.ACTION_DOWN
            && event.repeatCount != 0
            && updateTime - lastKeyEventDispatchTime < keyEventDispatchMinTime
        ) {
            Log.i(
                TAG,
                "dispatchKeyEvent: 忽略keyEvent ${gridView.scrollState == RecyclerView.SCROLL_STATE_IDLE}"
            )
            true
        } else {
            Log.i(
                TAG,
                "dispatchKeyEvent: 触发super ${gridView.scrollState == RecyclerView.SCROLL_STATE_IDLE}"
            )
            lastKeyEventDispatchTime = updateTime
            callback.superDispatchKeyEvent(event)
        }
    }

    fun focusSearch(focused: View?, direction: Int): View? {
        logd { "invoke focusSearch(focused=$focused,direction=$direction)" }
        val next = findNextFocus(focused, direction)
        if (next == null || next == focused) {//todo：解决下一个问题的方案可以考虑采用判断layout是否正在布局或者是否处于滑动中
            if (!handleBoundaryKeyListener(focused, direction) && boundaryShakeEnable) {
                handleBoundaryAnimation(focused, direction)
                logd { "执行shake动画 next=$next focused=$focused" }
            }
        }
        return next
    }

    private fun handleBoundaryAnimation(focused: View?, direction: Int) {
        //todo 这里逻辑有点问题，一直按住按键，列表滚动实际没到边界的时候也会存在item做shake动画
        focused ?: return
        if (direction == View.FOCUS_LEFT || direction == View.FOCUS_RIGHT) {
            focused.shakeX()
        } else {
            focused.shakeY()
        }
    }

    private fun findNextFocus(focused: View?, direction: Int): View? {
        val next = callback.superFocusSearch(focused, direction)
        if (!focusSearchOptimization || gridView.isFocusSearchDisabled) {
            logd { "focus search disabled ,return super find result =$next" }
            return next
        }
        return focusSearchHelper.optFocusSearch(focused, direction, next)
    }

    /**
     * @param direction  One of View.FOCUS_UP, View.FOCUS_DOWN, View.FOCUS_LEFT, View.FOCUS_RIGHT, View.FOCUS_FORWARD, View.FOCUS_BACKWARD or 0 for not applicable.
     */
    private fun handleBoundaryKeyListener(focused: View?, direction: Int): Boolean {
        focused ?: return false
        return boundaryKeyListener?.onBoundary(gridView.findContainingItemView(focused), direction)
            ?: false
    }

    private inline fun logd(msg: () -> String) {
        Log.d(TAG, msg.invoke())
    }
}