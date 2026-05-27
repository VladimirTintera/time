package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Represents a configuration for formatting times.
 *
 * This interface defines which components (hour, minute, seconds, milliseconds, am/pm)
 * should be included in the formatted output and their respective styles.
 *
 * Example:
 * ```kotlin
 * val format = TimeFormat {
 *     hour = HourFormat.Auto.Numeric
 *     minute = MinuteFormat.Padded
 * }
 * ```
 */
interface TimeFormat {
    /** The format style for the hour component, or null if omitted. */
    val hour: HourFormat?

    /** The format style for the minute component, or null if omitted. */
    val minute: MinuteFormat?

    /** The format style for the second component, or null if omitted. */
    val second: SecondFormat?

    /** The format style for the fractional second component, or null if omitted. */
    val fractionalSecond: FractionalSecondFormat?

    /** Indicates whether to include the AM/PM marker in the formatted output. */
    val periodStyle: DayPeriodStyle?
}

internal fun TimeFormat.isEmpty() = hour == null && minute == null && second == null

internal fun TimeFormat.toTimeCldrSkeleton(): String = buildString {

    when (periodStyle) {
        DayPeriodStyle.Required -> {
            when (hour) {
                HourFormat.Auto.Numeric -> append(HourFormat.Digital12.Numeric.pattern)
                HourFormat.Auto.Padded -> append(HourFormat.Digital12.Padded.pattern)
                else -> hour?.also { append(it.pattern) }
            }
            append("a")
        }

        DayPeriodStyle.None -> hour?.also {
            when (it) {
                HourFormat.Auto.Numeric, HourFormat.Digital12.Numeric -> append(HourFormat.Digital24h.Numeric.pattern)
                HourFormat.Auto.Padded, HourFormat.Digital12.Padded -> append(HourFormat.Digital24h.Padded.pattern)
                else -> append(it.pattern)
            }
        }

        else -> hour?.also { append(it.pattern) }
    }

    minute?.also { append(it.pattern) }
    second?.also {
        append(it.pattern)
        fractionalSecond?.also { fraction ->
            append(".")
            append(fraction.pattern)
        }
    }
}

/**
 * Builder for creating [TimeFormat] instances using a DSL.
 *
 * This builder provides a flexible way to construct a [TimeFormat] by specifying
 * the desired format for each time component. It also includes predefined styles
 * for convenience.
 *
 * Example:
 * ```kotlin
 * val builder = TimeFormatBuilder().apply {
 *     hour = HourFormat.Auto.Numeric
 *     minute = MinuteFormat.Padded
 * }
 * val format = builder.build()
 * ```
 */
@TimeDslMarker
class TimeFormatBuilder internal constructor() : TimeFormat {
    /** The format style for the hour component, or null if omitted. */
    override var hour: HourFormat? = null

    /** The format style for the minute component, or null if omitted. */
    override var minute: MinuteFormat? = null

    /** The format style for the second component, or null if omitted. */
    override var second: SecondFormat? = null

    /** The format style for the fractional second component, or null if omitted. */
    override var fractionalSecond: FractionalSecondFormat? = null

    /** Indicates whether to include the AM/PM marker in the formatted output. */
    override var periodStyle: DayPeriodStyle? = null

    /**
     * Builds and returns a [TimeFormat] instance.
     *
     * Example:
     * ```kotlin
     * val builder = TimeFormatBuilder()
     * val format = builder.build()
     * ```
     *
     * @return The configured [TimeFormat].
     */
    fun build(): TimeFormat = this

    /**
     * Copies the configuration from an existing [TimeFormat].
     *
     * This allows for easily extending or modifying a predefined format.
     *
     * Example:
     * ```kotlin
     * val existingFormat = TimeFormat { short() }
     * val format = TimeFormat {
     *     from(existingFormat)
      *    second = SecondFormat.Padded
     * }
     * ```
     *
     * @param timeFormat The format to copy from.
     */
    fun from(timeFormat: TimeFormat) {
        hour = timeFormat.hour
        minute = timeFormat.minute
        second = timeFormat.second
        fractionalSecond = timeFormat.fractionalSecond
        periodStyle = timeFormat.periodStyle
    }

    /**
     * Applies a short time format.
     *
     * This typically includes an automatic hour format and a padded minute.
     *
     * Example:
     * ```kotlin
     * val format = TimeFormat {
     *     short()
     * }
     * ```
     */
    fun short() {
        hour = HourFormat.Auto.Numeric
        minute = MinuteFormat.Padded
    }

    /**
     * Applies a full time format.
     *
     * This typically includes an automatic hour format, a padded minute, and seconds.
     *
     * Example:
     * ```kotlin
     * val format = TimeFormat {
     *     full()
     * }
     * ```
     */
    fun full() {
        hour = HourFormat.Auto.Numeric
        minute = MinuteFormat.Padded
        second = SecondFormat.Padded
    }
}

/**
 * Creates a [TimeFormat] using a DSL.
 *
 * This function provides a convenient way to construct a [TimeFormat] instance
 * by applying a configuration block to a [TimeFormatBuilder].
 *
 * Example:
 * ```kotlin
 * val format = TimeFormat {
 *     hour = HourFormat.Auto.Numeric
 *     minute = MinuteFormat.Padded
 * }
 * ```
 *
 * @param block The configuration block for the [TimeFormatBuilder].
 * @return The newly created [TimeFormat].
 */
fun TimeFormat(
    block: TimeFormatBuilder.() -> Unit
): TimeFormat = TimeFormatBuilder().apply(block).build()
