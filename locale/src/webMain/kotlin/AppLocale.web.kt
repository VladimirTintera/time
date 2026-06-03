package eu.tintera.locale

import js.intl.DisplayNames
import js.intl.DisplayNamesOptions
import js.intl.DisplayNamesType
import js.intl.language
import js.string.JsStrings.toKotlinString
import web.navigator.navigator
import kotlin.js.js
import kotlin.js.toJsString
import kotlin.js.toList

actual typealias AppLocale = String

actual val AppLocale.languageTag: String
    get() = this // Na webu je samotný objekt už hotovým BCP 47 tagem (např. "en-US")

actual val AppLocale.languageCode: String
    get() = this.split("-", "_").first() // Čistější parsování bez double-splitu

actual val AppLocale.regionCode: String
    get() = this.split("-", "_").getOrNull(1).orEmpty()

actual fun localeForLanguageTag(tag: String): AppLocale = tag

actual val currentLocale: AppLocale
    get() = runCatching { navigator.language }.getOrElse { "en-US" }

private val webSupportedLocales: List<String> = listOf(
    "af", "am", "ar", "as", "az", "be", "bg", "bn", "br", "bs",
    "ca", "cs", "cy", "da", "de", "de-AT", "de-CH", "el", "en", "en-AU",
    "en-CA", "en-GB", "en-IN", "en-US", "es", "es-419", "es-ES", "es-MX", "es-US", "et",
    "eu", "fa", "fi", "fil", "fo", "fr", "fr-CA", "fr-CH", "fy", "ga",
    "gd", "gl", "gu", "he", "hi", "hr", "hu", "hy", "id", "is",
    "it", "ja", "ka", "kk", "km", "kn", "ko", "ky", "lo", "lt",
    "lv", "mk", "ml", "mn", "mr", "ms", "my", "nb", "ne", "nl",
    "nl-BE", "nn", "or", "pa", "pl", "ps", "pt", "pt-BR", "pt-PT", "ro",
    "ru", "si", "sk", "sl", "sq", "sr", "sv", "sw", "ta", "te",
    "th", "tk", "tr", "uk", "ur", "uz", "vi", "zh", "zh-Hans", "zh-Hant", "zu"
)

actual fun availableLocales(): List<AppLocale> {
    return webSupportedLocales
}

// 👑 Webová magie pro lidsky čitelný název (Display Name) přes JS Intl API
actual val AppLocale.displayName: String
    get() = try {
        DisplayNames(this.toJsString(), displayNames().apply {
            this.type = DisplayNamesType.language
        }).of(this) ?: ""
    } catch (_: Exception) {
        this // Fallback na syrový tag, pokud by to starší browser nedal
    }

private fun displayNames(): DisplayNamesOptions = js("({})")