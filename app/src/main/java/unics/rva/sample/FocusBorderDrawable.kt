package unics.rva.sample

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import java.util.jar.Attributes

/**
 * Create by luochao
 * on 2023/11/2
 */
class FocusBorderDrawable(
    drawable: Drawable,
    insetLeftFraction: Int,
    insetTopFraction: Int,
    insetRightFraction: Int,
    insetBottomFraction: Int
) : InsetDrawable(
    drawable,
    insetLeftFraction,
    insetTopFraction,
    insetRightFraction,
    insetBottomFraction
) {

    companion object{

//        fun inflate(context: Context,attributes: Attributes?):Drawable{
//            val ta = context.obtainStyledAttributes(attributes,R.styleable)
//            val ninePatchRes =
//        }
    }
}

class FocusBorderLayer(layers: Array<out Drawable>) : LayerDrawable(layers)