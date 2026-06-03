package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.locale.currentLocale
import kotlinx.datetime.TimeZone

/**
 * Runs a block of code with a specified regional context containing [TimeZone] and [AppLocale] context parameters.
 *
 * This allows calling context-aware functions inside the block without explicitly passing or
 * establishing those context parameters elsewhere.
 *
 * Example:
 * ```kotlin
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = withRegionalContext(TimeZone.UTC, myLocale) {
 *     // context-aware formatting calls can be placed here
 * }
 * ```
 *
 * @param timeZone The time zone context to establish. Defaults to the current system default time zone.
 * @param locale The app locale context to establish. Defaults to the current active app locale.
 * @param block The context-aware block of code to execute.
 * @return The result of executing the block.
 */
inline fun <R> withRegionalContext(
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
    locale: AppLocale = currentLocale,
    block: context(TimeZone, AppLocale) () -> R
): R {
    return block(timeZone, locale)
}
