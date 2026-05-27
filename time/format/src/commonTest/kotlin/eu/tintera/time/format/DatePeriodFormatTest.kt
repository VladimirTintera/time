package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DatePeriodFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = DatePeriodFormatBuilder().build()
        assertEquals(FormatStyle.Full, format.style)
        assertNull(format.maxUnitsCount)
        assertNull(format.years)
        assertNull(format.months)
        assertNull(format.days)
    }

    @Test
    fun testBuilderDsl() {
        val format = DatePeriodFormat {
            style = FormatStyle.Narrow
            maxUnitsCount = 2
            years = UnitVisibility.Required
            months = UnitVisibility.Auto
        }
        assertEquals(FormatStyle.Narrow, format.style)
        assertEquals(2, format.maxUnitsCount)
        assertEquals(UnitVisibility.Required, format.years)
        assertEquals(UnitVisibility.Auto, format.months)
        assertNull(format.days)
    }
}
