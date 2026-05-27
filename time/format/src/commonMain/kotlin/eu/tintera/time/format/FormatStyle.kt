package eu.tintera.time.format

/**
 * Defines the formatting length style for names of time/date units in periods or durations.
 *
 * Example:
 * ```kotlin
 * val style = FormatStyle.Full
 * ```
 */
enum class FormatStyle {
    /**
     * Verbose formatting.
     *
     * Example: "1 den, 2 hodiny, 35 minut"
     */
    Full,

    /**
     * Abbreviated formatting.
     *
     * Example: "1 d., 2 hod., 35 min."
     */
    Short,

    /**
     * Most compact formatting.
     *
     * Example: "1d 2h 35m"
     */
    Narrow,
}