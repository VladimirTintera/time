package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExceptionsTest {

    @Test
    fun testFormattingException() {
        val cause = RuntimeException("root cause")
        val ex = FormattingException("error message", cause)
        assertEquals("error message", ex.message)
        assertEquals(cause, ex.cause)
        assertTrue(ex is IllegalArgumentException)
    }

    @Test
    fun testEmptyFormatConfigurationException() {
        val ex = EmptyFormatConfigurationException("empty config")
        assertEquals("empty config", ex.message)
        assertTrue(ex is FormattingException)
    }
}
