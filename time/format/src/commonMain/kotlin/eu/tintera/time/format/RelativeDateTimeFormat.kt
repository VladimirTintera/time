package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

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
interface RelativeDateTimeFormat {
    /**
     * The style to use for formatting the unit names (full, short, narrow).
     */
    val style: FormatStyle

    /** Threshold for formatting differences in years. If null, years are not used. */
    val years: UnitThreshold?

    /** Threshold for formatting differences in months. If null, months are not used. */
    val months: UnitThreshold?

    /** Threshold for formatting differences in days. If null, days are not used. */
    val days: UnitThreshold?

    /** Threshold for formatting differences in hours. If null, hours are not used. */
    val hours: UnitThreshold?

    /** Threshold for formatting differences in minutes. If null, minutes are not used. */
    val minutes: UnitThreshold?

    /** Threshold for formatting differences in seconds. If null, seconds are not used. */
    val seconds: UnitThreshold?
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

/**
 * Builder for constructing [RelativeDateTimeFormat] configurations using a DSL.
 *
 * Example:
 * ```kotlin
 * val builder = RealRelativeDateTimeFormatBuilder().apply {
 *     style = FormatStyle.Short
 *     hours(2)
 * }
 * val format = builder.build()
 * ```
 */
@TimeDslMarker
class RealRelativeDateTimeFormatBuilder internal constructor() : RelativeDateTimeFormat {
    /** The style to use for formatting. Defaults to [FormatStyle.Full]. */
    override var style: FormatStyle = FormatStyle.Full

    /** Threshold for formatting differences in years. Defaults to 1. */
    override var years: UnitThreshold? = UnitThreshold(1)

    /** Threshold for formatting differences in months. Defaults to 1. */
    override var months: UnitThreshold? = UnitThreshold(1)

    /** Threshold for formatting differences in days. Defaults to 1. */
    override var days: UnitThreshold? = UnitThreshold(1)

    /** Threshold for formatting differences in hours. Defaults to 1. */
    override var hours: UnitThreshold? = UnitThreshold(1)

    /** Threshold for formatting differences in minutes. Defaults to 1. */
    override var minutes: UnitThreshold? = UnitThreshold(1)

    /** Threshold for formatting differences in seconds. Defaults to null (disabled). */
    override var seconds: UnitThreshold? = null

    /**
     * Configures the threshold for the years unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     years(2)
     * }
     * ```
     *
     * @param min The minimum number of years, or null to disable relative years.
     */
    fun years(min: Int? = 1) {
        years = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the months unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     months(2)
     * }
     * ```
     *
     * @param min The minimum number of months, or null to disable relative months.
     */
    fun months(min: Int? = 1) {
        months = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the days unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     days(2)
     * }
     * ```
     *
     * @param min The minimum number of days, or null to disable relative days.
     */
    fun days(min: Int? = 1) {
        days = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the hours unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     hours(2)
     * }
     * ```
     *
     * @param min The minimum number of hours, or null to disable relative hours.
     */
    fun hours(min: Int? = 1) {
        hours = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the minutes unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     minutes(2)
     * }
     * ```
     *
     * @param min The minimum number of minutes, or null to disable relative minutes.
     */
    fun minutes(min: Int? = 1) {
        minutes = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the seconds unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     seconds(10)
     * }
     * ```
     *
     * @param min The minimum number of seconds, or null to disable relative seconds.
     */
    fun seconds(min: Int? = 1) {
        seconds = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Builds and returns a [RelativeDateTimeFormat] instance.
     *
     * Example:
     * ```kotlin
     * val builder = RealRelativeDateTimeFormatBuilder()
     * val format = builder.build()
     * ```
     */
    fun build(): RelativeDateTimeFormat = this
}

/**
 * Creates a [RelativeDateTimeFormat] instance using a DSL configuration block.
 *
 * Example:
 * ```kotlin
 * val format = RelativeDateTimeFormat {
 *     style = FormatStyle.Full
 *     days(2)
 * }
 * ```
 *
 * @param block The configuration block applied to [RealRelativeDateTimeFormatBuilder].
 * @return The configured [RelativeDateTimeFormat].
 */
fun RelativeDateTimeFormat(
    block: RealRelativeDateTimeFormatBuilder.() -> Unit
) = RealRelativeDateTimeFormatBuilder().apply(block).build()