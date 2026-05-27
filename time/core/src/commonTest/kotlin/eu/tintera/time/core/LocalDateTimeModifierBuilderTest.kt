package eu.tintera.time.core

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.DateTimePeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

class LocalDateTimeModifierBuilderTest {

    @Test
    fun testModifyBasic() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.UTC) {
            plusYears(2)
            plusMonths(3)
            plusDays(5)
            withTime(15, 30, 45, 123)
        }
        assertEquals(LocalDateTime(2025, 8, 15, 15, 30, 45, 123), modified)
    }

    @Test
    fun testModifyMinusAndWithDate() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.UTC) {
            minusMonths(2)
            minusDays(5)
            withDate(2024, 12, 25)
        }
        // Note: withDate overrides previous date modifications
        assertEquals(LocalDateTime(2024, 12, 25, 12, 0, 0, 0), modified)
    }

    @Test
    fun testModifyMinusYears() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.UTC) {
            minusYears(3)
        }
        assertEquals(LocalDateTime(2020, 5, 10, 12, 0, 0, 0), modified)
    }

    @Test
    fun testPlusTime() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.UTC) {
            plusTime(kotlinx.datetime.LocalTime(3, 0))
        }
        assertEquals(LocalDateTime(2023, 5, 10, 15, 0, 0, 0), modified)
    }

    @Test
    fun testPlusDuration() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.UTC) {
            plusDuration(2.days + 5.hours)
        }
        assertEquals(LocalDateTime(2023, 5, 12, 17, 0, 0, 0), modified)
    }

    @Test
    fun testPlusPeriod() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.UTC) {
            plus(DateTimePeriod(years = 1, months = 1, days = 1, hours = 1))
        }
        assertEquals(LocalDateTime(2024, 6, 11, 13, 0, 0, 0), modified)
    }

    @Test
    fun testTimeZoneChange() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0, 0, 0)
        val modified = initial.modify(TimeZone.of("Europe/Prague")) {
            timeZone(TimeZone.UTC)
            plusDuration(5.hours)
        }
        // Since duration addition uses UTC, 12:00 + 5 hours = 17:00
        assertEquals(LocalDateTime(2023, 5, 10, 17, 0, 0, 0), modified)
    }
}
