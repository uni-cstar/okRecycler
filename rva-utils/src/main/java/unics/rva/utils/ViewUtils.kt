/**
 * Create by luochao
 * on 2023/11/13
 */
package unics.rva.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.Method

fun ViewGroup.findContainingItemView(view: View): View? {
    var result = view
    var parent = result.parent
    while (parent != null && parent !== this && parent is View) {
        result = parent
        parent = result.parent
    }
    return if (parent === this) result else null
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