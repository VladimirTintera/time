package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker
import kotlinx.datetime.LocalDateTime

/**
 * Configuration for formatting relative date-time differences (e.g., "3 days ago", "in 2 hours").
 *
 * Each unit can have an optional [UnitThreshold] specifying when it should start being used.
 *
 * Example:
 * ```kotlin
 * val format = RelativeDateTimeFormat {
 *     style = FormatStyle.Full
 *     days(2)
 * }
 * ```
 */
class RelativeDateTimeFormat internal constructor(
    val block: RelativeDateTimeFormatScope.() -> Unit = RelativeDateTimeFormatScope.defaultConfig
) {
   companion object {
       operator fun invoke(
           block: RelativeDateTimeFormatScope.() -> Unit = RelativeDateTimeFormatScope.defaultConfig
       ) : RelativeDateTimeFormat = RelativeDateTimeFormat(block)
   }
}

/**
 * Defines a threshold for when a time unit should be used for relative formatting.
 *
 * Example:
 * ```kotlin
 * val threshold = UnitThreshold(5)
 * ```
 */
interface UnitThreshold {
    /**
     * The minimum difference in this unit required to format the difference using this unit.
     */
    val min: Int
}

/**
 * Creates a [UnitThreshold] instance with the specified minimum value.
 *
 * Example:
 * ```kotlin
 * val threshold = UnitThreshold(min = 5)
 * ```
 *
 * @param min The minimum absolute value required to trigger this unit.
 * @return The configured [UnitThreshold].
 */
fun UnitThreshold(min: Int): UnitThreshold = UnitThresholdImpl(min = min)

internal data class UnitThresholdImpl(
    override val min: Int = 1
) : UnitThreshold

