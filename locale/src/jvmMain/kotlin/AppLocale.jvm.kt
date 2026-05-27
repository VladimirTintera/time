package eu.tintera.locale

import java.util.Locale

actual typealias AppLocale = Locale

actual val AppLocale.languageCode: String
    get() = this.language

actual fun localeForLangCode(code: String): Locale = Locale.of(code)
actual fun getCurrentLocale(): Locale = Locale.getDefault()