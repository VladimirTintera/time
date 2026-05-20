package eu.tintera.time

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js
import kotlin.math.abs
import kotlin.time.Instant

internal actual fun formatRelativeTime(
    target: Instant,
    now: Instant,
    style: RelativeUnitStyle
): String {
    val diffMs = target.toEpochMilliseconds() - now.toEpochMilliseconds()
    val diffSec = diffMs / 1000
    val diffMin = diffSec / 60
    val diffHour = diffMin / 60
    val diffDay = diffHour / 24

    // Vybereme nejvhodnější jednotku a hodnotu
    val (value, unit) = when {
        abs(diffDay) > 0 -> Pair(diffDay, "day")
        abs(diffHour) > 0 -> Pair(diffHour, "hour")
        abs(diffMin) > 0 -> Pair(diffMin, "minute")
        else -> Pair(diffSec, "second")
    }

    val jsStyle = when (style) {
        RelativeUnitStyle.Full -> "long"
        RelativeUnitStyle.Short -> "short"
        RelativeUnitStyle.Narrow -> "narrow"
    }

    // Zavoláme vestavěné Intl API přes kratičkou JS funkci
    return jsFormatRelative(value.toInt(), unit, jsStyle)
}

// Pomocná Wasm/JS funkce pro formátování
@OptIn(ExperimentalWasmJsInterop::class)
private fun jsFormatRelative(value: Int, unit: String, style: String): String = js(
    """
    {
        const rtf = new Intl.RelativeTimeFormat(undefined, { 
            numeric: 'auto', // Zajistí texty jako "včera", "dnes" místo "před 1 dnem"
            style: style 
        });
        return rtf.format(value, unit);
    }
"""
)