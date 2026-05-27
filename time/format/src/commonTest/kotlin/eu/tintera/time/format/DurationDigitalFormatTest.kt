package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DurationDigitalFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = DurationDigitalFormatBuilder().build()
        assertNull(format.day)
        assertNull(format.hour)
        assertNull(format.minute)
        assertNull(format.second)
        assertNull(format.fractionalSecond)
        assertEquals(" ", format.separator)
    }

    @Test
    fun testBuilderDsl() {
        val format = DurationDigitalFormat {
            day = FormatStyle.Narrow
            hour = HourFormat.Digital24h.Numeric
            minute = MinuteFormat.Padded
            second = SecondFormat.Numeric
            fractionalSecond = FractionalSecondFormat.ThreeDigits
            separator = " | "
        }
        assertEquals(FormatStyle.Narrow, format.day)
        assertEquals(HourFormat.Digital24h.Numeric, format.hour)
        assertEquals(MinuteFormat.Padded, format.minute)
        assertEquals(SecondFormat.Numeric, format.second)
        assertEquals(FractionalSecondFormat.ThreeDigits, format.fractionalSecond)
        assertEquals(" | ", format.separator)
    }

    @Test
    fun testStopwatchPreset() {
        val format = DurationDigitalFormat {
            stopwatch()
        }
        assertEquals(HourFormat.Digital24h.Padded, format.hour)
        assertEquals(MinuteFormat.Padded, format.minute)
        assertEquals(SecondFormat.Padded, format.second)
        assertNull(format.day)
        assertNull(format.fractionalSecond)
    }
}
