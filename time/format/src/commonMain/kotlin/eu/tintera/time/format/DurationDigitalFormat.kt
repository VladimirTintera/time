package eu.tintera.time.format

/**
 * Configuration for digital-style duration formatting (e.g., "12:30:15", or "1 d. 12:30:15").
 *
 * Example:
 * ```kotlin
 * val format = DurationDigitalFormat {
 *     stopwatch()
 * }
 * ```
 */
class DurationDigitalFormat internal constructor(
    internal val block: DurationDigitalFormatScope.() -> Unit
) {
    companion object {
        operator fun invoke(
            block: DurationDigitalFormatScope.() -> Unit
        ): DurationDigitalFormat = DurationDigitalFormat(block)
    }
}