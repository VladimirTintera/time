package eu.tintera.time

import kotlinx.datetime.LocalDateTime

/**
 * Represents a configuration for formatting both date and time components.
 *
 * This interface encapsulates a [DateFormat] and a [TimeFormat], allowing for
 * comprehensive formatting of a [LocalDateTime].
 */
interface DateTimeFormat {
    /** The format for the date components, or null if omitted. */
    val dateFormat: DateFormat?
    /** The format for the time components, or null if omitted. */
    val timeFormat: TimeFormat?
}

internal fun DateTimeFormat.isEmpty() = (dateFormat?.isEmpty() ?: true) && (timeFormat?.isEmpty() ?: true)

/**
 * Implementation of [DateTimeFormat].
 */
internal data class DateTimeFormatImpl(
    override val dateFormat: DateFormat?,
    override val timeFormat: TimeFormat?
) : DateTimeFormat

/**
 * Converts the format to a CLDR skeleton string.
 */
internal fun DateTimeFormat.toCldrSkeleton(): String = buildString {
    dateFormat?.toCldrSkeleton()?.let { append(it) }
    timeFormat?.toCldrSkeleton()?.let { append(it) }
}

/**
 * Builder for creating [DateTimeFormat] instances using a DSL.
 *
 * This builder provides a structured way to construct a [DateTimeFormat] by
 * independently configuring its date and time components.
 */
@DateTimeDslMarker
class DateTimeFormatBuilder {
    internal var dateFormat: DateFormat? = null
    internal var timeFormat: TimeFormat? = null

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
        dateFormat = dateTimeFormat.dateFormat
        timeFormat = dateTimeFormat.timeFormat
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

        if (format.isEmpty()) {
            throw IllegalArgumentException(
                "DateTimeFormat cannot be empty. You must configure at least one date or time component " +
                        "(e.g., call dateFormat { ... } or timeFormat { ... } with valid properties)."
            )
        }

        return format
    }
}

/**
 * Creates a [DateTimeFormat] using a DSL.
 *
 * This function provides a convenient way to construct a [DateTimeFormat] instance
 * by applying a configuration block to a [DateTimeFormatBuilder].
 *
 * @param base An optional base [DateTimeFormat] to build upon.
 * @param block The configuration block for the [DateTimeFormatBuilder].
 * @return The newly created [DateTimeFormat].
 */
fun dateTimeFormat(
    base: DateTimeFormat? = null,
    block: DateTimeFormatBuilder.() -> Unit = {}
): DateTimeFormat {
    val builder = DateTimeFormatBuilder()
    base?.also { builder.from(it) }
    return builder.apply(block).build()
}


/**
 * Platform-specific implementation for formatting a [LocalDateTime].
 */
internal expect fun formatDateTime(
    date: LocalDateTime,
    format: DateTimeFormat
): String
