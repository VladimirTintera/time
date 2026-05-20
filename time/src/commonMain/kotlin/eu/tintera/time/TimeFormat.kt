package eu.tintera.time

/**
 * Represents a configuration for formatting times.
 *
 * This interface defines which components (hour, minute, seconds, milliseconds, am/pm)
 * should be included in the formatted output and their respective styles.
 */
interface TimeFormat {
    /** The format style for the hour component, or null if omitted. */
    val hour: HourFormat?
    /** The format style for the minute component, or null if omitted. */
    val minute: MinuteFormat?
    /** Indicates whether to include seconds in the formatted output. */
    val includeSeconds: Boolean
    /** Indicates whether to include milliseconds in the formatted output. */
    val includeMilliseconds: Boolean
    /** Indicates whether to include the AM/PM marker in the formatted output. */
    val includeAmPm: Boolean
}

internal fun TimeFormat.isEmpty() = hour == null && minute == null && !includeSeconds

internal data class TimeFormatImpl(
    override val hour: HourFormat? = null,
    override val minute: MinuteFormat? = null,
    override val includeSeconds: Boolean = false,
    override val includeMilliseconds: Boolean = false,
    override val includeAmPm: Boolean = false
) : TimeFormat

internal fun TimeFormat.toCldrSkeleton(): String = buildString {
    hour?.let { append(it.pattern) }
    minute?.let { append(it.pattern) }
    if (includeSeconds) append("ss")
    if (includeMilliseconds) append("SSS")
    if (includeAmPm) append("a")
}

/**
 * Builder for creating [TimeFormat] instances using a DSL.
 *
 * This builder provides a flexible way to construct a [TimeFormat] by specifying
 * the desired format for each time component. It also includes predefined styles
 * for convenience.
 */
@DateTimeDslMarker
class TimeFormatBuilder {
    /** The format style for the hour component, or null if omitted. */
    var hour: HourFormat? = null
    /** The format style for the minute component, or null if omitted. */
    var minute: MinuteFormat? = null
    /** Indicates whether to include seconds in the formatted output. */
    var includeSeconds: Boolean = false
    /** Indicates whether to include milliseconds in the formatted output. */
    var includeMilliseconds: Boolean = false
    /** Indicates whether to include the AM/PM marker in the formatted output. */
    var includeAmPm: Boolean = false

    /**
     * Builds and returns a [TimeFormat] instance.
     *
     * @return The configured [TimeFormat].
     * @throws IllegalArgumentException if no time components have been configured.
     */
    fun build(): TimeFormat {
        val format = TimeFormatImpl(
            hour = hour,
            minute = minute,
            includeSeconds = includeSeconds,
            includeMilliseconds = includeMilliseconds,
            includeAmPm = includeAmPm
        )

        if (format.isEmpty()) {
            throw IllegalArgumentException(
                "TimeFormat cannot be empty. You must configure at least one time component "
            )
        }

        return format
    }

    /**
     * Copies the configuration from an existing [TimeFormat].
     *
     * This allows for easily extending or modifying a predefined format.
     *
     * @param timeFormat The format to copy from.
     */
    fun from(timeFormat: TimeFormat) {
        hour = timeFormat.hour
        minute = timeFormat.minute
        includeSeconds = timeFormat.includeSeconds
        includeMilliseconds = timeFormat.includeMilliseconds
        includeAmPm = timeFormat.includeAmPm
    }

    /**
     * Applies a short time format.
     *
     * This typically includes an automatic hour format and a padded minute.
     */
    fun short() {
        hour = HourFormat.Auto
        minute = MinuteFormat.Padded
    }

    /**
     * Applies a full time format.
     *
     * This typically includes an automatic hour format, a padded minute, and seconds.
     */
    fun full() {
        hour = HourFormat.Auto
        minute = MinuteFormat.Padded
        includeSeconds = true
    }
}

/**
 * Creates a [TimeFormat] using a DSL.
 *
 * This function provides a convenient way to construct a [TimeFormat] instance
 * by applying a configuration block to a [TimeFormatBuilder].
 *
 * @param base An optional base [TimeFormat] to build upon.
 * @param block The configuration block for the [TimeFormatBuilder].
 * @return The newly created [TimeFormat].
 */
fun time(
    base: TimeFormat? = null,
    block: TimeFormatBuilder.() -> Unit
): TimeFormat {
    val builder = TimeFormatBuilder()
    base?.also { builder.from(it) }
    return builder.apply(block).build()
}
