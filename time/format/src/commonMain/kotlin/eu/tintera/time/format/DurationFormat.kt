package eu.tintera.time.format

/**
 * Configuration for localized duration formatting.
 *
 * This class encapsulates a formatting DSL block used to define how [kotlin.time.Duration]
 * values are represented as strings.
 *
 * Example:
 * ```kotlin
 * val format = DurationFormat {
 *     days = UnitVisibility.Required
 *     hours = UnitVisibility.Auto
 * }
 * ```
 */
class DurationFormat internal constructor(
    val block: DurationFormatScope.() -> Unit = DurationFormatScope.defaultConfig
) {
    companion object {
        /**
         * Creates a [DurationFormat] instance with the specified configuration block.
         *
         * @param block The configuration DSL block.
         * @return A new [DurationFormat] instance.
         */
        operator fun invoke(
            block: DurationFormatScope.() -> Unit = DurationFormatScope.defaultConfig
        ): DurationFormat = DurationFormat(block)
    }
}
