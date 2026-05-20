package eu.tintera.time

import com.ibm.icu.text.DateTimePatternGenerator
import com.ibm.icu.util.ULocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

internal actual fun formatDateTime(
    date: LocalDateTime,
    format: eu.tintera.time.DateTimeFormat
): String {
    val skeleton = format.toCldrSkeleton()

    // ICU4J vygeneruje lokalizovaný pattern (stejně jako Android's getBestDateTimePattern)
    val generator = DateTimePatternGenerator.getInstance(ULocale.getDefault())
    val localizedPattern = generator.getBestPattern(skeleton)

    val formatter = DateTimeFormatter.ofPattern(localizedPattern)
    val javaDate = date.toJavaLocalDateTime()

    return javaDate.format(formatter)
}