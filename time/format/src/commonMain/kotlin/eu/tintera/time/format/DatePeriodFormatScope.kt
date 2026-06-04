package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker
import kotlinx.datetime.DatePeriod

@TimeDslMarker
class DatePeriodFormatScope internal constructor(
    override val value: DatePeriod,
    override val locale: AppLocale
) : FormatScope<DatePeriod> {

    var years: UnitVisibility? = null

    var months: UnitVisibility? = null

    var days: UnitVisibility? = null


    fun from(date: DatePeriodFormat) {
        date.block(this)
    }

    fun full(
        visibility: UnitVisibility
    ) {
        years = visibility
        months = visibility
        days = visibility
    }

    fun fullAuto() = full(UnitVisibility.Auto)

    fun fullRequired() = full(UnitVisibility.Required)

    fun isEmpty() = years == null && months == null && days == null


    companion object {
        /**
         * The default configuration block for [DatePeriodFormatScope].
         *
         * By default, it sets all calendar components to auto visibility (e.g. [UnitVisibility.Auto]).
         *
         * Example:
         * ```kotlin
         * val config = DatePeriodFormatScope.defaultConfig
         * ```
         */
        val defaultConfig: DatePeriodFormatScope.() -> Unit = {
            fullAuto()
        }
    }

}