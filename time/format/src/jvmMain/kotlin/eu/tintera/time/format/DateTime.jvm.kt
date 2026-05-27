package eu.tintera.time.format

import com.ibm.icu.text.DateTimePatternGenerator
import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

internal actual fun nativeDateTimeFormat(
    date: LocalDateTime,
    locale: AppLocale,
    dateFormat: DateFormat?,
    timeFormat: TimeFormat?,
    skeleton: String
): String {

    val generator = DateTimePatternGenerator.getInstance(locale)
    val localizedPattern = generator.getBestPattern(skeleton)

    val formatter = DateTimeFormatter.ofPattern(localizedPattern)
    val javaDate = date.toJavaLocalDateTime()

    return javaDate.format(formatter)
}