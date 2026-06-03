package eu.tintera.time.core

/**
 * Defines the direction for sequence generation of intervals.
 *
 * Example:
 * ```kotlin
 * val direction = SequenceDirection.Forward
 * ```
 */
enum class SequenceDirection {
    /**
     * Generates intervals moving forward in time.
     */
    Forward,

    /**
     * Generates intervals moving backward in time.
     */
    Backward
}