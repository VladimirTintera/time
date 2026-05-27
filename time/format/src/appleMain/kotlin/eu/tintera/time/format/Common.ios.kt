package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.cinterop.UnsafeNumber
import platform.Foundation.NSDateFormatter

@OptIn(UnsafeNumber::class)
internal fun String.toDateTimeTemplate(locale: AppLocale) = NSDateFormatter.dateFormatFromTemplate(
    tmplate = this,
    options = 0u,
    locale = locale
)