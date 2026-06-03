package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker
import eu.tintera.time.core.datePeriod
import kotlinx.datetime.DateTimePeriod

@TimeDslMarker
class DateTimePeriodFormatScope internal constructor(
    override val value: DateTimePeriod,
    override val locale: AppLocale
) : FormatScope<DateTimePeriod> {

    /**
     * The formatting style to use for units (full, short, narrow). Defaults to [FormatStyle.Full].
     */
    var style: FormatStyle = FormatStyle.Full

    /**
     * The maximum number of units to format. If null, all configured units are formatted.
     */
    var maxUnitsCount: Int? = null
    internal val calendar = DatePeriodFormatScope(value.datePeriod, locale)
    internal val clock = ClockFormatScope()

    /**
     * Sets the visibility of all calendar and clock components to the specified [unitVisibility].
     *
     * Example:
     * ```kotlin
     * val format = DateTimePeriodFormat {
     *     full(UnitVisibility.Always)
     * }
     * ```
     *
     * @param unitVisibility The visibility setting to apply to all calendar and clock components.
     */
    fun full(unitVisibility: UnitVisibility) {
        calendar { full(unitVisibility) }
        clock { full(unitVisibility) }
    }

    /**
     * Sets the visibility of all calendar and clock components to [UnitVisibility.Auto].
     *
     * Example:
     * ```kotlin
     * val format = DateTimePeriodFormat {
     *     fullAuto()
     * }
     * ```
     */
    fun fullAuto() {
        full(UnitVisibility.Auto)
    }

    /**
     * Sets the visibility of all calendar and clock components to [UnitVisibility.Required].
     *
     * Example:
     * ```kotlin
     * val format = DateTimePeriodFormat {
     *     fullRequired()
     * }
     * ```
     */
    fun fullRequired() {
        full(UnitVisibility.Required)
    }

    fun calendar(block: DatePeriodFormatScope.() -> Unit = DatePeriodFormatScope.defaultConfig) {
        calendar.block()
    }

    fun clock(block: ClockFormatScope.() -> Unit = ClockFormatScope.defaultConfig) {
        clock.block()
    }

    companion object {
        /**
         * The default configuration block for [DateTimePeriodFormatScope].
         *
         * By default, it sets all date-time period components to auto visibility (e.g. [fullAuto]).
         *
         * Example:
         * ```kotlin
         * val config = DateTimePeriodFormatScope.defaultConfig
         * ```
         */
        val defaultConfig: DateTimePeriodFormatScope.() -> Unit = {
            fullAuto()
        }
    }
}