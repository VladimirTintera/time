package eu.tintera.time.core.context

import eu.tintera.time.core.LocalDateTimeModifierBuilder
import eu.tintera.time.core.modify
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

/**
 * Modifies this [LocalDateTime] using the [LocalDateTimeModifierBuilder] DSL.
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context
 * to perform the operations in the block.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * with(TimeZone.UTC) {
 *     val result = ldt.modify {
 *         plusDays(2)
 *     }
 * }
 * ```
 *
 * @param block The builder block.
 * @return The modified [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.modify(
    block: LocalDateTimeModifierBuilder.() -> Unit
): LocalDateTime = modify(timeZone, block)

/**
 * Modifies this [LocalDateTime] using the [LocalDateTimeModifierBuilder] DSL as an invoke operator.
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * with(TimeZone.UTC) {
 *     val result = ldt {
 *         plusDays(2)
 *     }
 * }
 * ```
 *
 * @param block The builder block.
 * @return The modified [LocalDateTime].
 */
context(timeZone: TimeZone)
operator fun LocalDateTime.invoke(
    block: LocalDateTimeModifierBuilder.() -> Unit
): LocalDateTime = modify(timeZone, block)

