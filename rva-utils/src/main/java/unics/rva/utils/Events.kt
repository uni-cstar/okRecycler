package unics.rva.utils

/**
 * Create by luochao
 * on 2023/10/25
 */
data class EventHolder<T : Function<*>>(
    val throttle: Boolean,
    val period: Long,
    val block: T
)