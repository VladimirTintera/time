package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Configuration for formatting calendar-based [kotlinx.datetime.DatePeriod]s.
 *
 * Extends [CalendarComponents] to control which fields (years, months, days) are displayed.
 */
interface DatePeriodFormat : CalendarComponents {
    /**
     * The style to use for formatting units (e.g., "1 year" vs "1 y.").
     */
    val style: FormatStyle

    /**
     * The maximum number of units to output. For example, if maxUnitsCount is 2,
     * "2 years, 3 months, 4 days" would be formatted as "2 years, 3 months".
     */
    val maxUnitsCount: Int?
}

internal data class DatePeriodFormatImpl(
    override val style: FormatStyle,
    private val calendar: CalendarComponents,
    override val maxUnitsCount: Int?
) : DatePeriodFormat, CalendarComponents by calendar

/**
 * Builder for constructing [DatePeriodFormat] instances using a DSL.
 */
@TimeDslMarker
class DatePeriodFormatBuilder internal constructor() : CalendarComponentsBuilder() {
    /**
     * The formatting style to use for unit names. Defaults to [FormatStyle.Full].
     */
    var width: FormatStyle = FormatStyle.Full

    /**
     * The maximum number of units to format. If null, all configured units are formatted.
     */
    var maxUnitsCount: Int? = null

    /**
     * Builds and returns a [DatePeriodFormat] instance based on the builder's state.
     */
    override fun build(): DatePeriodFormat = DatePeriodFormatImpl(
        style = width,
        calendar = super.build(),
        maxUnitsCount = maxUnitsCount
    )
}

/**
 * Creates a [DatePeriodFormat] instance using a DSL configuration block.
 *
 * @param block The configuration block applied to the [DatePeriodFormatBuilder].
 * @return The configured [DatePeriodFormat].
 */
fun DatePeriodFormat(
    block: DatePeriodFormatBuilder.() -> Unit
): DatePeriodFormat = DatePeriodFormatBuilder().apply(block).build()