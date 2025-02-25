package unics.rval


import android.annotation.SuppressLint
import androidx.annotation.LayoutRes
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import unics.rval.presenter.RvalPresenter

/**
 * 初始化
 * @see RvalItemBridgeAdapter.addClassPresenter
 */
fun RecyclerView.rval(setup: RvalItemBridgeAdapter.() -> Unit): RvalItemBridgeAdapter {
    val adapter = RvalItemBridgeAdapter()
    if (this !is BaseGridView && layoutManager == null) {
        this.layoutManager = LinearLayoutManager(this.context)
    }
    setup.invoke(adapter)
    this.adapter = adapter
    return adapter
}

/**
 * 单Presenter初始化
 * @param presenter 指定的presenter
 */
fun RecyclerView.rvalSingle(presenter: Presenter): RvalItemBridgeAdapter {
    val adapter = RvalItemBridgeAdapter(presenter)
    if (this !is BaseGridView && layoutManager == null) {
        this.layoutManager = LinearLayoutManager(this.context)
    }
    this.adapter = adapter
    return adapter
}

/**
 * 单Presenter初始化
 * @param setup presenter初始化代码块
 */
inline fun RecyclerView.rvalSingle(setup: RvalPresenter.() -> Unit): RvalItemBridgeAdapter {
    return this.rvalSingle(RvalPresenter().apply(setup))
}

/**
 * 单Presenter初始化
 * @param itemLayoutRes presenter使用的item布局资源id
 * @param setup presenter初始化代码块
 */
inline fun RecyclerView.rvalSingle(
    @LayoutRes itemLayoutRes: Int, setup: RvalPresenter.() -> Unit
): RvalItemBridgeAdapter {
    return this.rvalSingle(RvalPresenter(itemLayoutRes).apply(setup))
}

/**
 * 单Presenter初始化
 * @param presenterSelector 自定义[RvalItemBridgeAdapter]使用的[PresenterSelector]
 */
fun RecyclerView.rvalSingle(presenterSelector: PresenterSelector): RvalItemBridgeAdapter {
    val adapter = RvalItemBridgeAdapter(presenterSelector)
    if (this !is BaseGridView)
        this.layoutManager = LinearLayoutManager(this.context)
    this.adapter = adapter
    return adapter
}

/**
 * 获取绑定的适配器
 */
val RecyclerView.rval: RvalItemBridgeAdapter get() = this.adapter as RvalItemBridgeAdapter

/**
 * 使用居中对齐（选中的item在中间）
 */
@SuppressLint("RestrictedApi")
fun BaseGridView.applyCenterAlignment() {
    focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ALIGNED
    windowAlignment = BaseGridView.WINDOW_ALIGN_NO_EDGE
}