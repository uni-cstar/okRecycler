/**
 * Created by Lucio on 2021/11/30.
 * 横向/竖向 晃动动画
 */
package unics.rval

import android.view.View
import android.view.animation.Animation
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import androidx.annotation.IdRes

/**
 * Shake动画持续时间
 */
var SHAKE_DURATION = 500L

/**
 * 横向抖动
 */
fun View.shakeX(cycles: Float = 3f, duration: Long = SHAKE_DURATION) {
    startAnim(R.id.ucs_anim_tag_shake_x) {
        ShakeXAnimation(cycles = cycles, duration = duration)
    }
}

/**
 * 竖向抖动
 */
@JvmOverloads
fun View.shakeY(cycles: Float = 3f, duration: Long = SHAKE_DURATION) {
    startAnim(R.id.ucs_anim_tag_shake_y) {
        ShakeYAnimation(cycles = cycles, duration = duration)
    }
}

/**
 * 横向抖动动画
 * @param cycles 循环次数，默认3次
 */
fun ShakeXAnimation(
    cycles: Float = 3f,
    duration: Long = SHAKE_DURATION
): Animation {
    return TranslateAnimation(0.0f, 4.0f, 0.0f, 0.0f)
        .also {
            it.duration = duration
            it.interpolator = CycleInterpolator(cycles)
        }

}

/**
 * 竖向抖动动画
 * @param cycles 循环次数，默认3次
 */
fun ShakeYAnimation(
    cycles: Float = 3f,
    duration: Long = SHAKE_DURATION
): Animation {
    return TranslateAnimation(0.0f, 0.0f, 0.0f, 4.0f)
        .also {
            it.duration = duration
            it.interpolator = CycleInterpolator(cycles)
        }

}


internal inline fun View.startAnim(
    @IdRes animTag: Int,
    initializer: () -> Animation
) {
    var anim = this.getTag(animTag) as? Animation
    if (anim == null) {
        anim = initializer()
        this.setTag(animTag, anim)
    }
    clearAnimation()
    startAnimation(anim)
}
