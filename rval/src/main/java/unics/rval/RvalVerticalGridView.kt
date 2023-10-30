package unics.rval

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.leanback.widget.GridViewCompat
import androidx.leanback.widget.VerticalGridView

/**
 * Create by luochao
 * on 2023/10/30
 */
class RvalVerticalGridView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : VerticalGridView(context, attrs), GridViewCompat.Callback {

    private val mCompat = GridViewCompat(this, this)

    var focusSearchOptimization: Boolean
        get() = mCompat.focusSearchOptimization
        set(value) {
            mCompat.focusSearchOptimization = value
        }

    /**
     * 边界按键监听：通常用于实现边界抖动
     */
    var boundaryKeyListener: RvalBoundaryKeyListener?
        get() = mCompat.boundaryKeyListener
        set(value) {
            mCompat.boundaryKeyListener = value
        }

    /**
     * 是否启用边界抖动动画
     */
    var boundaryShakeEnable: Boolean
        get() = mCompat.boundaryShakeEnable
        set(value) {
            mCompat.boundaryShakeEnable = value
        }

    override fun focusSearch(focused: View?, direction: Int): View? {
        return mCompat.focusSearch(focused, direction)
    }

    override fun superFocusSearch(focused: View?, direction: Int): View? {
        return super.focusSearch(focused,direction)
    }

}