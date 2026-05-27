package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.TimeZone

internal fun TimeZone.toJsTimeZoneString(
    locale: AppLocale
): String {
    // 1. Pokud je to UTC, JS striktně vyžaduje "UTC"
    if (this == TimeZone.UTC) return "UTC"

    // 2. Bezpečný fallback: zkusíme vyčistit to, co dává id
    val rawId = this.id

    // JS Intl engine je extrémně háklivý. Pokud id obsahuje zkratky (CET, EST),
    // nejlepší je zeptat se přímo runtime prohlížeče/Node.js na jeho aktuální IANA zónu.
    if (rawId == "SYSTEM" || rawId.length <= 4) {
        return js.intl.DateTimeFormat().resolvedOptions().timeZone
    }

    return try {
        js.intl.DateTimeFormat(locale, jsObject { timeZone = rawId })
        rawId // ID prošlo, můžeme ho bezpečně použít
    } catch (_: Throwable) {
        // Pokud ID selhalo (RangeError), bezpečně spadneme na zónu uživatele v prohlížeči
        js.intl.DateTimeFormat().resolvedOptions().timeZone
    }
}