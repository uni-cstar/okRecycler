package unics.leanback.effect;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;

import java.lang.reflect.Field;

/**
 * Create by luochao
 * on 2023/11/15
 * 效果Drawable构建工厂
 */
interface EffectDrawableFactory<T extends EffectParams> {

    Drawable create(T params);

    static Drawable createEffectDrawable(Drawable content, EffectParams params) {
        float strokeSize = params.getStrokeSize();
        float contentGap = params.getContentGap();
        return new EffectDrawable(content,
                -(int) (params.getShadowLeft() + strokeSize + contentGap),
                -(int) (params.getShadowTop() + strokeSize + contentGap),
                -(int) (params.getShadowRight() + strokeSize + contentGap),
                -(int) (params.getShadowBottom() + strokeSize + contentGap));
    }

    final class NinePath implements EffectDrawableFactory<EffectParams.NinePathEffectParams> {

        private static class SingleTone {
            static EffectDrawableFactory<EffectParams.NinePathEffectParams> mInstance = new NinePath();
        }

        public static EffectDrawableFactory<EffectParams.NinePathEffectParams> getInstance() {
            return SingleTone.mInstance;
        }

        @Override
        public Drawable create(EffectParams.NinePathEffectParams params) {
            Drawable content;
            int strokeSize = (int) params.getStrokeSize();
            if (strokeSize > 0) {
                GradientDrawable strokeDrawable = new GradientDrawable();
                strokeDrawable.setStroke(strokeSize, params.getStrokeColor());
                if (params.getCornerRadius() > 0) {
                    strokeDrawable.setCornerRadius(params.getCornerRadius());
                }
                if (params.getCornerRadii() != null) {
                    strokeDrawable.setCornerRadii(params.getCornerRadii());
                }
                try {
                    if (Build.VERSION.SDK_INT >= 29) {
                        strokeDrawable.setPadding(strokeSize, strokeSize, strokeSize, strokeSize);
                    } else {
                        Field paddingField = GradientDrawable.class.getDeclaredField("mPadding");
                        paddingField.setAccessible(true);
                        Rect rect = (Rect) paddingField.get(strokeDrawable);
                        if (rect != null) {
                            rect.set(strokeSize, strokeSize, strokeSize, strokeSize);
                        } else {
                            paddingField.set(strokeDrawable, new Rect(strokeSize, strokeSize, strokeSize, strokeSize));
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                content = new LayerDrawable(new Drawable[]{params.getDrawable(), strokeDrawable});
            } else {
                content = params.getDrawable();
            }
            return createEffectDrawable(content, params);
        }
    }

    final class Draw implements EffectDrawableFactory<EffectParams.DrawEffectParams> {

        private static class SingleTone {
            static EffectDrawableFactory<EffectParams.DrawEffectParams> mInstance = new Draw();
        }

        public static EffectDrawableFactory<EffectParams.DrawEffectParams> getInstance() {
            return Draw.SingleTone.mInstance;
        }

        private static ShadowEffect createShadowEffect(EffectParams.DrawEffectParams params) {
            ShadowEffect drawable = new ShadowEffect(params.getShadowLeft(), params.getShadowTop(), params.getShadowRight(), params.getShadowBottom());
            drawable.setColor(params.getShadowColor());
            float[] radii = params.getCornerRadii();
            if (radii != null && radii.length > 0) {
                drawable.setCornerRadii(radii);
            } else {
                drawable.setCornerRadius(params.getCornerRadius());
            }
            return drawable;
        }

        private static StrokeEffect createStrokeEffect(EffectParams.DrawEffectParams params) {
            StrokeEffect drawable = new StrokeEffect(params.getStrokeSize());
            drawable.setColor(params.getStrokeColor());
            float[] radii = params.getCornerRadii();
            if (radii != null && radii.length > 0) {
                drawable.setCornerRadii(radii);
            } else {
                drawable.setCornerRadius(params.getCornerRadius());
            }
            return drawable;
        }

        @Override
        public Drawable create(EffectParams.DrawEffectParams params) {
            boolean hasShadow = params.getShadowLeft() > 0 || params.getShadowTop() > 0 || params.getShadowRight() > 0 || params.getShadowBottom() > 0;
            boolean hasStroke = params.getStrokeSize() > 0;
            Drawable content;
            if (hasShadow && hasStroke) {
                //有阴影有边框
                ShadowEffect shadow = createShadowEffect(params);
                StrokeEffect stroke = createStrokeEffect(params);
                content = new LayerDrawable(new Drawable[]{shadow, stroke});
            } else if (hasShadow) {
                //只有阴影
                content = createShadowEffect(params);
            } else if (hasStroke) {
                //只有边框
                content = createStrokeEffect(params);
            } else {
                //什么都没有
                content = null;
            }
            return createEffectDrawable(content, params);
        }
    }

}
