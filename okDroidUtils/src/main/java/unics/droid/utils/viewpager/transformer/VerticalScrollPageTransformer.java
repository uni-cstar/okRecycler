package unics.droid.utils.viewpager.transformer;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Create by luochao
 * on 2023/11/27
 * 垂直滚动转换效果
 */
public class VerticalScrollPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        float alpha = 0f;
        if (position >= 0 && position <= 1.0) {
            alpha = 1 - position;
        } else if (position > -1 && position < 0) {
            alpha = position + 1;
        }
        page.setAlpha(alpha);
        page.setTranslationX(page.getWidth() * -position);
        page.setTranslationY(position * page.getHeight());
    }
}
