package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

/**
 * Configuration that determines the visibility and formatting of clock-based time components
 * (hours, minutes, seconds).
 *
 * Example:
 * ```kotlin
 * val components = ClockComponents {
 *     hours = UnitVisibility.Always
 *     minutes = UnitVisibility.IfNotEmpty
 * }
 * ```
 */
interface ClockComponents {
    /**
     * Visibility setting for the hours unit. If null, the hours unit is omitted from formatting.
     */
    val hours: UnitVisibility?

    /**
     * Visibility setting for the minutes unit. If null, the minutes unit is omitted from formatting.
     */
    val minutes: UnitVisibility?

    /**
     * Visibility setting for the seconds unit. If null, the seconds unit is omitted from formatting.
     */
    val seconds: UnitVisibility?
}

internal data class ClockComponentsImpl(
    override val hours: UnitVisibility? = null,
    override val minutes: UnitVisibility? = null,
    override val seconds: UnitVisibility? = null
) : ClockComponents

/**
 * Builder for constructing [ClockComponents] configurations using a DSL style.
 *
 * Example:
 * ```kotlin
 * val builder = ClockComponentsBuilder().apply {
 *     hours = UnitVisibility.Always
 * }
 * val components = builder.build()
 * ```
 */
@TimeDslMarker
open class ClockComponentsBuilder internal constructor() {
    /**
     * Visibility setting for the hours unit.
     */
    var hours: UnitVisibility? = null

    /**
     * Visibility setting for the minutes unit.
     */
    var minutes: UnitVisibility? = null

    /**
     * Visibility setting for the seconds unit.
     */
    var seconds: UnitVisibility? = null

    /**
     * Copies settings from another [ClockComponents] instance.
     *
     * Example:
     * ```kotlin
     * val source = ClockComponents { hours = UnitVisibility.Always }
     * val builder = ClockComponentsBuilder().apply {
      *     from(source)
      * }
     * ```
     *
     * @param clock The source clock components configuration to copy.
     */
    fun from(clock: ClockComponents) {
        hours = clock.hours
        minutes = clock.minutes
        seconds = clock.seconds
    }

    /**
     * Builds and returns a [ClockComponents] instance based on the current builder state.
     *
     * Example:
     * ```kotlin
     * val builder = ClockComponentsBuilder()
     * val components = builder.build()
     * ```
     *
     * @return The configured [ClockComponents].
     */
    open fun build(): ClockComponents = ClockComponentsImpl(
        hours = hours,
        minutes = minutes,
        seconds = seconds
    )
}

/**
 * Creates a new [ClockComponents] configuration using a DSL configuration block.
 *
 * Example:
 * ```kotlin
 * val components = ClockComponents {
 *     seconds = UnitVisibility.Always
 * }
 * ```
 *
 * @param block The configuration block applied to the [ClockComponentsBuilder].
 * @return The configured [ClockComponents] instance.
 */
fun ClockComponents(
    block: ClockComponentsBuilder.() -> Unit
): ClockComponents = ClockComponentsBuilder().apply(block).build()