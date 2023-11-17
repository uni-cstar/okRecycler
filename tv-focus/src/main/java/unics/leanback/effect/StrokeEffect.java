package unics.leanback.effect;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by luochao
 * on 2023/11/15
 */
final class StrokeEffect extends AbstractEffect<StrokeEffect.StrokeState> {

    private boolean mPathIsDirty = true;

    StrokeEffect(float strokeSize) {
        super(new StrokeState(strokeSize));
    }

    private StrokeEffect(@NonNull StrokeState state, @Nullable Resources resources) {
        super(state, resources);
    }

    public void setStrokeWidth(float width) {
        if (mState.mPaint.getStrokeWidth() == width)
            return;
        mState.mPaint.setStrokeWidth(width);
        mPathIsDirty = true;
        invalidateSelf();
    }

    @Override
    public void setCornerRadii(@Nullable float[] radii) {
        mPathIsDirty = true;
        super.setCornerRadii(radii);
    }

    @Override
    public void setCornerRadius(float radius) {
        mPathIsDirty = true;
        super.setCornerRadius(radius);
    }

    @Override
    public boolean getPadding(Rect padding) {
        int strokeWidth = (int)mState.mPaint.getStrokeWidth();
        padding.set(strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        return (padding.left | padding.top | padding.right | padding.bottom) != 0;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            Effects.log("StrokeBounds#draw: empty bounds,return.");
            return;
        }
        buildPathIfDirty();
        Effects.log("StrokeBounds#draw$bounds:" + bounds);
        canvas.drawPath(mState.mStrokePath, mState.mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        Effects.log("StrokeBounds#setBounds:" + left + "," + top + "," + right + "," + bottom);
    }

    private void buildPathIfDirty() {
        if (!mPathIsDirty)
            return;

        final StrokeEffect.StrokeState st = mState;
        Effects.log("ShadowEffect#draw: buildPathIfDirty=" + st.mRectF);
        st.mStrokePath.rewind();
        Rect bounds = getBounds();

        float inset = st.mPaint.getStrokeWidth() * 0.5f;
        float left = bounds.left + inset;
        float top = bounds.top + inset;
        float right = bounds.right - inset;
        float bottom = bounds.bottom - inset;
        st.mRectF.set(left, top, right, bottom);
        //必须使用这种方式，否则在某些情况下会出现中间有个阴影色的色块
        if (st.mRadiusArray != null && st.mRadiusArray.length > 0) {
            st.mStrokePath.addRoundRect(st.mRectF, st.mRadiusArray, Path.Direction.CW);
        } else if (st.mRadius > 0) {
            st.mStrokePath.addRoundRect(st.mRectF, st.mRadius, st.mRadius, Path.Direction.CW);
        } else {
            st.mStrokePath.addRoundRect(st.mRectF, 0f, 0f, Path.Direction.CW);
        }
        Effects.log("ShadowEffect#draw: buildPathIfDirty=" + st.mRectF);
        mPathIsDirty = false;
    }

    static final class StrokeState extends AbstractEffect.AbstractState {

        Path mStrokePath = new Path();
        RectF mRectF = new RectF();

        StrokeState(float strokeSize) {
            super();
            mPaint.setStrokeWidth(strokeSize);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        StrokeState(@NonNull StrokeState orig) {
            super(orig);
            mStrokePath.set(orig.mStrokePath);
            mRectF.set(orig.mRectF);
        }

        @Override
        public Drawable newDrawable() {
            return new StrokeEffect(new StrokeState(this), null);
        }

        @Override
        public Drawable newDrawable(Resources res) {
            return new StrokeEffect(new StrokeState(this), res);
        }

    }

}
