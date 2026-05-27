package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker


/**
 * Configuration for formatting date-time values.
 *
 * Combines [DateFormat] and [TimeFormat] to describe formatting options
 * for both date and time components.
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
 */
@TimeDslMarker
class DateTimeFormatBuilder internal constructor() {
    internal var dateFormat: DateFormat = DateFormatBuilder()
    internal var timeFormat: TimeFormat = TimeFormatBuilder()

    /**
     * Configures the date portion of the format using a DSL.
     *
     * @param block The configuration block for the [DateFormatBuilder].
     */
    fun date(block: DateFormatBuilder.() -> Unit) {
        dateFormat = DateFormatBuilder().apply(block).build()
    }

    /**
     * Configures the time portion of the format using a DSL.
     *
     * @param block The configuration block for the [TimeFormatBuilder].
     */
    fun time(block: TimeFormatBuilder.() -> Unit) {
        timeFormat = TimeFormatBuilder().apply(block).build()
    }

    /**
     * Copies the configuration from an existing [DateTimeFormat].
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
     * @return The configured [DateTimeFormat].
     * @throws IllegalArgumentException if no date or time components have been configured.
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
 * @param block The configuration block for the [DateTimeFormatBuilder].
 * @return The newly created [DateTimeFormat].
 */
fun DateTimeFormat(
    block: DateTimeFormatBuilder.() -> Unit = {}
): DateTimeFormat = DateTimeFormatBuilder().apply(block).build()