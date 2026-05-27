package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateTimePeriodFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = DateTimePeriodFormatBuilder().build()
        assertEquals(FormatStyle.Full, format.style)
        assertNull(format.maxUnitsCount)
        assertNull(format.years)
        assertNull(format.months)
        assertNull(format.days)
        assertNull(format.hours)
        assertNull(format.minutes)
        assertNull(format.seconds)
    }

    @Test
    fun testBuilderDsl() {
        val format = DateTimePeriodFormat {
            style = FormatStyle.Narrow
            maxUnitsCount = 3
            calendar {
                years = UnitVisibility.Required
                days = UnitVisibility.Auto
            }
            clock {
                hours = UnitVisibility.Required
            }
        }
        assertEquals(FormatStyle.Narrow, format.style)
        assertEquals(3, format.maxUnitsCount)
        assertEquals(UnitVisibility.Required, format.years)
        assertEquals(UnitVisibility.Auto, format.days)
        assertEquals(UnitVisibility.Required, format.hours)
        assertNull(format.months)
        assertNull(format.minutes)
        assertNull(format.seconds)
    }
}
