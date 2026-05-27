package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = DateFormatBuilder().build()
        assertNull(format.weekDay)
        assertNull(format.day)
        assertNull(format.month)
        assertNull(format.year)
    }

    @Test
    fun testBuilderDsl() {
        val format = DateFormat {
            weekDay = WeekDayFormat.FullName
            day = DayFormat.Padded
            month = MonthFormat.Name.Full
            year = YearFormat.FourDigits
        }
        assertEquals(WeekDayFormat.FullName, format.weekDay)
        assertEquals(DayFormat.Padded, format.day)
        assertEquals(MonthFormat.Name.Full, format.month)
        assertEquals(YearFormat.FourDigits, format.year)
    }

    @Test
    fun testPredefinedStyles() {
        val short = DateFormat { short() }
        assertEquals(DayFormat.Padded, short.day)
        assertEquals(MonthFormat.Digital.Numeric, short.month)
        assertEquals(YearFormat.TwoDigits, short.year)
        assertNull(short.weekDay)

        val medium = DateFormat { medium() }
        assertEquals(DayFormat.Numeric, medium.day)
        assertEquals(MonthFormat.Digital.Numeric, medium.month)
        assertEquals(YearFormat.FourDigits, medium.year)
        assertNull(medium.weekDay)

        val long = DateFormat { long() }
        assertEquals(DayFormat.Numeric, long.day)
        assertEquals(MonthFormat.Name.Full, long.month)
        assertEquals(YearFormat.FourDigits, long.year)
        assertNull(long.weekDay)

        val full = DateFormat { full() }
        assertEquals(DayFormat.Numeric, full.day)
        assertEquals(MonthFormat.Name.Full, full.month)
        assertEquals(YearFormat.FourDigits, full.year)
        assertEquals(WeekDayFormat.FullName, full.weekDay)
    }

    @Test
    fun testFromCopy() {
        val original = DateFormat { short() }
        val copied = DateFormat { from(original) }
        assertEquals(DayFormat.Padded, copied.day)
        assertEquals(MonthFormat.Digital.Numeric, copied.month)
        assertEquals(YearFormat.TwoDigits, copied.year)
        assertNull(copied.weekDay)
    }
}
