package eu.tintera.locale


/**
 * Represents a locale in the application.
 *
 * This expect class is mapped to target-specific platform types:
 * - JVM: `java.util.Locale`
 * - Android: `android.icu.util.ULocale`
 * - iOS/macOS (Apple): `platform.Foundation.NSLocale`
 * - Web/JS: `String` (representing language/locale tag)
 *
 * Example:
 * ```kotlin
 * val locale = localeForLanguageTag("en-US")
 * val tag = locale.languageTag
 * ```
 */
expect class AppLocale

/**
 * The language code representing the primary language of this [AppLocale].
 *
 * Returns the ISO 639-1 language code (e.g., "en", "cs").
 *
 * Example:
 * ```kotlin
 * val lang = currentLocale.languageCode
 * ```
 */
expect val AppLocale.languageCode: String

/**
 * The unique BCP 47 language tag identifying this [AppLocale].
 *
 * Returns the canonical language identifier (e.g., "en-US", "zh-Hans-HK").
 *
 * Example:
 * ```kotlin
 * val tag = currentLocale.languageTag
 * ```
 */
expect val AppLocale.languageTag: String

/**
 * The region or country code of this [AppLocale].
 *
 * Returns the ISO 3166-1 alpha-2 region/country code (e.g., "US", "CZ"),
 * or an empty string if not applicable.
 *
 * Example:
 * ```kotlin
 * val region = currentLocale.regionCode
 * ```
 */
expect val AppLocale.regionCode: String

/**
 * The display name of this [AppLocale], formatted for display to users.
 *
 * The display name is localized using the locale itself.
 *
 * Example:
 * ```kotlin
 * val name = currentLocale.displayName
 * ```
 */
expect val AppLocale.displayName: String


/**
 * Creates an [AppLocale] instance for the specified language or locale code.
 *
 * Example:
 * ```kotlin
 * val locale = localeForLanguageTag("cs-CZ")
 * ```
 *
 * @param tag The language code or locale identifier (e.g., "en", "cs", "en_US", "cs-CZ").
 * @return The corresponding [AppLocale].
 */
expect fun localeForLanguageTag(tag: String): AppLocale

/**
 * Returns the current active locale of the system or application.
 *
 * Example:
 * ```kotlin
 * val locale = currentLocale
 * ```
 *
 * @return The current default [AppLocale].
 */
expect val currentLocale: AppLocale

/**
 * Returns a list of all available locales supported on the current platform.
 *
 * Example:
 * ```kotlin
 * val locales = availableLocales()
 * ```
 *
 * @return A list of [AppLocale] instances representing available locales.
 */
expect fun availableLocales(): List<AppLocale>