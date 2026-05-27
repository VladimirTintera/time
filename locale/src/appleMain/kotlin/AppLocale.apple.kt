package eu.tintera.locale

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.localeWithLocaleIdentifier

actual typealias AppLocale = NSLocale

actual val AppLocale.languageCode: String
    get() = this.languageCode

actual fun localeForLangCode(code: String): AppLocale = NSLocale.localeWithLocaleIdentifier(code)
actual fun getCurrentLocale() = NSLocale.currentLocale
