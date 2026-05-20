package eu.tintera.time

/**
 * Represents a configuration for formatting dates.
 *
 * This interface defines which components (weekday, day, month, year) should be included
 * in the formatted output and their respective styles.
 */
interface DateFormat {
    /** The format style for the day of the week, or null if omitted. */
    val weekDay: WeekDayFormat?
    /** The format style for the day of the month, or null if omitted. */
    val day: DayFormat?
    /** The format style for the month, or null if omitted. */
    val month: MonthFormat?
    /** The format style for the year, or null if omitted. */
    val year: YearFormat?
}

internal fun DateFormat.isEmpty() = weekDay == null && day == null && month == null && year == null

internal data class DateFormatImpl(
    override val weekDay: WeekDayFormat?,
    override val day: DayFormat?,
    override val month: MonthFormat?,
    override val year: YearFormat?,
) : DateFormat

internal fun DateFormat.toCldrSkeleton() = buildString {
    weekDay?.let { append(it.pattern) }
    day?.let { append(it.pattern) }
    month?.let { append(it.pattern) }
    year?.let { append(it.pattern) }
}

/**
 * Builder for creating [DateFormat] instances using a DSL.
 *
 * This builder provides a flexible way to construct a [DateFormat] by specifying
 * the desired format for each date component. It also includes predefined styles
 * for convenience.
 */
@DateTimeDslMarker
class DateFormatBuilder {
    /** The format style for the day of the week, or null if omitted. */
    var weekDay: WeekDayFormat? = null
    /** The format style for the day of the month, or null if omitted. */
    var day: DayFormat? = null
    /** The format style for the month, or null if omitted. */
    var month: MonthFormat? = null
    /** The format style for the year, or null if omitted. */
    var year: YearFormat? = null

    /**
     * Copies the configuration from an existing [DateFormat].
     *
     * This allows for easily extending or modifying a predefined format.
     *
     * @param dateFormat The format to copy from.
     */
    fun from(dateFormat: DateFormat) {
        weekDay = dateFormat.weekDay
        day = dateFormat.day
        month = dateFormat.month
        year = dateFormat.year
    }

    /**
     * Applies a short date format.
     *
     * This typically includes a padded day, a numeric month, and a two-digit year.
     */
    fun short() {
        day = DayFormat.Padded
        month = MonthFormat.Number
        year = YearFormat.TwoDigits
    }

    /**
     * Applies a medium date format.
     *
     * This typically includes a standard day, a numeric month, and a four-digit year.
     */
    fun medium() {
        day = DayFormat.Normal
        month = MonthFormat.Number
        year = YearFormat.FourDigits
    }

    /**
     * Applies a long date format.
     *
     * This typically includes a standard day, the full month name, and a four-digit year.
     * The weekday is explicitly omitted.
     */
    fun long() {
        day = DayFormat.Normal
        month = MonthFormat.FullName
        year = YearFormat.FourDigits
        weekDay = null
    }

    /**
     * Applies a full date format.
     *
     * This typically includes the full weekday name, a standard day, the full month name,
     * and a four-digit year.
     */
    fun full() {
        day = DayFormat.Normal
        month = MonthFormat.FullName
        year = YearFormat.FourDigits
        weekDay = WeekDayFormat.FullName // Tohle dělá "Full" skutečným Fullem
    }

    /**
     * Builds and returns a [DateFormat] instance.
     *
     * @return The configured [DateFormat].
     * @throws IllegalArgumentException if no date components have been configured.
     */
    fun build(): DateFormat {
        val format = DateFormatImpl(
            weekDay = weekDay,
            day = day,
            month = month,
            year = year
        )

        if (format.isEmpty()) {
            throw IllegalArgumentException(
                "TimeFormat cannot be empty. You must configure at least one date component"
            )
        }

        return format
    }
}

/**
 * Creates a [DateFormat] using a DSL.
 *
 * This function provides a convenient way to construct a [DateFormat] instance
 * by applying a configuration block to a [DateFormatBuilder].
 *
 * @param base An optional base [DateFormat] to build upon.
 * @param block The configuration block for the [DateFormatBuilder].
 * @return The newly created [DateFormat].
 */
fun date(
    base: DateFormat? = null,
    block: DateFormatBuilder.() -> Unit
): DateFormat {

    val builder = DateFormatBuilder()
    base?.also {
        builder.from(it)
    }

    return builder.apply(block).build()
}
