package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CalendarComponentsTest {

    @Test
    fun testBuilderDefaultValues() {
        val components = CalendarComponentsBuilder().build()
        assertNull(components.years)
        assertNull(components.months)
        assertNull(components.days)
    }

    @Test
    fun testBuilderDsl() {
        val components = CalendarComponents {
            years = UnitVisibility.Required
            months = UnitVisibility.Auto
            days = UnitVisibility.Required
        }
        assertEquals(UnitVisibility.Required, components.years)
        assertEquals(UnitVisibility.Auto, components.months)
        assertEquals(UnitVisibility.Required, components.days)
    }

    @Test
    fun testFromCopy() {
        val original = CalendarComponents {
            years = UnitVisibility.Required
            months = UnitVisibility.Auto
        }
        val copied = CalendarComponents {
            from(original)
        }
        assertEquals(UnitVisibility.Required, copied.years)
        assertEquals(UnitVisibility.Auto, copied.months)
        assertNull(copied.days)
    }
}
