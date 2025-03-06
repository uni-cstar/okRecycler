package unics.rval.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DiffCallback
import androidx.leanback.widget.FocusHighlightHelper
import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import androidx.leanback.widget.SinglePresenterSelector
import androidx.recyclerview.widget.RecyclerView
import unics.rva.utils.canTakeFocusCompat
import unics.rval.presenter.RvalPresenter
import unics.rval.presenter.ZoomFactor

/**
 * ！！！ itemViewType根据presenter的位置来决定，并且很难破坏这个设计，因此最好不要随便改变这个设计
 */
class RvalItemBridgeAdapter private constructor(
    private val arrayObjectAdapter: ArrayObjectAdapter,
    private val presenterSelector: PresenterSelector
) : ItemBridgeAdapter() {

    /**
     * 默认获取焦点的item位置（只在第一次显示该item时请求焦点）
     */
    var defaultFocusedAdapterPosition: Int = -1
        set(value) {
            field = value
            handleDefaultFocusPosition()
        }

    /**
     * 默认请求焦点时，是否采用延迟，默认不延迟
     * @see defaultFocusedAdapterPosition
     */
    var defaultFocusedRequestDelay: Long = -1

    private var mAttachedRecyclerView: RecyclerView? = null

    constructor() : this(ArrayObjectAdapter(), ClassPresenterSelector())

    constructor(presenter: Presenter) : this(
        ArrayObjectAdapter(),
        SinglePresenterSelector(presenter)
    )

    constructor(presenterSelector: PresenterSelector) : this(
        ArrayObjectAdapter(presenterSelector),
        presenterSelector
    )

    fun setupHeaderItemFocusHighlight(scaleEnabled: Boolean = true) {
        FocusHighlightHelper.setupHeaderItemFocusHighlight(this, scaleEnabled)
    }

    fun setupBrowseItemFocusHighlight(
        @ZoomFactor
        zoomIndex: Int,
        useDimmer: Boolean = false
    ) {
        FocusHighlightHelper.setupBrowseItemFocusHighlight(this, zoomIndex, useDimmer)
    }

    override fun setAdapter(adapter: ObjectAdapter?) {
        throw RuntimeException("不允许修改ObjectAdapter，内部已提供实现")
    }

    inline fun <reified T> addClassPresenter(setup: RvalPresenter.() -> Unit): RvalItemBridgeAdapter {
        return addClassPresenter(T::class.java, RvalPresenter().apply(setup))
    }

    inline fun <reified T> addClassPresenter(presenter: Presenter): RvalItemBridgeAdapter {
        return addClassPresenter(T::class.java, presenter)
    }

    fun addClassPresenter(cls: Class<*>, presenter: Presenter): RvalItemBridgeAdapter {
        if (presenterSelector !is ClassPresenterSelector)
            throw IllegalStateException("the presenter selector is not ClassPresenterSelector,cannot perform this action.")
        presenterSelector.addClassPresenter(cls, presenter)
        return this
    }

    inline fun <reified T> addClassPresenterSelector(presenter: PresenterSelector): RvalItemBridgeAdapter {
        return addClassPresenterSelector(T::class.java, presenter)
    }

    fun addClassPresenterSelector(
        cls: Class<*>,
        presenterSelector: PresenterSelector
    ): RvalItemBridgeAdapter {
        if (this.presenterSelector !is ClassPresenterSelector)
            throw IllegalStateException("the presenter selector is not ClassPresenterSelector,cannot perform this action.")
        this.presenterSelector.addClassPresenterSelector(cls, presenterSelector)
        return this
    }

    fun add(item: Any) {
        arrayObjectAdapter.add(item)
    }

    fun add(index: Int, item: Any) {
        arrayObjectAdapter.add(index, item)
    }

    fun addAll(index: Int, items: Collection<*>) {
        arrayObjectAdapter.addAll(index, items)
    }

    fun addAll(items: Collection<*>) {
        addAll(arrayObjectAdapter.size(), items)
    }

    fun remove(item: Any): Boolean {
        return arrayObjectAdapter.remove(item)
    }

    fun removeItems(position: Int, count: Int): Int {
        return arrayObjectAdapter.removeItems(position, count)
    }

    fun move(fromPosition: Int, toPosition: Int) {
        arrayObjectAdapter.move(fromPosition, toPosition)
    }

    fun replace(position: Int, item: Any) {
        arrayObjectAdapter.replace(position, item)
    }

    fun replaceAll(items: List<*>?, diffCallback: DiffCallback<*>?) {
        arrayObjectAdapter.setItems(items.orEmpty(), diffCallback)
    }

    fun setItems(items: List<*>?, diffCallback: DiffCallback<*>?) {
        arrayObjectAdapter.setItems(items.orEmpty(), diffCallback)
    }

    override fun clear() {
        super.clear()
        arrayObjectAdapter.clear()
    }

    operator fun <T> get(index: Int): T {
        return arrayObjectAdapter.get(index) as T
    }

    fun <T> getOrNull(index: Int): T? {
        return try {
            this[index]
        } catch (e: Throwable) {
            null
        }
    }

    fun indexOf(item: Any): Int {
        return arrayObjectAdapter.indexOf(item)
    }

    fun indexOf(filter: (Any) -> Boolean): Int {
        return arrayObjectAdapter.unmodifiableList<Any>().indexOfFirst(filter)
    }

    fun <E> unmodifiableList(): List<E> {
        return arrayObjectAdapter.unmodifiableList()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.mAttachedRecyclerView = recyclerView
//        recyclerView.addOnUnhandledKeyEventListener()
        presenterSelector.presenters.forEach {
            if (it is RvalPresenter) {
                it.onAttachedToRecyclerView(recyclerView)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.mAttachedRecyclerView = null
        presenterSelector.presenters.forEach {
            if (it is RvalPresenter) {
                it.onDetachedFromRecyclerView(recyclerView)
            }
        }
    }

    override fun onBind(viewHolder: ViewHolder?) {
        super.onBind(viewHolder)
        viewHolder ?: return
        //处理默认焦点位置
        if (defaultFocusedAdapterPosition >= 0 && defaultFocusedAdapterPosition == viewHolder.bindingAdapterPosition) {
            val itemView = viewHolder.itemView
            if (itemView is ViewGroup) {
                if (itemView.canTakeFocusCompat()) {
                    requestDefaultFocus(itemView)
                    defaultFocusedAdapterPosition = -1
                } else {
                    val list = arrayListOf<View>()
                    itemView.addFocusables(list, View.FOCUS_DOWN)
                    list.firstOrNull()?.let {
                        requestDefaultFocus(it)
                        defaultFocusedAdapterPosition = -1
                    }
                }
            } else if (itemView.canTakeFocusCompat()) {
                requestDefaultFocus(itemView)
                defaultFocusedAdapterPosition = -1
            }
        }
    }

    private fun requestDefaultFocus(view: View) {
        if (defaultFocusedRequestDelay > 0) {
            view.postDelayed({
                view.requestFocus()
            }, defaultFocusedRequestDelay)
        } else {
            view.requestFocus()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun handleDefaultFocusPosition() {
        if (defaultFocusedAdapterPosition < 0)
            return
        (mAttachedRecyclerView as? BaseGridView)?.let { rv ->
            if (rv.selectedPosition == RecyclerView.NO_POSITION && rv.focusScrollStrategy == BaseGridView.FOCUS_SCROLL_ALIGNED) {
                rv.selectedPosition = defaultFocusedAdapterPosition
            }
        }
    }

    init {
        super.setPresenter(presenterSelector)
        super.setAdapter(arrayObjectAdapter)
    }

}