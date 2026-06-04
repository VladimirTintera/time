package eu.tintera.time.core

/**
 * Represents the direction to move when adjusting a date to a specific day of the week.
 *
 * Example:
 * ```kotlin
 * val direction = MoveDirection.Forward
 * ```
 */
enum class MoveDirection {
    /**
     * Move forward in time (to the future).
     */
    Forward,

    /**
     * Move backward in time (to the past).
     */
    Backward
}