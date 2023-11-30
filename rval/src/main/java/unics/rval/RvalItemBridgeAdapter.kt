package unics.rval

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DiffCallback
import androidx.leanback.widget.ItemBridgeAdapter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector
import androidx.leanback.widget.SinglePresenterSelector
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class RvalItemBridgeAdapter private constructor(
    private val arrayObjectAdapter: ArrayObjectAdapter,
    private val presenterSelector: PresenterSelector
) : ItemBridgeAdapter() {

    constructor() : this(ArrayObjectAdapter(), ClassPresenterSelector())

    constructor(presenter: Presenter) : this(
        ArrayObjectAdapter(),
        SinglePresenterSelector(presenter)
    )

    constructor(presenterSelector: PresenterSelector) : this(
        ArrayObjectAdapter(presenterSelector),
        presenterSelector
    )

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
        if(presenterSelector !is ClassPresenterSelector)
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
        addAll(arrayObjectAdapter.size(),items)
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

    @JvmOverloads
    fun replaceAll(items: List<*>?, diffCallback: DiffCallback<*>? = null) {
        arrayObjectAdapter.setItems(items.orEmpty(), diffCallback)
    }

    @JvmOverloads
    fun setItems(items: List<*>?, diffCallback: DiffCallback<*>? = null) {
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
//        recyclerView.addOnUnhandledKeyEventListener()
        presenterSelector.presenters.forEach {
            if(it is RvalPresenter){
                it.onAttachedToRecyclerView(recyclerView)
            }
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        presenterSelector.presenters.forEach {
            if(it is RvalPresenter){
                it.onDetachedFromRecyclerView(recyclerView)
            }
        }
    }

    init {
        super.setPresenter(presenterSelector)
        super.setAdapter(arrayObjectAdapter)
    }

}