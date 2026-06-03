package eu.tintera.time.format

import eu.tintera.locale.localeForLanguageTag
import kotlinx.datetime.DateTimePeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DateTimePeriodFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val scope = DateTimePeriodFormatScope(DateTimePeriod(1, 2, 3, 4, 5, 6), localeForLanguageTag("en"))
        assertEquals(FormatStyle.Full, scope.style)
        assertNull(scope.maxUnitsCount)
        assertNull(scope.calendar.years)
        assertNull(scope.calendar.months)
        assertNull(scope.calendar.days)
        assertNull(scope.clock.hours)
        assertNull(scope.clock.minutes)
        assertNull(scope.clock.seconds)
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
        val scope = DateTimePeriodFormatScope(DateTimePeriod(1, 2, 3, 4, 5, 6), localeForLanguageTag("en"))
        format.block(scope)
        assertEquals(FormatStyle.Narrow, scope.style)
        assertEquals(3, scope.maxUnitsCount)
        assertEquals(UnitVisibility.Required, scope.calendar.years)
        assertEquals(UnitVisibility.Auto, scope.calendar.days)
        assertEquals(UnitVisibility.Required, scope.clock.hours)
        assertNull(scope.calendar.months)
        assertNull(scope.clock.minutes)
        assertNull(scope.clock.seconds)
    }

    @Test
    fun testFullFunctions() {
        val fullAuto = DateTimePeriodFormat {
            full(UnitVisibility.Auto)
        }
        val scope1 = DateTimePeriodFormatScope(DateTimePeriod(1, 2, 3, 4, 5, 6), localeForLanguageTag("en"))
        fullAuto.block(scope1)
        assertEquals(UnitVisibility.Auto, scope1.calendar.years)
        assertEquals(UnitVisibility.Auto, scope1.calendar.months)
        assertEquals(UnitVisibility.Auto, scope1.calendar.days)
        assertEquals(UnitVisibility.Auto, scope1.clock.hours)
        assertEquals(UnitVisibility.Auto, scope1.clock.minutes)
        assertEquals(UnitVisibility.Auto, scope1.clock.seconds)

        val fullAuto2 = DateTimePeriodFormat {
            fullAuto()
        }
        val scope2 = DateTimePeriodFormatScope(DateTimePeriod(1, 2, 3, 4, 5, 6), localeForLanguageTag("en"))
        fullAuto2.block(scope2)
        assertEquals(UnitVisibility.Auto, scope2.calendar.years)
        assertEquals(UnitVisibility.Auto, scope2.calendar.months)
        assertEquals(UnitVisibility.Auto, scope2.calendar.days)
        assertEquals(UnitVisibility.Auto, scope2.clock.hours)
        assertEquals(UnitVisibility.Auto, scope2.clock.minutes)
        assertEquals(UnitVisibility.Auto, scope2.clock.seconds)

        val fullRequired = DateTimePeriodFormat {
            fullRequired()
        }
        val scope3 = DateTimePeriodFormatScope(DateTimePeriod(1, 2, 3, 4, 5, 6), localeForLanguageTag("en"))
        fullRequired.block(scope3)
        assertEquals(UnitVisibility.Required, scope3.calendar.years)
        assertEquals(UnitVisibility.Required, scope3.calendar.months)
        assertEquals(UnitVisibility.Required, scope3.calendar.days)
        assertEquals(UnitVisibility.Required, scope3.clock.hours)
        assertEquals(UnitVisibility.Required, scope3.clock.minutes)
        assertEquals(UnitVisibility.Required, scope3.clock.seconds)
    }
}
