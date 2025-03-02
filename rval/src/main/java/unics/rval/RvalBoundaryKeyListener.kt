package unics.rval

import android.view.View

/**
 * Create by luochao
 * on 2023/10/30
 * 边界按键监听
 */
fun interface RvalBoundaryKeyListener {
    /**
     * @param direction One of View.FOCUS_UP, View.FOCUS_DOWN, View.FOCUS_LEFT, View.FOCUS_RIGHT, View.FOCUS_FORWARD, View.FOCUS_BACKWARD or 0 for not applicable.
     * @return true if handled,false otherwise
     */
    fun onBoundary(itemView: View?,@FocusDirection direction: Int): Boolean
}