package eu.tintera.time.format

import eu.tintera.locale.localeForLanguageTag
import kotlinx.datetime.DatePeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DatePeriodFormatTest {

    @Test
    fun testBuilderDefaultValues() {
        val scope = DatePeriodFormatScope(DatePeriod(1, 2, 3), localeForLanguageTag("en"))
        assertNull(scope.years)
        assertNull(scope.months)
        assertNull(scope.days)
    }

    @Test
    fun testBuilderDsl() {
        val format = DatePeriodFormat {
            years = UnitVisibility.Required
            months = UnitVisibility.Auto
        }
        val scope = DatePeriodFormatScope(DatePeriod(1, 2, 3), localeForLanguageTag("en"))
        format.block(scope)
        assertEquals(UnitVisibility.Required, scope.years)
        assertEquals(UnitVisibility.Auto, scope.months)
        assertNull(scope.days)
    }

    @Test
    fun testFromCopy() {
        val original = DatePeriodFormat {
            years = UnitVisibility.Required
            months = UnitVisibility.Auto
        }
        val scope = DatePeriodFormatScope(DatePeriod(1, 2, 3), localeForLanguageTag("en"))
        scope.from(original)
        assertEquals(UnitVisibility.Required, scope.years)
        assertEquals(UnitVisibility.Auto, scope.months)
        assertNull(scope.days)
    }
}
