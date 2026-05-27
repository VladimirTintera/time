package eu.tintera.time.format

import com.ibm.icu.text.DisplayContext
import com.ibm.icu.text.RelativeDateTimeFormatter
import com.ibm.icu.util.ULocale
import eu.tintera.locale.AppLocale
import kotlin.math.absoluteValue

internal actual fun nativeRelativeTimeFormat(
    measurable: Measurable?,
    style: FormatStyle,
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

    val direction =
        if (measurable.value < 0) RelativeDateTimeFormatter.Direction.LAST else RelativeDateTimeFormatter.Direction.NEXT

    return formatter.format(
        measurable.value.absoluteValue.toDouble(), direction, when (measurable.unit) {
            MeasureUnit.YEARS -> RelativeDateTimeFormatter.RelativeUnit.YEARS
            MeasureUnit.MONTHS -> RelativeDateTimeFormatter.RelativeUnit.MONTHS
            MeasureUnit.DAYS -> RelativeDateTimeFormatter.RelativeUnit.DAYS
            MeasureUnit.HOURS -> RelativeDateTimeFormatter.RelativeUnit.HOURS
            MeasureUnit.MINUTES -> RelativeDateTimeFormatter.RelativeUnit.MINUTES
            MeasureUnit.SECOND -> RelativeDateTimeFormatter.RelativeUnit.SECONDS
        }
    )
}