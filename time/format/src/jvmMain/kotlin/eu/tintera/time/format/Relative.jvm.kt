package eu.tintera.time.format

import com.ibm.icu.text.DisplayContext
import com.ibm.icu.text.RelativeDateTimeFormatter
import com.ibm.icu.util.ULocale
import eu.tintera.locale.AppLocale
import kotlin.math.absoluteValue

internal actual fun nativeRelativeTimeFormat(
    measurable: Measurable?,
    style: FormatStyle,
    display: RelativeDisplay,
    locale: AppLocale
): String {

    val icuStyle = when (style) {
        FormatStyle.Full -> RelativeDateTimeFormatter.Style.LONG
        FormatStyle.Short -> RelativeDateTimeFormatter.Style.SHORT
        FormatStyle.Narrow -> RelativeDateTimeFormatter.Style.NARROW
    }

    val formatter = RelativeDateTimeFormatter.getInstance(
        ULocale.forLocale(locale),
        null,
        icuStyle,
        DisplayContext.CAPITALIZATION_NONE
    )

    if (measurable == null)
        return formatter.format(RelativeDateTimeFormatter.Direction.PLAIN, RelativeDateTimeFormatter.AbsoluteUnit.NOW)

    val value = measurable.value
    val absValue = value.absoluteValue

    // 1. Správné určení směru (Direction) s ošetřením Longů a nuly (THIS)
    val direction = when {
        value == -2 && display == RelativeDisplay.Idiomatic && measurable.unit == MeasureUnit.DAYS -> RelativeDateTimeFormatter.Direction.LAST_2
        value == 2 && display == RelativeDisplay.Idiomatic && measurable.unit == MeasureUnit.DAYS -> RelativeDateTimeFormatter.Direction.NEXT_2
        value == 0 -> RelativeDateTimeFormatter.Direction.THIS
        value < 0 -> RelativeDateTimeFormatter.Direction.LAST
        else -> RelativeDateTimeFormatter.Direction.NEXT
    }

    // 2. Bezpečné mapování jednotek na základě toho, co ICU v dané jednotce reálně podporuje
    val absoluteUnit = if (display == RelativeDisplay.Idiomatic) {
        when (measurable.unit) {
            MeasureUnit.DAYS -> if (absValue <= 2L) RelativeDateTimeFormatter.AbsoluteUnit.DAY else null // Dny umí 0, 1, 2
            MeasureUnit.MONTHS -> if (absValue <= 1L) RelativeDateTimeFormatter.AbsoluteUnit.MONTH else null // Měsíce umí jen 0, 1 (tento, příští)
            MeasureUnit.YEARS -> if (absValue <= 1L) RelativeDateTimeFormatter.AbsoluteUnit.YEAR else null // Roky umí jen 0, 1 (tento, příští)
            else -> null
        }
    } else null

    // Pokud máme shodu pro lidský výraz, vrátíme ho
    if (absoluteUnit != null) {
        val result = formatter.format(direction, absoluteUnit)
        // Pojistka: Pokud by pro nějaký exotický jazyk ICU pro NEXT_2 vrátilo prázdný řetězec,
        // propadneme bezpečně níže do číselného formátu.
        if (result?.isNotEmpty() == true) return result
    }

    return formatter.format(
        absValue.toDouble(),
        if (value < 0L) RelativeDateTimeFormatter.Direction.LAST else RelativeDateTimeFormatter.Direction.NEXT,
        when (measurable.unit) {
            MeasureUnit.YEARS -> RelativeDateTimeFormatter.RelativeUnit.YEARS
            MeasureUnit.MONTHS -> RelativeDateTimeFormatter.RelativeUnit.MONTHS
            MeasureUnit.DAYS -> RelativeDateTimeFormatter.RelativeUnit.DAYS
            MeasureUnit.HOURS -> RelativeDateTimeFormatter.RelativeUnit.HOURS
            MeasureUnit.MINUTES -> RelativeDateTimeFormatter.RelativeUnit.MINUTES
            MeasureUnit.SECONDS -> RelativeDateTimeFormatter.RelativeUnit.SECONDS
            MeasureUnit.FRACTIONAL_SECONDS -> throw UnsupportedOperationException("Unsupported unit: ${measurable.unit}")
        }
    )
}