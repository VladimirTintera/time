package eu.tintera.time


import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.*
import kotlin.time.Instant

@OptIn(ExperimentalForeignApi::class)
internal actual fun formatRelativeTime(
    target: Instant,
    now: Instant,
    style: RelativeUnitStyle
): String {
    val formatter = NSRelativeDateTimeFormatter().apply {
        locale = NSLocale.currentLocale

        // 1. Nastavení délky slov (Full, Short, Narrow)
        unitsStyle = when (style) {
            RelativeUnitStyle.Full -> NSRelativeDateTimeFormatterUnitsStyleFull
            RelativeUnitStyle.Short -> NSRelativeDateTimeFormatterUnitsStyleShort
            RelativeUnitStyle.Narrow -> NSRelativeDateTimeFormatterUnitsStyleAbbreviated // iOS používá název Abbreviated
        }

        // 2. Nastavení magických slov jako "včera" / "dnes"
        // Většinou chceme 'Named', aby to psalo "yesterday" místo "1 day ago".
        // Pokud bys chtěl striktně jen čísla, dal bys NSRelativeDateTimeFormatterStyleNumeric.
        dateTimeStyle = NSRelativeDateTimeFormatterStyleNamed
    }

    // Spočítáme rozdíl v sekundách (převod na Double pro NSTimeInterval)
    val timeInterval = (target.toEpochMilliseconds() - now.toEpochMilliseconds()) / 1000.0

    return formatter.localizedStringFromTimeInterval(timeInterval)
}