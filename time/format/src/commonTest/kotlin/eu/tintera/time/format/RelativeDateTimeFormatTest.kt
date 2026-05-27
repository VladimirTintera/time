package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

class RelativeDateTimeFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val format = RelativeDateTimeFormat {}
        assertEquals(FormatStyle.Full, format.style)
        assertEquals(1, format.years?.min)
        assertEquals(1, format.months?.min)
        assertEquals(1, format.days?.min)
        assertEquals(1, format.hours?.min)
        assertEquals(1, format.minutes?.min)
        assertNull(format.seconds)
    }

    @Test
    fun testBuilderDsl() {
        val format = RelativeDateTimeFormat {
            style = FormatStyle.Narrow
            years(2)
            months(null) // disabled
            days(5)
            hours(null)
            minutes(10)
            seconds(30)
        }
        assertEquals(FormatStyle.Narrow, format.style)
        assertEquals(2, format.years?.min)
        assertNull(format.months)
        assertEquals(5, format.days?.min)
        assertNull(format.hours)
        assertEquals(10, format.minutes?.min)
        assertEquals(30, format.seconds?.min)
    }

    @Test
    fun testUnitThresholdFactory() {
        val threshold = UnitThreshold(5)
        assertEquals(5, threshold.min)
    }
}
