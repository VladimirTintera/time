package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker
import kotlin.time.Duration

@TimeDslMarker
class DurationDigitalFormatScope internal constructor(
    override val value: Duration,
    override val locale: AppLocale
) : FormatScope<Duration> {
    /** The style to format the days component. */
    var day: FormatStyle? = null

    /** The style to format the hours component. */
    var hour: HourFormat.Digital24h? = null

    /** The style to format the minutes component. */
    var minute: MinuteFormat? = null

    /** The style to format the seconds component. */
    var second: SecondFormat? = null

    /** The style to format the fractional seconds component. */
    var fractionalSecond: FractionalSecondFormat? = null

    /** The separator between days and time. Defaults to " ". */
    var separator: String = " "

    /**
     * Configures the builder for standard stopwatch formatting.
     * Sets hours, minutes, and seconds to padded formats (e.g., "01:05:09").
     *
     * Example:
     * ```kotlin
     * val format = DurationDigitalFormat {
     *     stopwatch()
     * }
     * ```
     */
    fun stopwatch() {
        hour = HourFormat.Digital24h.Padded
        minute = MinuteFormat.Padded
        second = SecondFormat.Padded
    }

    companion object {
        /**
         * The default configuration block for [DurationDigitalFormatScope].
         *
         * By default, it sets the stopwatch format (e.g. [stopwatch]).
         *
         * Example:
         * ```kotlin
         * val config = DurationDigitalFormatScope.defaultConfig
         * ```
         */
        val defaultConfig: DurationDigitalFormatScope.() -> Unit = {
            stopwatch()
        }
    }
}