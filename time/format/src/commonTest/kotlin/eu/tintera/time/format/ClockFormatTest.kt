package eu.tintera.time.format

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClockFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val scope = ClockFormatScope()
        assertNull(scope.hours)
        assertNull(scope.minutes)
        assertNull(scope.seconds)
    }

    @Test
    fun testBuilderDsl() {
        val format = ClockFormat {
            hours = UnitVisibility.Required
            minutes = UnitVisibility.Auto
            seconds = UnitVisibility.Required
        }
        val scope = ClockFormatScope()
        format.block(scope)
        assertEquals(UnitVisibility.Required, scope.hours)
        assertEquals(UnitVisibility.Auto, scope.minutes)
        assertEquals(UnitVisibility.Required, scope.seconds)
    }

    @Test
    fun testFromCopy() {
        val original = ClockFormat {
            hours = UnitVisibility.Required
            minutes = UnitVisibility.Auto
        }
        val scope = ClockFormatScope()
        scope.from(original)
        assertEquals(UnitVisibility.Required, scope.hours)
        assertEquals(UnitVisibility.Auto, scope.minutes)
        assertNull(scope.seconds)
    }
}
