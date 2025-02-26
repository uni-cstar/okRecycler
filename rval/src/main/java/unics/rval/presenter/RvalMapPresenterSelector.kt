package unics.rval.presenter

import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector

open class RvalMapPresenterSelector(private val keyProvider: (item: Any?) -> String) :
    PresenterSelector() {

    internal val presenterMap: MutableMap<String, Presenter> = mutableMapOf()
    internal val presenters: MutableList<Presenter> = mutableListOf()

    override fun getPresenter(item: Any?): Presenter? {
        val key = keyProvider.invoke(item)
        return presenterMap[key]
    }

    fun add(key: String, presenter: Presenter): RvalMapPresenterSelector {
        presenterMap[key] = presenter
        val index = presenters.indexOf(presenter)
        if (index >= 0) {
            presenters[index] = presenter
        } else {
            presenters.add(presenter)
        }
        return this
    }

    fun remove(key: String) {
        val presenter = presenterMap[key]
        presenterMap.remove(key)
        if (presenter != null) {
            presenters.removeAll {
                it == presenter
            }
        }
    }

    override fun getPresenters(): Array<Presenter> {
        return presenters.toTypedArray()
    }

    fun getPresentersArrayList(): ArrayList<Presenter> {
        return ArrayList<Presenter>().also {
            it.addAll(getPresenters())
        }
    }

    /**
     * 将其他presenterSelector中的presenters添加到当前容器中
     */
    fun appendPresenterSelector(other: RvalMapPresenterSelector) {
        this.presenterMap.putAll(other.presenterMap)
        this.presenters.addAll(other.presenters)
    }

}