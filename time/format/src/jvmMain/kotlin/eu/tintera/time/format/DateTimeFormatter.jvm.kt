package eu.tintera.time.format

import com.ibm.icu.text.DateTimePatternGenerator
import eu.tintera.locale.AppLocale
import kotlinx.datetime.toJavaLocalDateTime

internal actual fun createDateTimeFormatterFactory(
    locale: AppLocale
): DateTimeFormatterFactory = DateTimeFormatterFactory { skeleton, _, _ ->

    val pattern = DateTimePatternGenerator.getInstance(locale).getBestPattern(skeleton).localizedPatternFix(skeleton)
    val formatter = java.time.format.DateTimeFormatter.ofPattern(pattern, locale)

    DateTimeFormatter {
        it.toJavaLocalDateTime().format(formatter)
    }
}