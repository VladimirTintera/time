package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Configuration for formatting [kotlinx.datetime.DateTimePeriod]s containing both calendar and clock units.
 *
 * Extends [CalendarComponents] and [ClockComponents] to control visibility of fields.
 */
interface DateTimePeriodFormat : CalendarComponents, ClockComponents {
    /**
     * The style to use for formatting units (e.g., "1 hour" vs "1 hr" vs "1h").
     */
    val style: FormatStyle

    /**
     * The maximum number of units to output. For example, if maxUnitsCount is 2,
     * "2 years, 3 months, 4 hours" would be formatted as "2 years, 3 months".
     */
    val maxUnitsCount: Int?
}

internal data class DateTimePeriodFormatImpl(
    private val calendarComponents: CalendarComponents,
    private val clockComponents: ClockComponents,
    override val style: FormatStyle,
    override val maxUnitsCount: Int?,
) : DateTimePeriodFormat, CalendarComponents by calendarComponents, ClockComponents by clockComponents

/**
 * Builder for constructing [DateTimePeriodFormat] instances using a DSL.
 */
@TimeDslMarker
class DateTimePeriodFormatBuilder internal constructor() {

    /**
     * The formatting style to use for units (full, short, narrow). Defaults to [FormatStyle.Full].
     */
    var width: FormatStyle = FormatStyle.Full

    /**
     * The maximum number of units to format. If null, all configured units are formatted.
     */
    var maxUnitsCount: Int? = null
    private var calendar: CalendarComponents? = null
    private var clock: ClockComponents? = null

    /**
     * Configures the visibility of calendar components (years, months, days).
     *
     * @param block The configuration block applied to [CalendarComponentsBuilder].
     */
    fun calendar(block: CalendarComponentsBuilder.() -> Unit) {
        calendar = CalendarComponents(block)
    }

    /**
     * Configures the visibility of clock components (hours, minutes, seconds).
     *
     * @param block The configuration block applied to [ClockComponentsBuilder].
     */
    fun clock(block: ClockComponentsBuilder.() -> Unit) {
        clock = ClockComponents(block)
    }

    /**
     * Builds and returns a [DateTimePeriodFormat] instance based on the builder's state.
     */
    fun build(): DateTimePeriodFormat = DateTimePeriodFormatImpl(
        calendarComponents = calendar ?: CalendarComponents {},
        clockComponents = clock ?: ClockComponents {},
        style = width,
        maxUnitsCount = maxUnitsCount,
    )
}

/**
 * Creates a [DateTimePeriodFormat] instance using a DSL configuration block.
 *
 * @param block The configuration block applied to the [DateTimePeriodFormatBuilder].
 * @return The configured [DateTimePeriodFormat].
 */
fun DateTimePeriodFormat(
    block: DateTimePeriodFormatBuilder.() -> Unit
): DateTimePeriodFormat = DateTimePeriodFormatBuilder().apply(block).build()