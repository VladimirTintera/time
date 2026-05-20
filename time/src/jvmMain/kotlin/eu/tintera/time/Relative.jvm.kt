package eu.tintera.time

import java.util.Locale
import kotlin.time.Instant

internal actual fun formatRelativeTime(
    target: Instant,
    now: Instant,
    style: RelativeUnitStyle
): String {
    val p = org.ocpsoft.prettytime.PrettyTime(Locale.getDefault())
    return p.format(java.util.Date(target.toEpochMilliseconds()))
}