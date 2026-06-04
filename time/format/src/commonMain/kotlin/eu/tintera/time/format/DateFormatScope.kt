package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker

@TimeDslMarker
class DateFormatScope<T : Any> internal constructor(
    override val value: T,
    override val locale: AppLocale
) : FormatScope<T> {
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
     * Example:
     * ```kotlin
     * val source = DateFormat { year = YearFormat.FourDigits }
     * val format = DateFormat {
     *     from(source)
     * }
     * ```
     *
     * @param dateFormat The format to copy from.
     */
    fun from(dateFormat: BaseDateFormat<T>) {
        dateFormat.block(this)
    }

    /**
     * Applies a short date format.
     *
     * This typically includes a padded day, a numeric month, and a two-digit year.
     *
     * Example:
     * ```kotlin
     * val format = DateFormat {
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
     * val format = DateFormat {
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
     * val format = DateFormat {
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
     * val format = DateFormat {
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

    fun isEmpty() = weekDay == null && day == null && month == null && year == null

    fun cldrSkeleton() = buildString {
        weekDay?.let { append(it.pattern) }
        day?.let { append(it.pattern) }
        month?.let { append(it.pattern) }
        year?.let { append(it.pattern) }
    }

    companion object {
        /**
         * The default configuration block for [DateFormatScope].
         *
         * By default, it sets the date format to medium style.
         *
         * Example:
         * ```kotlin
         * val config = DateFormatScope.defaultConfig<kotlinx.datetime.LocalDate>()
         * ```
         */
        fun <T : Any> defaultConfig(): DateFormatScope<T>.() -> Unit = {
            medium()
        }
    }
}