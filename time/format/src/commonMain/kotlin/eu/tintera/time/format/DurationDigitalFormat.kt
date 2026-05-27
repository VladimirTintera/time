package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Configuration for digital-style duration formatting (e.g., "12:30:15", or "1 d. 12:30:15").
 */
interface DurationDigitalFormat {
    /** The style to format the days component, or null if the days component should be omitted. */
    val day: FormatStyle?

    /** The style to format the hours component, or null if the hours component should be omitted. */
    val hour: HourFormat.Digital24h?

    /** The style to format the minutes component, or null if the minutes component should be omitted. */
    val minute: MinuteFormat?

    /** The style to format the seconds component, or null if the seconds component should be omitted. */
    val second: SecondFormat?

    /** The style to format the fractional seconds component, or null if omitted. */
    val fractionalSecond: FractionalSecondFormat?

    /** The separator string between the days component and the digital time components. Defaults to " ". */
    val separator: String
}

/**
 * Builder for constructing [DurationDigitalFormat] instances using a DSL.
 */
@TimeDslMarker
class DurationDigitalFormatBuilder internal constructor() : DurationDigitalFormat {
    /** The style to format the days component. */
    override var day: FormatStyle? = null

    /** The style to format the hours component. */
    override var hour: HourFormat.Digital24h? = null

    /** The style to format the minutes component. */
    override var minute: MinuteFormat? = null

    /** The style to format the seconds component. */
    override var second: SecondFormat? = null

    /** The style to format the fractional seconds component. */
    override var fractionalSecond: FractionalSecondFormat? = null

    /** The separator between days and time. Defaults to " ". */
    override var separator: String = " "

    /**
     * Configures the builder for standard stopwatch formatting.
     * Sets hours, minutes, and seconds to padded formats (e.g., "01:05:09").
     */
    fun stopwatch() {
        hour = HourFormat.Digital24h.Padded
        minute = MinuteFormat.Padded
        second = SecondFormat.Padded
    }

    /**
     * Builds and returns a [DurationDigitalFormat] instance.
     */
    fun build(): DurationDigitalFormat = this
}

/**
 * Creates a [DurationDigitalFormat] instance using a DSL configuration block.
 *
 * @param block The configuration block applied to the [DurationDigitalFormatBuilder].
 * @return The configured [DurationDigitalFormat].
 */
fun DurationDigitalFormat(
    block: DurationDigitalFormatBuilder.() -> Unit
) = DurationDigitalFormatBuilder().apply(block).build()