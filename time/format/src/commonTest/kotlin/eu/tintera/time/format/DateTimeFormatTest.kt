package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateTimeFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = DateTimeFormatBuilder().build()
        // Date format default
        assertNull(format.weekDay)
        assertNull(format.day)
        assertNull(format.month)
        assertNull(format.year)
        // Time format default
        assertNull(format.hour)
        assertNull(format.minute)
        assertNull(format.second)
        assertNull(format.fractionalSecond)
        assertNull(format.periodStyle)
    }

    @Test
    fun testBuilderDsl() {
        val format = DateTimeFormat {
            date {
                day = DayFormat.Numeric
                month = MonthFormat.Name.Full
                year = YearFormat.FourDigits
            }
            time {
                hour = HourFormat.Auto.Padded
                minute = MinuteFormat.Padded
            }
        }
        assertEquals(DayFormat.Numeric, format.day)
        assertEquals(MonthFormat.Name.Full, format.month)
        assertEquals(YearFormat.FourDigits, format.year)
        assertEquals(HourFormat.Auto.Padded, format.hour)
        assertEquals(MinuteFormat.Padded, format.minute)
    }

    @Test
    fun testFromCopy() {
        val original = DateTimeFormat {
            date { medium() }
            time { short() }
        }
        val copied = DateTimeFormat {
            from(original)
        }
        assertEquals(DayFormat.Numeric, copied.day)
        assertEquals(MonthFormat.Digital.Numeric, copied.month)
        assertEquals(YearFormat.FourDigits, copied.year)
        assertEquals(HourFormat.Auto.Numeric, copied.hour)
        assertEquals(MinuteFormat.Padded, copied.minute)
    }
}
