package eu.tintera.time.core.context

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class LocalDateTimeModifierBuilderTest {

    private val tz = TimeZone.UTC

    @Test
    fun testModifyWithContext() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = with(tz) {
            initial.modify {
                plusYears(1)
                plusMonths(1)
                plusDays(1)
            }
        }
        assertEquals(LocalDateTime(2024, 6, 11, 12, 0, 0, 0), modified)
    }

    @Test
    fun testPlusTimeWithContext() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = with(tz) {
            initial.modify {
                plusTime(LocalTime(3, 0))
            }
        }
        assertEquals(LocalDateTime(2023, 5, 10, 15, 0, 0, 0), modified)
    }
}
