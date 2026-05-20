package eu.tintera.time

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

internal actual fun formatDateTime(
    date: LocalDateTime,
    format: DateTimeFormat
): String {
    val d = date.toInstant(TimeZone.currentSystemDefault()).toNSDate()
    val nsTemplate = NSDateFormatter.dateFormatFromTemplate(
        tmplate = format.toCldrSkeleton(),
        options = 0u,
        locale = NSLocale.currentLocale
    )

    val formatter = NSDateFormatter().apply {
        locale = NSLocale.currentLocale
        nsTemplate?.also {
            setLocalizedDateFormatFromTemplate(nsTemplate)
        }
    }

    return formatter.stringFromDate(d)
}