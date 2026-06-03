package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker
import kotlin.time.Duration

@TimeDslMarker
class DurationFormatScope internal constructor(
    override val value: Duration,
    override val locale: AppLocale
): ClockFormatScope(), FormatScope<Duration> {

    /**
     * The formatting style to use for unit names. Defaults to [FormatStyle.Full].
     */
    var style: FormatStyle = FormatStyle.Full

    /**
     * Visibility setting for the days unit.
     */
    var days: UnitVisibility? = null

    /**
     * Visibility setting for the fractional seconds unit.
     */
    var fractionalSeconds: UnitVisibility? = null

    /**
     * Applies a preset configuration for a full/detailed format.
     * Sets style to [FormatStyle.Full] and sets days, hours, and minutes to [UnitVisibility.Auto].
     *
     * Example:
     * ```kotlin
     * val format = DurationFormat {
     *     full()
     * }
     * ```
     */
    fun full() {
        style = FormatStyle.Full
        days = UnitVisibility.Auto
        hours = UnitVisibility.Auto
        minutes = UnitVisibility.Auto
        seconds = null
        fractionalSeconds = null
    }

    /**
     * Applies a preset configuration for a short format.
     * Sets style to [FormatStyle.Short] and sets days, hours, and minutes to [UnitVisibility.Auto].
     *
     * Example:
     * ```kotlin
     * val format = DurationFormat {
     *     short()
     * }
     * ```
     */
    fun short() {
        style = FormatStyle.Short
        days = UnitVisibility.Auto
        hours = UnitVisibility.Auto
        minutes = UnitVisibility.Auto
        seconds = null
        fractionalSeconds = null
    }


    companion object {
        /**
         * The default configuration block for [DurationFormatScope].
         *
         * By default, it sets the style to [FormatStyle.Full] and includes all components
         * with [UnitVisibility.Auto] (e.g. [full] and [fullAuto]).
         *
         * Example:
         * ```kotlin
         * val config = DurationFormatScope.defaultConfig
         * ```
         */
        val defaultConfig : DurationFormatScope.() -> Unit = {
            full()
            fullAuto()
        }
    }
}