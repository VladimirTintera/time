package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker


/**
 * Configuration for formatting date-time values.
 *
 * Combines [DateFormat] and [TimeFormat] to describe formatting options
 * for both date and time components.
 *
 * Example:
 * ```kotlin
 * val format = DateTimeFormat {
 *     date {
 *         year = YearFormat.FourDigits
 *     }
 *     time {
 *         hour = HourFormat.Digital24h.Padded
 *     }
 * }
 * ```
 */
interface DateTimeFormat : DateFormat, TimeFormat

internal fun cldrSkeleton(
    dateFormat: DateFormat?,
    timeFormat: TimeFormat?
): String = buildString {
    dateFormat?.toDateCldrSkeleton()?.takeIf { it.isNotEmpty() }?.also { append(it) }
    timeFormat?.toTimeCldrSkeleton()?.takeIf { it.isNotEmpty() }?.also { append(it) }
}

/**
 * Implementation of [DateTimeFormat].
 */
internal data class DateTimeFormatImpl(
    val dateFormat: DateFormat,
    val timeFormat: TimeFormat
) : DateTimeFormat, DateFormat by dateFormat, TimeFormat by timeFormat

/**
 * Builder for creating [DateTimeFormat] instances using a DSL.
 *
 * This builder provides a structured way to construct a [DateTimeFormat] by
 * independently configuring its date and time components.
 *
 * Example:
 * ```kotlin
 * val builder = DateTimeFormatBuilder().apply {
 *     date { year = YearFormat.FourDigits }
 * }
 * val format = builder.build()
 * ```
 */
@TimeDslMarker
class DateTimeFormatBuilder internal constructor() {
    internal var dateFormat: DateFormat = DateFormatBuilder()
    internal var timeFormat: TimeFormat = TimeFormatBuilder()

    /**
     * Configures the date portion of the format using a DSL.
     *
     * Example:
     * ```kotlin
     * val format = DateTimeFormat {
      *     date {
      *         year = YearFormat.FourDigits
      *     }
      * }
     * ```
     *
     * @param block The configuration block for the [DateFormatBuilder].
     */
    fun date(block: DateFormatBuilder.() -> Unit) {
        dateFormat = DateFormatBuilder().apply(block).build()
    }

    /**
     * Configures the time portion of the format using a DSL.
     *
     * Example:
     * ```kotlin
     * val format = DateTimeFormat {
      *     time {
      *         hour = HourFormat.Digital24h.Padded
      *     }
      * }
     * ```
     *
     * @param block The configuration block for the [TimeFormatBuilder].
     */
    fun time(block: TimeFormatBuilder.() -> Unit) {
        timeFormat = TimeFormatBuilder().apply(block).build()
    }

    /**
     * Copies the configuration from an existing [DateTimeFormat].
     *
     * Example:
     * ```kotlin
     * val source = DateTimeFormat { date { year = YearFormat.FourDigits } }
     * val builder = DateTimeFormatBuilder().apply {
     *     from(source)
     * }
     * ```
     *
     * @param dateTimeFormat The format to copy from.
     */
    fun from(
        dateTimeFormat: DateTimeFormat
     ) {
        dateFormat = dateTimeFormat
        timeFormat = dateTimeFormat
    }

    /**
     * Builds and returns a [DateTimeFormat] instance.
     *
     * Example:
     * ```kotlin
     * val builder = DateTimeFormatBuilder()
     * val format = builder.build()
     * ```
     *
     * @return The configured [DateTimeFormat].
     */
    fun build(): DateTimeFormat {
        val format = DateTimeFormatImpl(
            dateFormat = dateFormat,
            timeFormat = timeFormat
        )

        return format
    }
}

/**
 * Creates a [DateTimeFormat] using a DSL.
 *
 * This function provides a convenient way to construct a [DateTimeFormat] instance
 * by applying a configuration block to a [DateTimeFormatBuilder].
 *
 * Example:
 * ```kotlin
 * val format = DateTimeFormat {
 *     date {
 *         year = YearFormat.FourDigits
 *     }
 * }
 * ```
 *
 * @param block The configuration block for the [DateTimeFormatBuilder].
 * @return The newly created [DateTimeFormat].
 */
fun DateTimeFormat(
    block: DateTimeFormatBuilder.() -> Unit = {}
): DateTimeFormat = DateTimeFormatBuilder().apply(block).build()