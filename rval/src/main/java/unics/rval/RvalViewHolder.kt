package unics.rval

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.leanback.widget.Presenter
import androidx.recyclerview.widget.RecyclerView
import unics.rva.utils.findContainingRecyclerView

open class RvalViewHolder(view: View) : Presenter.ViewHolder(view) {

    private val views: SparseArray<View> = SparseArray()
    private var _data: Any? = null
    private var _attachedRv: RecyclerView? = null
    inline val context: Context get() = view.context

    constructor(parent: ViewGroup, layoutId: Int, attachToRoot: Boolean = false) : this(
        LayoutInflater.from(parent.context),
        parent,
        layoutId, attachToRoot
    )

    constructor(
        inflater: LayoutInflater,
        parent: ViewGroup,
        layoutId: Int,
        attachToRoot: Boolean = false
    ) : this(inflater.inflate(layoutId, parent, attachToRoot))

    /**
     * 绑定依赖
     */
    @CallSuper
    open fun bindModel(data: Any) {
        this._data = data
    }

    @CallSuper
    open fun unbind() {
        this._data = null
    }

    @CallSuper
    fun bindRecyclerView(recyclerView: RecyclerView?) {
        _attachedRv = recyclerView
    }

    @Suppress("UNCHECKED_CAST")
    fun <M> getModel(): M = _data as M

    open fun <T : View> getView(@IdRes viewId: Int): T {
        val view = getViewOrNull<T>(viewId)
        checkNotNull(view) { "No view found with id $viewId" }
        return view
    }

    @Suppress("UNCHECKED_CAST")
    open fun <T : View> getViewOrNull(@IdRes viewId: Int): T? {
        val view = views.get(viewId)
        if (view == null) {
            this.view.findViewById<T>(viewId)?.let {
                views.put(viewId, it)
                return it
            }
        }
        return view as? T
    }

    fun setText(@IdRes viewId: Int, value: CharSequence?): RvalViewHolder {
        getView<TextView>(viewId).text = value
        return this
    }

    fun setTextOrGone(@IdRes id: Int, message: CharSequence?) {
        getViewOrNull<TextView>(id)?.apply {
            visibility = if (message.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            text = message
        }
    }

    fun setVisible(vid: Int, visible: Boolean) {
        getViewOrNull<View>(vid)?.let {
            if (visible) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.GONE
            }
        }
    }

    fun setVisibleOrNot(vid: Int, visible: Boolean) {
        getViewOrNull<View>(vid)?.let {
            if (visible) {
                it.visibility = View.VISIBLE
            } else {
                it.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 不安全的获取位置方法
     * 如果未调用[bindRecyclerView]绑定rv，则会调用[View.findContainingRecyclerView]获取当前view所在的rv
     * （但该方法并不可靠，在itemView未绑定到rv上时，是无法获取rv的），再根据rv获取adapter判断类型查找到对应数据所在的位置
     * 这种逻辑在很多情况下并不有效，但相对稳定的方法（只怪[androidx.leanback.widget.ItemBridgeAdapter]将bindViewHolder设置为final，并且未对外暴漏position）
     */
    fun getAdapterPositionUnsafe(): Int {
        val model = _data ?: return RecyclerView.NO_POSITION
        val adapter = (_attachedRv ?: view.findContainingRecyclerView())?.adapter
            ?: return RecyclerView.NO_POSITION
        if (adapter is RvalItemBridgeAdapter) {
            return adapter.indexOf(model)
        }
        return RecyclerView.NO_POSITION
    }

}