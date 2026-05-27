package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker


/**
 * Configuration for formatting [kotlin.time.Duration] values into localized text.
 *
 * Extends [ClockComponents] to control visibility of time components (hours, minutes, seconds).
 *
 * Example:
 * ```kotlin
 * val format = DurationFormat {
 *     width = FormatStyle.Full
 *     hours = UnitVisibility.Always
 * }
 * ```
 */
interface DurationFormat : ClockComponents {
    /**
     * The style to use for formatting unit names (e.g., "1 hour" vs "1 hr" vs "1h").
     */
    val style: FormatStyle

    /**
     * Visibility setting for the days unit. If null, the days unit is omitted from formatting.
     */
    val days: UnitVisibility?

    /**
     * Visibility setting for the fractional seconds unit. If null, fractional seconds are omitted.
     */
    val fractionalSeconds: UnitVisibility?
}

internal data class DurationFormatImpl(
    override val style: FormatStyle,
    private val clock: ClockComponents,
    override val days: UnitVisibility?,
    override val fractionalSeconds: UnitVisibility?
) : DurationFormat, ClockComponents by clock

/**
 * Builder for constructing [DurationFormat] configurations using a DSL.
 *
 * Example:
 * ```kotlin
 * val builder = DurationFormatBuilder().apply {
 *     width = FormatStyle.Short
 * }
 * val format = builder.build()
 * ```
 */
@TimeDslMarker
class DurationFormatBuilder internal constructor(): ClockComponentsBuilder() {

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

    /**
     * Builds and returns a [DurationFormat] instance based on the builder's state.
     *
     * Example:
     * ```kotlin
     * val builder = DurationFormatBuilder()
     * val format = builder.build()
     * ```
     */
    override fun build(): DurationFormat = DurationFormatImpl(
        style = style,
        clock = super.build(),
        days = days,
        fractionalSeconds = fractionalSeconds
    )
}

/**
 * Creates a [DurationFormat] instance using a DSL configuration block.
 *
 * Example:
 * ```kotlin
 * val format = DurationFormat {
 *     hours = UnitVisibility.Always
 * }
 * ```
 *
 * @param block The configuration block applied to the [DurationFormatBuilder].
 * @return The configured [DurationFormat].
 */
fun DurationFormat(
    block: DurationFormatBuilder.() -> Unit
) = DurationFormatBuilder().apply(block).build()