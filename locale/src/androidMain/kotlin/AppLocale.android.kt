package eu.tintera.locale

import android.icu.util.ULocale

actual typealias AppLocale = ULocale

actual val AppLocale.languageCode: String
    get() = this.language

actual val AppLocale.displayName: String
    get() = this.getDisplayName(this)

actual val AppLocale.languageTag: String
    get() = this.toLanguageTag()

actual val AppLocale.regionCode: String
    get() = this.country

actual val currentLocale: AppLocale
    get() = ULocale.getDefault()
actual fun availableLocales(): List<AppLocale> = ULocale.getAvailableLocales().toList()

actual fun localeForLanguageTag(tag: String): AppLocale = ULocale(tag)