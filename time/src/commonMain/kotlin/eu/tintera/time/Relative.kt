package eu.tintera.time

import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Defines the style of relative time units used for formatting.
 */
enum class RelativeUnitStyle {
    /**
     * Full textual representation of relative time (e.g., "5 minutes ago", "yesterday").
     */
    Full,

    /**
     * Shortened textual representation of relative time (e.g., "5 mins ago").
     */
    Short,

    /**
     * Extremely shortened textual representation of relative time, suitable for small displays (e.g., "5m ago").
     */
    Narrow
}

/**
 * Platform-specific implementation for formatting relative time.
 *
 * @param target The target [Instant] to format.
 * @param now The reference [Instant] representing the current time.
 * @param style The desired [RelativeUnitStyle].
 * @return A string representing the relative time difference.
 */
internal expect fun formatRelativeTime(
    target: Instant,
    now: Instant,
    style: RelativeUnitStyle
): String
