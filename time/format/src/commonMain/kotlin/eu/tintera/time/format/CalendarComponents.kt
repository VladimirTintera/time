package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Configuration that determines the visibility and formatting of calendar-based date components
 * (years, months, days).
 */
interface CalendarComponents {
    /**
     * Visibility setting for the years unit. If null, the years unit is omitted from formatting.
     */
    val years: UnitVisibility?

    /**
     * Visibility setting for the months unit. If null, the months unit is omitted from formatting.
     */
    val months: UnitVisibility?

    /**
     * Visibility setting for the days unit. If null, the days unit is omitted from formatting.
     */
    val days: UnitVisibility?
}

internal data class CalendarComponentsImpl(
    override val years: UnitVisibility? = null,
    override val months: UnitVisibility? = null,
    override val days: UnitVisibility? = null,
) : CalendarComponents

/**
 * Builder for constructing [CalendarComponents] configurations using a DSL style.
 */
@TimeDslMarker
open class CalendarComponentsBuilder internal constructor() {

    /**
     * Visibility setting for the years unit.
     */
    var years: UnitVisibility? = null

    /**
     * Visibility setting for the months unit.
     */
    var months: UnitVisibility? = null

    /**
     * Visibility setting for the days unit.
     */
    var days: UnitVisibility? = null

    /**
     * Copies settings from another [CalendarComponents] instance.
     *
     * @param components The source components configuration to copy.
     */
    fun from(components: CalendarComponents) {
        years = components.years
        months = components.months
        days = components.days
    }

    /**
     * Builds and returns a [CalendarComponents] instance based on the current builder state.
     *
     * @return The configured [CalendarComponents].
     */
    open fun build(): CalendarComponents = CalendarComponentsImpl(
        years = years,
        months = months,
        days = days
    )
}

/**
 * Creates a new [CalendarComponents] configuration using a DSL configuration block.
 *
 * @param block The configuration block applied to the [CalendarComponentsBuilder].
 * @return The configured [CalendarComponents] instance.
 */
fun CalendarComponents(
    block: CalendarComponentsBuilder.() -> Unit
): CalendarComponents = CalendarComponentsBuilder().apply(block).build()