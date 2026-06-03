package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker

@TimeDslMarker
class RelativeDateTimeFormatScope internal constructor(
    override val value: RelativeValues,
    override val locale: AppLocale
) : FormatScope<RelativeValues> {
    /** The style to use for formatting. Defaults to [FormatStyle.Full]. */
    var style: FormatStyle = FormatStyle.Full

    val display: RelativeDisplay = RelativeDisplay.Idiomatic

    /** Threshold for formatting differences in years. */
    var years: UnitThreshold? = null

    /** Threshold for formatting differences in months. */
    var months: UnitThreshold? = null

    /** Threshold for formatting differences in days. */
    var days: UnitThreshold? = null

    /** Threshold for formatting differences in hours. */
    var hours: UnitThreshold? = null

    /** Threshold for formatting differences in minutes. */
    var minutes: UnitThreshold? = null

    /** Threshold for formatting differences in seconds. Defaults to null (disabled). */
    var seconds: UnitThreshold? = null

    /**
     * Configures all main relative units (years, months, days, hours, minutes) with a default threshold of 1.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     full()
     * }
     * ```
     */
    fun full() {
        years = UnitThreshold(1)
        months = UnitThreshold(1)
        days = UnitThreshold(1)
        hours = UnitThreshold(1)
        minutes = UnitThreshold(1)
        seconds = null
    }

    /**
     * Configures the threshold for the years unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     years(2)
     * }
     * ```
     *
     * @param min The minimum number of years, or null to disable relative years.
     */
    fun years(min: Int? = 1) {
        years = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the months unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     months(2)
     * }
     * ```
     *
     * @param min The minimum number of months, or null to disable relative months.
     */
    fun months(min: Int? = 1) {
        months = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the days unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     days(2)
     * }
     * ```
     *
     * @param min The minimum number of days, or null to disable relative days.
     */
    fun days(min: Int? = 1) {
        days = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the hours unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     hours(2)
     * }
     * ```
     *
     * @param min The minimum number of hours, or null to disable relative hours.
     */
    fun hours(min: Int? = 1) {
        hours = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the minutes unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     minutes(2)
     * }
     * ```
     *
     * @param min The minimum number of minutes, or null to disable relative minutes.
     */
    fun minutes(min: Int? = 1) {
        minutes = min?.let { UnitThresholdImpl(min) }
    }

    /**
     * Configures the threshold for the seconds unit.
     *
     * Example:
     * ```kotlin
     * val format = RelativeDateTimeFormat {
     *     seconds(10)
     * }
     * ```
     *
     * @param min The minimum number of seconds, or null to disable relative seconds.
     */
    fun seconds(min: Int? = 1) {
        seconds = min?.let { UnitThresholdImpl(min) }
    }


    companion object {
        /**
         * The default configuration block for [RelativeDateTimeFormatScope].
         *
         * By default, it enables all relative units (years, months, days, hours, minutes) with a threshold of 1.
         *
         * Example:
         * ```kotlin
         * val config = RelativeDateTimeFormatScope.defaultConfig
         * ```
         */
        val defaultConfig: RelativeDateTimeFormatScope.() -> Unit = {
            full()
        }
    }
}