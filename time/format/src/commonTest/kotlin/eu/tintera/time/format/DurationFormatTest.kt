package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DurationFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = DurationFormatBuilder().build()
        assertEquals(FormatStyle.Full, format.style)
        assertNull(format.days)
        assertNull(format.hours)
        assertNull(format.minutes)
        assertNull(format.seconds)
        assertNull(format.fractionalSeconds)
    }

    @Test
    fun testBuilderDsl() {
        val format = DurationFormat {
            style = FormatStyle.Narrow
            days = UnitVisibility.Required
            hours = UnitVisibility.Auto
            minutes = UnitVisibility.Required
            seconds = UnitVisibility.Auto
            fractionalSeconds = UnitVisibility.Required
        }
        assertEquals(FormatStyle.Narrow, format.style)
        assertEquals(UnitVisibility.Required, format.days)
        assertEquals(UnitVisibility.Auto, format.hours)
        assertEquals(UnitVisibility.Required, format.minutes)
        assertEquals(UnitVisibility.Auto, format.seconds)
        assertEquals(UnitVisibility.Required, format.fractionalSeconds)
    }

    @Test
    fun testPresets() {
        val full = DurationFormat { full() }
        assertEquals(FormatStyle.Full, full.style)
        assertEquals(UnitVisibility.Auto, full.days)
        assertEquals(UnitVisibility.Auto, full.hours)
        assertEquals(UnitVisibility.Auto, full.minutes)
        assertNull(full.seconds)
        assertNull(full.fractionalSeconds)

        val short = DurationFormat { short() }
        assertEquals(FormatStyle.Short, short.style)
        assertEquals(UnitVisibility.Auto, short.days)
        assertEquals(UnitVisibility.Auto, short.hours)
        assertEquals(UnitVisibility.Auto, short.minutes)
        assertNull(short.seconds)
        assertNull(short.fractionalSeconds)
    }
}
