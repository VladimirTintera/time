package eu.tintera.locale

import web.navigator.navigator

actual typealias AppLocale = String

actual val AppLocale.languageCode: String
    get() = this.split("-").first().split("_").first()

actual fun localeForLangCode(code: String) = code
actual fun getCurrentLocale() = navigator.language