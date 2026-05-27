package eu.tintera.locale


/**
 * Represents a locale in the application.
 *
 * This expect class is mapped to target-specific platform types:
 * - JVM: `java.util.Locale`
 * - Android: `android.icu.util.ULocale`
 * - iOS/macOS (Apple): `platform.Foundation.NSLocale`
 * - Web/JS: `String` (representing language/locale tag)
 */
expect class AppLocale

/**
 * The language code representing the primary language of this [AppLocale].
 *
 * Returns the ISO 639-1 language code (e.g., "en", "cs").
 */
expect val AppLocale.languageCode: String

/**
 * Creates an [AppLocale] instance for the specified language or locale code.
 *
 * @param code The language code or locale identifier (e.g., "en", "cs", "en_US", "cs-CZ").
 * @return The corresponding [AppLocale].
 */
expect fun localeForLangCode(code: String) : AppLocale

/**
 * Returns the current active locale of the system or application.
 *
 * @return The current default [AppLocale].
 */
expect fun getCurrentLocale() : AppLocale