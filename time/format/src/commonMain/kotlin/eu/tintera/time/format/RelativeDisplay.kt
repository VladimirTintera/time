package eu.tintera.time.format

/**
 * Defines the style of relative time formatting (e.g., idiomatic "yesterday" vs numeric "1 day ago").
 *
 * Example:
 * ```kotlin
 * val display = RelativeDisplay.Idiomatic
 * ```
 */
enum class RelativeDisplay {
    /**
     * Use idiomatic/relative terms (e.g., "yesterday", "tomorrow", "today") if available.
     *
     * Example: "yesterday"
     */
    Idiomatic,

    /**
     * Use numeric terms (e.g., "1 day ago", "in 1 day").
     *
     * Example: "1 day ago"
     */
    Numeric
}