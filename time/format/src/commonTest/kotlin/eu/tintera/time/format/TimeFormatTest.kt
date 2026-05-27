package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TimeFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = TimeFormatBuilder().build()
        assertNull(format.hour)
        assertNull(format.minute)
        assertNull(format.second)
        assertNull(format.fractionalSecond)
        assertNull(format.periodStyle)
    }

    @Test
    fun testBuilderDsl() {
        val format = TimeFormat {
            hour = HourFormat.Digital24h.Padded
            minute = MinuteFormat.Padded
            second = SecondFormat.Numeric
            fractionalSecond = FractionalSecondFormat.ThreeDigits
            periodStyle = DayPeriodStyle.Required
        }
        assertEquals(HourFormat.Digital24h.Padded, format.hour)
        assertEquals(MinuteFormat.Padded, format.minute)
        assertEquals(SecondFormat.Numeric, format.second)
        assertEquals(FractionalSecondFormat.ThreeDigits, format.fractionalSecond)
        assertEquals(DayPeriodStyle.Required, format.periodStyle)
    }

    @Test
    fun testPredefinedStyles() {
        val short = TimeFormat { short() }
        assertEquals(HourFormat.Auto.Numeric, short.hour)
        assertEquals(MinuteFormat.Padded, short.minute)
        assertNull(short.second)
        assertNull(short.fractionalSecond)

        val full = TimeFormat { full() }
        assertEquals(HourFormat.Auto.Numeric, full.hour)
        assertEquals(MinuteFormat.Padded, full.minute)
        assertEquals(SecondFormat.Padded, full.second)
        assertNull(full.fractionalSecond)
    }

    @Test
    fun testFromCopy() {
        val original = TimeFormat { full() }
        val copied = TimeFormat { from(original) }
        assertEquals(HourFormat.Auto.Numeric, copied.hour)
        assertEquals(MinuteFormat.Padded, copied.minute)
        assertEquals(SecondFormat.Padded, copied.second)
    }
}
