package unics.rva.utils

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * Create by luochao
 * on 2023/10/25
 * RV相关的工具函数
 */

const val NO_POSITION = RecyclerView.NO_POSITION

/**
 * 设置是否使用Item改变动画：也就是调用Adapter#notifyXX之后刷新列表，是否执行动画（默认是闪一下）
 */
fun RecyclerView.setChangeAnimationsEnabled(isEnable: Boolean) {
    (itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = isEnable
}

/**
 * 查找包含该view得RecyclerView，通常用于ViewHolder中view查找所属的RecyclerView
 */
fun View.findContainingRecyclerView(): RecyclerView? {
    var view = this
    var parent = view.parent
    while (parent != null && parent is View && parent !is RecyclerView) {
        view = parent
        parent = view.parent
    }
    return parent as? RecyclerView
}

/**
 * 查找该view在recyclerview中所属的ViewHolder
 */
fun View.findContainingViewHolder(): RecyclerView.ViewHolder? {
    val recyclerView = findContainingRecyclerView() ?: return null
    return recyclerView.findContainingViewHolder(this)
}

/**
 * 查找该view在recyclerview所持有的child中的位置
 */
fun RecyclerView.findImmediateChildIndex(view: View?): Int {
    if (view == null || view == this)
        return NO_POSITION

    val itemView = this.findContainingItemView(view)
    if (itemView != null) {
        var i = 0
        val count: Int = this.childCount
        while (i < count) {
            if (this.getChildAt(i) == view) {
                return i
            }
            i++
        }
    }
    return NO_POSITION
}

/**
 * 是否正在滑动中
 */
fun RecyclerView.isScrolling(): Boolean {
    return scrollState != RecyclerView.SCROLL_STATE_IDLE
}

inline fun <T> SparseArray<T>.forEach(action: (key: Int, value: T) -> Unit) {
    for (index in 0 until size()) {
        action(keyAt(index), valueAt(index))
    }
}