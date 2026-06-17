package eu.tintera.time.format

/**
 * Encapsulates a formatting configuration for clock/time components of a period or duration.
 *
 * Example:
 * ```kotlin
 * val format = ClockFormat {
 *     hours = UnitVisibility.Required
 *     minutes = UnitVisibility.Auto
 * }
 * ```
 *
 * @param block The configuration block applied to [ClockFormatScope].
 */
class ClockFormat(
    internal val block: ClockFormatScope.() -> Unit
) {
    companion object {
        /**
         * Creates a [ClockFormat] using the specified configuration block.
         *
         * Example:
         * ```kotlin
         * val format = ClockFormat {
         *     hours = UnitVisibility.Always
         * }
         * ```
         *
         * @param block The configuration block applied to [ClockFormatScope].
         * @return A new [ClockFormat] instance.
         */
        operator fun invoke(
            block: ClockFormatScope.() -> Unit
        ): ClockFormat = ClockFormat(block)
    }
}