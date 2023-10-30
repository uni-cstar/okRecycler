package unics.rval

import android.util.SparseArray
import android.view.FocusFinder
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.GridViewFocusSearchHelper
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.HorizontalGridViewFocusSearchHelper
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.VerticalGridView
import androidx.leanback.widget.VerticalGridViewFocusSearchHelper
import androidx.recyclerview.widget.RecyclerView
import unics.rva.utils.EventHolder
import unics.rva.utils.canTakeFocusCompat
import unics.rva.utils.findContainingRecyclerView
import unics.rva.utils.forEach
import unics.rva.utils.throttleClick

open class RvalPresenter() : Presenter() {

    constructor(@LayoutRes layoutId: Int) : this() {
        itemCreatorByLayoutId = { parent ->
            RvalViewHolder(parent, layoutId = layoutId)
        }
    }

    constructor(@LayoutRes layoutId: Int, binder: LrvaViewHolderCallback) : this(layoutId) {
        itemBind(binder)
    }

    @Deprecated("", replaceWith = ReplaceWith("RvalPresenter().apply(setup)"))
    constructor(setup: RvalPresenter.() -> Unit) : this() {
        apply(setup)
    }

    private var itemCreatorByLayoutId: ((parent: ViewGroup) -> RvalViewHolder)? = null
    private var itemCreator: ((parent: ViewGroup) -> RvalViewHolder)? = null
    private var itemCreated: LrvaViewHolderCallback? = null
    private var itemBinder: LrvaViewHolderCallback? = null
    private var itemPayloadsBinder: ItemPayloadsBindCallback? = null
    private var itemUnbinder: LrvaViewHolderCallback? = null
    private var itemClicker: EventHolder<OnViewClick>? = null
    private var itemKeyListener: OnViewKeyEvent? = null
//    private var itemKeyBoundaryListener: OnViewKeyEvent? = null
    private val itemChildClickersLazy = lazy {
        SparseArray<EventHolder<OnViewClick>>(5)
    }
    private val itemChildClickers: SparseArray<EventHolder<OnViewClick>> by itemChildClickersLazy
    private var itemFocusChanger: OnViewFocusChange? = null
    private var attachedRecyclerView: RecyclerView? = null
//
//    /**
//     * 是否开启寻焦优化
//     *
//     */
//    var focusSearchOptimization: Boolean = false

