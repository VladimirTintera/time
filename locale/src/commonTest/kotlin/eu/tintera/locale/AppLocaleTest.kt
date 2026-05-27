package eu.tintera.locale

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotNull

class AppLocaleTest {

    @Test
    fun testCurrentLocale() {
        val locale = currentLocale
        assertNotNull(locale)
        assertTrue(locale.languageTag.isNotEmpty())
        assertTrue(locale.languageCode.isNotEmpty())
        assertTrue(locale.displayName.isNotEmpty())
    }

    @Test
    fun testLocaleForLanguageTag() {
        val en = localeForLanguageTag("en")
        assertEquals("en", en.languageCode)
        assertTrue(en.languageTag.startsWith("en", ignoreCase = true))

        val cz = localeForLanguageTag("cs-CZ")
        assertEquals("cs", cz.languageCode)
        assertEquals("CZ", cz.regionCode.uppercase())
        assertTrue(cz.languageTag.startsWith("cs", ignoreCase = true))
    }

    @Test
    fun testAvailableLocales() {
        val locales = availableLocales()
        assertTrue(locales.isNotEmpty())
        val validLocales = locales.filter { it.languageTag != "und" && it.languageTag.isNotEmpty() }
        assertTrue(validLocales.isNotEmpty())
        for (locale in validLocales.take(10)) {
            assertTrue(locale.languageTag.isNotEmpty())
            assertTrue(locale.languageCode.isNotEmpty())
        }
    }
}
