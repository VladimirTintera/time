package eu.tintera.locale

import java.util.Locale

actual typealias AppLocale = Locale

actual val AppLocale.languageCode: String
    get() = this.language

actual val AppLocale.displayName: String
    get() = this.getDisplayName(this)

actual val AppLocale.languageTag: String
    get() = this.toLanguageTag()

actual val AppLocale.regionCode: String
    get() = this.country

actual val currentLocale: AppLocale
    get() = Locale.getDefault()
actual fun availableLocales(): List<AppLocale> = Locale.getAvailableLocales().toList()

actual fun localeForLanguageTag(tag: String): AppLocale = Locale.forLanguageTag(tag.replace("_", "-"))