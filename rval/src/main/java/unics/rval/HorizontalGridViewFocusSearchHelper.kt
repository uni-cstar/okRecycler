package androidx.leanback.widget

import android.view.View
import java.lang.reflect.Field
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

/**
 * Create by luochao
 * on 2023/10/24
 */
internal class HorizontalGridViewFocusSearchHelper (view: HorizontalGridView) :
    GridViewFocusSearchHelper<HorizontalGridView>(view) {

    private var numberRowField: Field? = null

    /**
     * 获取用于下一个获取焦点的位置
     * @return 返回下一个获取焦点view的位置，如果返回跟当前位置相同，则不处理
     */
    override fun getNextFocusAdapterPosition(currentAdapterPosition: Int, direction: Int): Int {
        val maxPosition = max(currentAdapterPosition, itemCount - 1)
        when (direction) {
            View.FOCUS_UP -> {
                return if (isRtl) {
                    if (currentAdapterPosition == maxPosition) return currentAdapterPosition
                    min(maxPosition, currentAdapterPosition + 1)
                } else {
                    max(0, currentAdapterPosition - 1)
                }
            }

            View.FOCUS_DOWN -> {
                return if (isRtl) {
                    max(0, currentAdapterPosition - 1)
                } else {
                    if (currentAdapterPosition == maxPosition) return currentAdapterPosition
                    min(maxPosition, currentAdapterPosition + 1)
                }
            }

            View.FOCUS_LEFT, View.FOCUS_RIGHT -> {
                if (currentAdapterPosition == maxPosition && direction == View.FOCUS_RIGHT) return currentAdapterPosition
                try {
                    val layoutManager = getGridLayoutManager() ?: return currentAdapterPosition
                    val numberColumnField = numberRowField
                        ?: GridLayoutManager::class.java.getDeclaredField("mNumRowsRequested")
                            .also {
                                it.isAccessible = true
                                numberRowField = it
                            }
                    val numberColumn = numberColumnField.getInt(layoutManager)
                    if (direction == View.FOCUS_LEFT) {
                        val newPosition = (currentAdapterPosition - numberColumn).coerceAtLeast(0)
                        if (numberColumn <= 1) return newPosition
                        val inFirstRow =
                            newPosition < numberColumn && currentAdapterPosition < numberColumn
                        return if (inFirstRow) {
                            currentAdapterPosition
                        } else {
                            newPosition
                        }
                    } else {
                        val newPosition = (currentAdapterPosition + numberColumn).coerceAtMost(
                            maxPosition
                        )
                        if (numberColumn <= 1) return newPosition
                        val lastRowIndex = ceil((maxPosition + 1.0) / numberColumn).toInt()
                        val inLastRow =
                            lastRowIndex == ceil((currentAdapterPosition + 1.0) / numberColumn).toInt() && lastRowIndex == ceil(
                                (newPosition + 1.0) / numberColumn
                            ).toInt()
                        return if (inLastRow) {
                            currentAdapterPosition
                        } else {
                            newPosition
                        }
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
        return currentAdapterPosition
    }

}