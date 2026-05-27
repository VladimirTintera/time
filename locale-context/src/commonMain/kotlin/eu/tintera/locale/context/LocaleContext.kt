package eu.tintera.locale.context

import eu.tintera.locale.AppLocale
import eu.tintera.locale.getCurrentLocale

/**
 * A context that holds the active [AppLocale] for formatting operations.
 */
interface LocaleContext {
    val locale: AppLocale
}

internal data class LocaleContextImpl(
    override val locale: AppLocale
) : LocaleContext

/**
 * Creates a [LocaleContext] instance with the specified [locale].
 */
fun localeContextOf(locale: AppLocale): LocaleContext = LocaleContextImpl(locale)

/**
 * Creates a [LocaleContext] instance with the current active locale of the system.
 */
fun currentLocaleContext(): LocaleContext = localeContextOf(getCurrentLocale())
