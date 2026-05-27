package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Represents a configuration for formatting dates.
 *
 * This interface defines which components (weekday, day, month, year) should be included
 * in the formatted output and their respective styles.
 *
 * Example:
 * ```kotlin
 * val format = DateFormat {
 *     year = YearFormat.FourDigits
 *     month = MonthFormat.Name.Full
 * }
 * ```
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

internal fun DateFormat.toDateCldrSkeleton() = buildString {
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
 *
 * Example:
 * ```kotlin
 * val builder = DateFormatBuilder().apply {
 *     year = YearFormat.FourDigits
 * }
 * val format = builder.build()
 * ```
 */
@TimeDslMarker
class DateFormatBuilder internal constructor() : DateFormat {
    /** The format style for the day of the week, or null if omitted. */
    override var weekDay: WeekDayFormat? = null

    /** The format style for the day of the month, or null if omitted. */
    override var day: DayFormat? = null

    /** The format style for the month, or null if omitted. */
    override var month: MonthFormat? = null

    /** The format style for the year, or null if omitted. */
    override var year: YearFormat? = null

    /**
     * Copies the configuration from an existing [DateFormat].
     *
     * This allows for easily extending or modifying a predefined format.
     *
     * Example:
     * ```kotlin
     * val source = DateFormat { year = YearFormat.FourDigits }
     * val builder = DateFormatBuilder().apply {
     *     from(source)
     * }
     * ```
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
     *
     * Example:
     * ```kotlin
     * val builder = DateFormatBuilder().apply {
     *     short()
     * }
     * ```
     */
    fun short() {
        day = DayFormat.Padded
        month = MonthFormat.Digital.Numeric
        year = YearFormat.TwoDigits
    }

    /**
     * Applies a medium date format.
     *
     * This typically includes a standard day, a numeric month, and a four-digit year.
     *
     * Example:
     * ```kotlin
     * val builder = DateFormatBuilder().apply {
     *     medium()
     * }
     * ```
     */
    fun medium() {
        day = DayFormat.Numeric
        month = MonthFormat.Digital.Numeric
        year = YearFormat.FourDigits
    }

    /**
     * Applies a long date format.
     *
     * This typically includes a standard day, the full month name, and a four-digit year.
     * The weekday is explicitly omitted.
     *
     * Example:
     * ```kotlin
     * val builder = DateFormatBuilder().apply {
     *     long()
     * }
     * ```
     */
    fun long() {
        day = DayFormat.Numeric
        month = MonthFormat.Name.Full
        year = YearFormat.FourDigits
        weekDay = null
    }

    /**
     * Applies a full date format.
     *
     * This typically includes the full weekday name, a standard day, the full month name,
     * and a four-digit year.
     *
     * Example:
     * ```kotlin
     * val builder = DateFormatBuilder().apply {
     *     full()
     * }
     * ```
     */
    fun full() {
        day = DayFormat.Numeric
        month = MonthFormat.Name.Full
        year = YearFormat.FourDigits
        weekDay = WeekDayFormat.FullName // Tohle dělá "Full" skutečným Fullem
    }

    /**
     * Builds and returns a [DateFormat] instance.
     *
     * Example:
     * ```kotlin
     * val builder = DateFormatBuilder().apply {
     *     year = YearFormat.FourDigits
     * }
     * val format = builder.build()
     * ```
     *
     * @return The configured [DateFormat].
     */
    fun build(): DateFormat = this
}

/**
 * Creates a [DateFormat] using a DSL.
 *
 * This function provides a convenient way to construct a [DateFormat] instance
 * by applying a configuration block to a [DateFormatBuilder].
 *
 * Example:
 * ```kotlin
 * val format = DateFormat {
 *     year = YearFormat.FourDigits
 * }
 * ```
 *
 * @param block The configuration block for the [DateFormatBuilder].
 * @return The newly created [DateFormat].
 */
fun DateFormat(
    block: DateFormatBuilder.() -> Unit
): DateFormat = DateFormatBuilder().apply(block).build()
