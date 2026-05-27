package eu.tintera.time.core.context

import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.days

class LocalDateTimeExtTest {

    private val tz = TimeZone.UTC

    @Test
    fun testPlusDateTimePeriodContext() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4)
        val result = with(tz) {
            ldt.plus(period)
        }
        assertEquals(LocalDateTime(2024, 3, 4, 14, 0, 0, 0), result)
    }

    @Test
    fun testPlusLocalTimeContext() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val localTime = LocalTime(5, 30, 0, 0)
        val result = with(tz) {
            ldt.plus(localTime)
        }
        assertEquals(LocalDateTime(2023, 1, 1, 15, 30, 0, 0), result)
    }

    @Test
    fun testPlusDurationContext() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val result = with(tz) {
            ldt.plus(12.hours)
        }
        assertEquals(LocalDateTime(2023, 1, 1, 22, 0, 0, 0), result)
    }

    @Test
    fun testGenerateSequenceContext() {
        val start = LocalDateTime(2023, 1, 1, 0, 0, 0, 0)
        val period = DateTimePeriod(days = 1)
        val list = with(tz) {
            start.generateSequence(period).take(2).toList()
        }
        assertEquals(2, list.size)
        assertEquals(start..<LocalDateTime(2023, 1, 2, 0, 0, 0, 0), list[0])
    }

    @Test
    fun testSliceContext() {
        val start = LocalDateTime(2023, 1, 1, 0, 0, 0, 0)
        val end = LocalDateTime(2023, 1, 3, 0, 0, 0, 0)
        val range = start..<end
        val period = DateTimePeriod(days = 1)
        val slices = with(tz) {
            range.slice(period).toList()
        }
        assertEquals(2, slices.size)
        assertEquals(LocalDateTime(2023, 1, 1, 0, 0, 0, 0)..<LocalDateTime(2023, 1, 2, 0, 0, 0, 0), slices[0])
    }

    @Test
    fun testFindIntervalContext() {
        val target = LocalDateTime(2023, 1, 2, 12, 0)
        val period = DateTimePeriod(days = 1)
        val found = with(tz) {
            target.findInterval(period)
        }
        assertNotNull(found)
        assertEquals(target, found.start)
    }

    @Test
    fun testFindTimeIntervalContext() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0)
        val duration = 1.hours
        val found = with(tz) {
            target.findTimeInterval(duration)
        }
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), found.start)
    }

    @Test
    fun testFloorToAndCeilToContext() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0)
        val period = DateTimePeriod(hours = 1)
        val floored = with(tz) { target.floorTo(period) }
        val ceiled = with(tz) { target.ceilTo(period) }
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), floored)
        assertEquals(LocalDateTime(2023, 1, 1, 11, 0, 0, 0), ceiled)
    }
}
