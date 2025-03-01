package unics.rval.presenter;

import androidx.annotation.IntDef;
import androidx.leanback.widget.FocusHighlight;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {ZoomFactor.ZOOM_FACTOR_NONE,
        ZoomFactor.ZOOM_FACTOR_SMALL,
        ZoomFactor.ZOOM_FACTOR_MEDIUM,
        ZoomFactor.ZOOM_FACTOR_LARGE,
        ZoomFactor.ZOOM_FACTOR_XSMALL})
@Retention(RetentionPolicy.SOURCE)
public @interface ZoomFactor {
    /**
     * No zoom factor.
     */
     int ZOOM_FACTOR_NONE = FocusHighlight.ZOOM_FACTOR_NONE;

    /**
     * A small zoom factor, recommended for large item views.
     */
     int ZOOM_FACTOR_SMALL = FocusHighlight.ZOOM_FACTOR_SMALL;

    /**
     * A medium zoom factor, recommended for medium sized item views.
     */
     int ZOOM_FACTOR_MEDIUM = FocusHighlight.ZOOM_FACTOR_MEDIUM;

    /**
     * A large zoom factor, recommended for small item views.
     */
     int ZOOM_FACTOR_LARGE = FocusHighlight.ZOOM_FACTOR_LARGE;

    /**
     * An extra small zoom factor.
     */
     int ZOOM_FACTOR_XSMALL = FocusHighlight.ZOOM_FACTOR_XSMALL;
}
