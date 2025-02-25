package unics.rval

import android.view.KeyEvent
import android.view.View
import unics.rval.presenter.RvalViewHolder

/**
 * Create by luochao
 * on 2023/10/25
 */

typealias LrvaViewHolderCallback = (RvalViewHolder).() -> Unit

typealias ItemPayloadsBindCallback = (RvalViewHolder).(payloads: MutableList<Any>?) -> Unit

/**
 * view点击代码块。view参数为被点击的控件
 */
typealias OnViewClick = RvalViewHolder.(view: View) -> Unit

/**
 * view焦点变化代码块。
 */
typealias OnViewFocusChange = RvalViewHolder.(view: View, hasFocus: Boolean) -> Unit

typealias OnViewKeyEvent = RvalViewHolder.(keyCode: Int, event: KeyEvent) -> Boolean