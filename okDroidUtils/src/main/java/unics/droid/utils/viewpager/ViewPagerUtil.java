package unics.droid.utils.viewpager;

import android.view.animation.Interpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

/**
 * Create by luochao
 * on 2023/11/27
 */
public class ViewPagerUtil {

    /**
     * 设置固定滚动持续时间
     * @param sDuration 固定滚动时间
     */
    public static void setFixedScrollDurationScroller(ViewPager pager, int sDuration) throws NoSuchFieldException, IllegalAccessException {
        setFixedScrollDurationScroller(pager, sDuration, null);
    }

    /**
     * 设置固定滚动持续时间
     * @param sDuration 固定滚动时间
     * @param interpolator 插值器
     */
    public static void setFixedScrollDurationScroller(ViewPager pager, int sDuration, Interpolator interpolator) throws NoSuchFieldException, IllegalAccessException {
        setScroller(pager, new Scroller(pager.getContext(), interpolator) {
            @Override
            public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                super.startScroll(startX, startY, dx, dy, sDuration);
            }
        });
    }

    public static void setScroller(ViewPager pager, Scroller scroller) throws NoSuchFieldException, IllegalAccessException {
        Field field = ViewPager.class.getDeclaredField("mScroller");
        field.setAccessible(true);
        field.set(pager, scroller);
    }

}
