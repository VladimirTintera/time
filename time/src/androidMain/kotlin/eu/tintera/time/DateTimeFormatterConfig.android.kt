package eu.tintera.time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


internal actual fun formatDateTime(
    date: LocalDateTime,
    format: DateTimeFormat
): String {
    val pattern = android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), format.toCldrSkeleton())
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return date.toJavaLocalDateTime().format(formatter)
}