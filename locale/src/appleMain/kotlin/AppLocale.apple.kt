package eu.tintera.locale

import platform.Foundation.*
import platform.Foundation.languageCode

actual typealias AppLocale = NSLocale

actual val AppLocale.languageTag: String
    get() = NSLocale.canonicalLanguageIdentifierFromString(this.localeIdentifier)
// ^ Tohle zaručí, že z "en_US" dostaneš standardní "en-US"

actual val AppLocale.languageCode: String
    get() = this.languageCode // nativní property NSLocale

actual val AppLocale.regionCode: String
    get() = this.countryCode ?: "" // nativní property NSLocale

actual fun localeForLanguageTag(tag: String) : AppLocale = NSLocale(localeIdentifier = tag)

actual val currentLocale: AppLocale
    get() = NSLocale.currentLocale

actual val AppLocale.displayName: String
    get() = this.displayNameForKey(
        key = NSLocaleIdentifier,
        value = this.localeIdentifier
    ) ?: this.localeIdentifier

actual fun availableLocales(): List<AppLocale> {
    return NSLocale.availableLocaleIdentifiers.map { id ->
        NSLocale((id as NSString).toString())
    }
}