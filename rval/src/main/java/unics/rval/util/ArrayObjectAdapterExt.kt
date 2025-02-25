package unics.rval.util

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector

fun newArrayObjectAdapter(data: List<*>, presenter: Presenter): ArrayObjectAdapter {
    return ArrayObjectAdapter(presenter).also {
        it.addAll(0, data)
    }
}

fun newArrayObjectAdapter(data: List<*>, presenterSelector: PresenterSelector): ArrayObjectAdapter {
    return ArrayObjectAdapter(presenterSelector).also {
        it.addAll(0, data)
    }
}