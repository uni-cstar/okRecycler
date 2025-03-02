package unics.rval;

import android.view.View;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef(value = {FocusDirection.FOCUS_UP,
        FocusDirection.FOCUS_RIGHT,
        FocusDirection.FOCUS_DOWN,
        FocusDirection.FOCUS_LEFT,
        FocusDirection.FOCUS_BACKWARD, FocusDirection.FOCUS_FORWARD})
@Retention(RetentionPolicy.SOURCE)
public @interface FocusDirection {

    int FOCUS_UP = View.FOCUS_UP;

    int FOCUS_RIGHT = View.FOCUS_RIGHT;

    int FOCUS_DOWN = View.FOCUS_DOWN;

    int FOCUS_LEFT = View.FOCUS_LEFT;
    int FOCUS_BACKWARD = View.FOCUS_BACKWARD;
    int FOCUS_FORWARD = View.FOCUS_FORWARD;


}
