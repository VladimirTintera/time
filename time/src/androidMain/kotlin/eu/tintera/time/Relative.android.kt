package eu.tintera.time

import android.text.format.DateUtils
import kotlin.time.Instant

internal actual fun formatRelativeTime(
    target: Instant,
    now: Instant,
    style: RelativeUnitStyle
): String {
    val flags = when (style) {
        RelativeUnitStyle.Full -> 0
        RelativeUnitStyle.Short, RelativeUnitStyle.Narrow -> DateUtils.FORMAT_ABBREV_RELATIVE
    }

    val result = DateUtils.getRelativeTimeSpanString(
        target.toEpochMilliseconds(),
        now.toEpochMilliseconds(),
        DateUtils.MINUTE_IN_MILLIS, // Minimální rozlišení (např. pod minutu ukáže "Před chvílí")
        flags
    )
    return result.toString()
}