package eu.tintera.time.format

import eu.tintera.locale.AppLocale

interface FormatScope<T: Any> {
    val value: T
    val locale: AppLocale
}