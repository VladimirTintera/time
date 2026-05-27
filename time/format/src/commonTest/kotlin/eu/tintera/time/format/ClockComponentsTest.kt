package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClockComponentsTest {

    @Test
    fun testBuilderDefaultValues() {
        val components = ClockComponentsBuilder().build()
        assertNull(components.hours)
        assertNull(components.minutes)
        assertNull(components.seconds)
    }

    @Test
    fun testBuilderDsl() {
        val components = ClockComponents {
            hours = UnitVisibility.Required
            minutes = UnitVisibility.Auto
            seconds = UnitVisibility.Required
        }
        assertEquals(UnitVisibility.Required, components.hours)
        assertEquals(UnitVisibility.Auto, components.minutes)
        assertEquals(UnitVisibility.Required, components.seconds)
    }

    @Test
    fun testFromCopy() {
        val original = ClockComponents {
            hours = UnitVisibility.Required
            minutes = UnitVisibility.Auto
        }
        val copied = ClockComponents {
            from(original)
        }
        assertEquals(UnitVisibility.Required, copied.hours)
        assertEquals(UnitVisibility.Auto, copied.minutes)
        assertNull(copied.seconds)
    }
}
