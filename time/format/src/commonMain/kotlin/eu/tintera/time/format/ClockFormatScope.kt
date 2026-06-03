package eu.tintera.time.format

import eu.tintera.time.core.TimeDslMarker

@TimeDslMarker
open class ClockFormatScope internal constructor() {
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
     * Copies settings from another [ClockFormat] instance.
     *
     * Example:
     * ```kotlin
     * val source = ClockFormat { hours = UnitVisibility.Required }
     * val format = ClockFormat {
     *     from(source)
     * }
     * ```
     *
     * @param clock The source clock components configuration to copy.
     */
    internal fun from(clock: ClockFormat) {
        clock.block(this)
    }

    internal fun full(unitVisibility: UnitVisibility = UnitVisibility.Auto) {
        hours = unitVisibility
        minutes = unitVisibility
        seconds = unitVisibility
    }

    internal fun fullAuto() = full(UnitVisibility.Auto)
    internal fun fullRequired() = full(UnitVisibility.Required)

    internal fun isEmpty() = hours == null && minutes == null && seconds == null

    companion object {
        /**
         * The default configuration block for [ClockFormatScope].
         *
         * By default, it sets all clock components to auto visibility (e.g. [UnitVisibility.Auto]).
         *
         * Example:
         * ```kotlin
         * val config = ClockFormatScope.defaultConfig
         * ```
         */
        val defaultConfig: ClockFormatScope.() -> Unit = {
            fullAuto()
        }
    }
}