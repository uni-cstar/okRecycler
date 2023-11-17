package unics.leanback.effect;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;

import androidx.annotation.Nullable;

/**
 * Create by luochao
 * on 2023/11/14
 */
class EffectDrawable extends InsetDrawable {

    public EffectDrawable(@Nullable Drawable drawable, int inset) {
        super(drawable, inset);
    }

    public EffectDrawable(@Nullable Drawable drawable, int insetLeft, int insetTop, int insetRight, int insetBottom) {
        super(drawable, insetLeft, insetTop, insetRight, insetBottom);
    }

//    @Override
//    public void draw(@NonNull Canvas canvas) {
//        super.draw(canvas);
//        StringBuilder sb = new StringBuilder();
//        sb.append("bounds=").append(this.getBounds()).append("\n");
//        sb.append("getIntrinsicHeight=").append(this.getIntrinsicHeight()).append("\n");
//        sb.append("getIntrinsicWidth=").append(this.getIntrinsicWidth()).append("\n");
//        sb.append("getMinimumHeight=").append(this.getMinimumHeight()).append("\n");
//        sb.append("getMinimumWidth=").append(this.getMinimumWidth()).append("\n");
//        sb.append("getOpacity=").append(this.getOpacity()).append("\n");
//        Rect padding = new Rect();
//        this.getPadding(padding);
//        sb.append("getPadding=").append(padding).append("\n");
//        sb.append("getChangingConfigurations=").append(this.getChangingConfigurations()).append("\n");
//        sb.append("getConstantState=").append(this.getConstantState()).append("\n");
//        sb.append("getLevel=").append(this.getLevel()).append("\n");
//        sb.append("getDrawable=").append(this.getDrawable().getIntrinsicWidth()).append("\n");
//        sb.append("getDrawable=").append(this.getDrawable().getIntrinsicHeight()).append("\n");
//        Effects.log("EffectDrawable:" + sb);
////        canvas.drawLine(0, getBounds().height()/2f, getBounds().width(), getBounds().height(), mPaint);
//    }
//
//    @Override
//    public void setBounds(int left, int top, int right, int bottom) {
//        super.setBounds(left, top, right, bottom);
//        Effects.log("setBounds:" + left + "," + top + "," + right + "," + bottom);
//    }

}
