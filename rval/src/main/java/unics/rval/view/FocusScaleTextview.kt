//package unics.rval.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View.OnFocusChangeListener
//import androidx.appcompat.widget.AppCompatTextView
//import com.xinjing.utils.UIUtil
//import com.xinjing.utils.ext.focusShakeAnim
//
//class FocusScaleTextview @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
//) : AppCompatTextView(context, attrs, defStyleAttr) {
//
//    var focusCallBack: (Boolean) -> Unit = {}
//
//    init {
//        onFocusChangeListener = OnFocusChangeListener { view, status ->
//            focusCallBack(status)
//            this.paint.isFakeBoldText = status
//            view?.focusShakeAnim(status, UIUtil.SMALL_SCALE_FACTOR)
//        }
//    }
//}