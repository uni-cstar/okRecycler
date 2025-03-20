package unics.rval.presenter

import android.annotation.SuppressLint
import androidx.leanback.widget.BaseGridView
import androidx.leanback.widget.HorizontalGridView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.RowPresenter

@SuppressLint("RestrictedApi")
open class RvalListRowNestedPresenter : RvalListRowPresenter {
    var focusScrollStrategy = BaseGridView.FOCUS_SCROLL_ALIGNED
    var onInitialGridView: ((HorizontalGridView) -> Unit)? = null

    constructor() : super()
    constructor(focusZoomFactor: Int) : super(focusZoomFactor)
    constructor(focusZoomFactor: Int, useFocusDimmer: Boolean) : super(
        focusZoomFactor,
        useFocusDimmer
    )

    @SuppressLint("RestrictedApi")
    override fun initializeRowViewHolder(holder: RowPresenter.ViewHolder?) {
        super.initializeRowViewHolder(holder)
        (holder as? ViewHolder)?.gridView?.let {
            //去除焦点记忆
            it.focusScrollStrategy = focusScrollStrategy
            onInitialGridView?.invoke(it)
        }
    }

    override fun onBindRowViewHolder(holder: RowPresenter.ViewHolder?, item: Any?) {
        super.onBindRowViewHolder(holder, item)
        val vh = holder as ViewHolder
        val rowItem = item as ListRow
//        vh.mItemBridgeAdapter.setAdapter(rowItem.adapter)
//        vh.mGridView.adapter = vh.mItemBridgeAdapter
//        vh.mGridView.contentDescription = rowItem.contentDescription
    }
}