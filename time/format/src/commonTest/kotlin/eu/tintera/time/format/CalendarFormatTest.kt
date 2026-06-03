package eu.tintera.time.format

import eu.tintera.locale.localeForLanguageTag
import kotlinx.datetime.DatePeriod
import kotlin.test.Test
import kotlin.test.assertEquals

class DatePeriodFormatExtraTest {

    @Test
    fun testFullFunctions() {
        val fullAuto = DatePeriodFormat {
            full(UnitVisibility.Auto)
        }
        val scope1 = DatePeriodFormatScope(DatePeriod(1, 2, 3), localeForLanguageTag("en"))
        fullAuto.block(scope1)
        assertEquals(UnitVisibility.Auto, scope1.years)
        assertEquals(UnitVisibility.Auto, scope1.months)
        assertEquals(UnitVisibility.Auto, scope1.days)

        val fullAuto2 = DatePeriodFormat {
            fullAuto()
        }
        val scope2 = DatePeriodFormatScope(DatePeriod(1, 2, 3), localeForLanguageTag("en"))
        fullAuto2.block(scope2)
        assertEquals(UnitVisibility.Auto, scope2.years)
        assertEquals(UnitVisibility.Auto, scope2.months)
        assertEquals(UnitVisibility.Auto, scope2.days)

        val fullRequired = DatePeriodFormat {
            fullRequired()
        }
        val scope3 = DatePeriodFormatScope(DatePeriod(1, 2, 3), localeForLanguageTag("en"))
        fullRequired.block(scope3)
        assertEquals(UnitVisibility.Required, scope3.years)
        assertEquals(UnitVisibility.Required, scope3.months)
        assertEquals(UnitVisibility.Required, scope3.days)
    }
}
