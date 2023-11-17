package unics.rva.sample.ui

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.AttributeSet
import android.util.Log
import org.xmlpull.v1.XmlPullParser

/**
 * Create by luochao
 * on 2023/11/13
 */
class XmlDrawableT : ShapeDrawable() {

    override fun inflate(
        r: Resources,
        parser: XmlPullParser,
        attrs: AttributeSet,
        theme: Resources.Theme?
    ) {
        super.inflate(r, parser, attrs, theme)
        Log.i("XmlDrawableT", "inflate: ")
    }

    override fun draw(canvas: Canvas) {
        canvas.drawColor(Color.RED)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}