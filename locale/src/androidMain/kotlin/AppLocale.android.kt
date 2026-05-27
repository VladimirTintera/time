package eu.tintera.locale

import android.icu.util.ULocale

actual typealias AppLocale = ULocale

actual val AppLocale.languageCode: String
    get() = this.language

actual fun localeForLangCode(code: String): AppLocale = ULocale(code)
actual fun getCurrentLocale(): AppLocale = ULocale.getDefault()