    /**
     * 默认获取焦点的item位置（只在第一次显示该item时请求焦点）
     */
    var defaultFocusedAdapterPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup): RvalViewHolder {
        val creator = itemCreator ?: itemCreatorByLayoutId
        require(creator != null) {
            "please invoke itemCreate method or provide item layout id by constructor to create item view."
        }
        val viewHolder = creator.invoke(parent)
        bindHolderEvents(viewHolder)
        itemCreated?.invoke(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        viewHolder as RvalViewHolder
        viewHolder.bindModel(item)
        viewHolder.bindRecyclerView(attachedRecyclerView)
        itemBinder?.invoke(viewHolder)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        item: Any,
        payloads: MutableList<Any>?
    ) {
        if (itemPayloadsBinder == null) {
            super.onBindViewHolder(viewHolder, item, payloads)
        } else {
            viewHolder as RvalViewHolder
            viewHolder.bindModel(item)
            itemPayloadsBinder!!.invoke(viewHolder, payloads)
        }

        //处理默认焦点位置
        if (defaultFocusedAdapterPosition >= 0 && viewHolder.view.canTakeFocusCompat()) {
            val recyclerView = viewHolder.view.findContainingRecyclerView() ?: return
            val vh = recyclerView.findContainingViewHolder(viewHolder.view) ?: return
            if (vh.adapterPosition == defaultFocusedAdapterPosition) {
                viewHolder.view.requestFocus()
                defaultFocusedAdapterPosition = -1
            }
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        if (viewHolder !is RvalViewHolder) return
        itemUnbinder?.invoke(viewHolder)
    }

    /**
     * 创建ViewHolder
     */
    fun itemCreate(creator: (parent: ViewGroup) -> RvalViewHolder) {
        itemCreator = creator
    }

    /**
     * 创建ViewHolder的view，调用该方法会覆盖之前设置的[itemCreate]
     */
    fun itemViewCreate(creator: (parent: ViewGroup) -> View) {
        itemCreate {
            RvalViewHolder(creator.invoke(it))
        }
    }

    /**
     * viewholder创建成功之后的回调
     */
    fun itemCreated(callback: LrvaViewHolderCallback?) {
        itemCreated = callback
    }

    fun itemBind(binder: LrvaViewHolderCallback?) {
        itemBinder = binder
    }

    fun itemBindPayloads(binder: ItemPayloadsBindCallback?) {
        itemPayloadsBinder = binder
    }

    fun itemUnbind(unBinder: LrvaViewHolderCallback?) {
        itemUnbinder = unBinder
    }

    /**
     * item view 点击事件
     * @param throttle 是否防抖动
     * @param periodMsc 防抖动的时间（毫秒），即在多少时间内只响应一次事件
     */
    fun itemClick(
        throttle: Boolean = false, periodMsc: Long = 500L, block: OnViewClick
    ) {
        this.itemClicker = EventHolder(throttle, periodMsc, block)
    }

    fun itemChildClick(@IdRes viewId: Int, block: OnViewClick) {
        itemChildClick(viewId, false, 500, block)
    }

    fun itemChildClick(@IdRes viewId: Int, throttle: Boolean, periodMsc: Long, block: OnViewClick) {
        this.itemChildClickers.put(viewId, EventHolder(throttle, periodMsc, block))
    }

    fun itemChildClick(viewIds: IntArray, block: OnViewClick) {
        itemChildClick(viewIds, false, 500L, block)
    }

    fun itemChildClick(viewIds: IntArray, throttle: Boolean, periodMsc: Long, block: OnViewClick) {
        viewIds.forEach {
            this.itemChildClickers.put(it, EventHolder(throttle, periodMsc, block))
        }
    }

    /**
     * 设置item view 焦点变化监听
     */
    fun itemFocusChanged(block: OnViewFocusChange?) {
        this.itemFocusChanger = block
    }

    /**
     * 按键监听
     */
    fun itemKeyEvent(block: OnViewKeyEvent?) {
        this.itemKeyListener = block
    }

//    /**
//     * 按键边界回调：在指定方向上找不到可获取焦点时回调
//     */
//    fun itemKeyEventBoundary(block: OnViewKeyEvent?) {
//        this.itemKeyBoundaryListener = block
//    }

    private fun bindHolderEvents(holder: RvalViewHolder) {
        itemClicker?.let { it ->
            val (throttle, period, block) = it
            if (throttle) {
                holder.view.throttleClick(period) { v -> block.invoke(holder, v) }
            } else {
                holder.view.setOnClickListener {
                    block.invoke(holder, it)
                }
            }
        }

        if (itemChildClickersLazy.isInitialized()) {
            itemChildClickers.forEach { key, value ->
                val (throttle, period, block) = value
                holder.getViewOrNull<View>(key)?.let { childView ->
                    if (throttle) {
                        childView.throttleClick(period) { v -> block.invoke(holder, v) }
                    } else {
                        childView.setOnClickListener {
                            block.invoke(holder, it)
                        }
                    }
                }
            }
        }

        itemFocusChanger?.let { listener ->
            val prvFocusListener = holder.view.onFocusChangeListener
            holder.view.setOnFocusChangeListener { v, hasFocus ->
                prvFocusListener?.onFocusChange(v, hasFocus)
                listener.invoke(holder, v, hasFocus)
            }
        }

        if (itemKeyListener != null) {
            holder.view.setOnKeyListener { v, keyCode, event ->
                itemKeyListener?.invoke(holder, keyCode, event) ?: false
            }
        }
//
//        if (focusSearchOptimization || itemKeyListener != null || itemKeyBoundaryListener != null) {
//            holder.view.setOnKeyListener { v, keyCode, event ->
//                var handled = itemKeyListener?.invoke(holder, keyCode, event) ?: false
//                try {
//                    if (!handled && focusSearchOptimization) {
//                        //寻焦优化
//                        handled = optFocusSearch(holder, keyCode, event)
//                    }
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//
//                if (!handled && itemKeyBoundaryListener != null) {//边界按键
//                    if (!focusSearchOptimization && event.action == KeyEvent.ACTION_DOWN) {
//                        //如果没有开启寻焦优化，并且没有处理过按键事件，且设置了边界按键事件，那么先做一下常规寻焦处理
//                        val rv = findPreferredRecyclerView(holder.view)
//                            ?: return@setOnKeyListener false
//                        val next = FocusFinder.getInstance()
//                            .findNextFocus(rv, v, transformFocusDirection(keyCode))
//                        if (next != null) {
//                            next.requestFocus()
//                            return@setOnKeyListener true
//                        }
//                    }
//                    handled = itemKeyBoundaryListener?.invoke(holder, keyCode, event) ?: false
//                }
//                handled
//            }
//        }
    }
//
//    private lateinit var focusSearchHelper: GridViewFocusSearchHelper<*>
//
//    private inline fun findPreferredRecyclerView(view: View): RecyclerView? {
//        return view.findContainingRecyclerView() ?: attachedRecyclerView
//    }

//    /**
//     * 焦点寻优优化
//     * todo 寻焦优化通过Presenter#itemView#keylistener监听实现不是很合理，应该通过recycleview处理更好
//     */
//    private fun optFocusSearch(
//        viewHolder: RvalViewHolder,
//        keyCode: Int,
//        event: KeyEvent
//    ): Boolean {
//        if (!focusSearchOptimization || event.action != KeyEvent.ACTION_DOWN)
//            return false
//        if (keyCode != KeyEvent.KEYCODE_DPAD_UP && keyCode != KeyEvent.KEYCODE_DPAD_RIGHT && keyCode != KeyEvent.KEYCODE_DPAD_DOWN && keyCode != KeyEvent.KEYCODE_DPAD_LEFT)
//            return false
//
//        val rv = findPreferredRecyclerView(viewHolder.view) ?: return false
//        if (rv is BaseGridView) {
//            if (rv is VerticalGridView) {
//                if (!::focusSearchHelper.isInitialized || focusSearchHelper.gridView != rv || focusSearchHelper !is VerticalGridViewFocusSearchHelper) {
//                    focusSearchHelper = VerticalGridViewFocusSearchHelper(rv)
//                }
//            } else if (rv is HorizontalGridView) {
//                if (!::focusSearchHelper.isInitialized || focusSearchHelper.gridView != rv || focusSearchHelper !is HorizontalGridViewFocusSearchHelper) {
//                    focusSearchHelper = HorizontalGridViewFocusSearchHelper(rv)
//                }
//            }
//            val next = focusSearchHelper.focusSearch(
//                viewHolder.view,
//                transformFocusDirection(keyCode)
//            )
//            if (next != null) {
//                next.requestFocus()
//                return true
//            }
//        } else {
//            val next = FocusFinder.getInstance()
//                .findNextFocus(rv, viewHolder.view, transformFocusDirection(keyCode))
//            if (next != null) {
//                next.requestFocus()
//                return true
//            }
//        }
//        return false
//    }

//    private fun transformFocusDirection(keyCode: Int): Int {
//        return when (keyCode) {
//            KeyEvent.KEYCODE_DPAD_UP -> View.FOCUS_UP
//            KeyEvent.KEYCODE_DPAD_LEFT -> View.FOCUS_LEFT
//            KeyEvent.KEYCODE_DPAD_RIGHT -> View.FOCUS_RIGHT
//            else -> View.FOCUS_DOWN
//        }
//    }

    internal fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        attachedRecyclerView = recyclerView
    }

    internal fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (attachedRecyclerView == recyclerView) {
            attachedRecyclerView = null
        }
    }

}