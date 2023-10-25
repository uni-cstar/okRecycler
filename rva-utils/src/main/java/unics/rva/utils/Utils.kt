package unics.rva.utils

import android.annotation.SuppressLint
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Method

/**
 * Create by luochao
 * on 2023/10/25
 */

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
 * 是否可以获取焦点
 */
fun View.canTakeFocusCompat(): Boolean {
    return try {
        canTakeFocusReflection()
    } catch (e: Throwable) {
        this.visibility == View.VISIBLE && this.isFocusable && this.isEnabled
    }
}

private var canTakeFocusMethod: Method? = null

/**
 * 反射调用view是否可以获取焦点
 */
@SuppressLint("SoonBlockedPrivateApi")
@Throws(NoSuchMethodException::class, SecurityException::class)
fun View.canTakeFocusReflection(): Boolean {
    val method = canTakeFocusMethod ?: View::class.java.getDeclaredMethod("canTakeFocus").also {
        it.isAccessible = true
        canTakeFocusMethod = it
    }
    return method.invoke(this) as Boolean
}

inline fun <T> SparseArray<T>.forEach(action: (key: Int, value: T) -> Unit) {
    for (index in 0 until size()) {
        action(keyAt(index), valueAt(index))
    }
}