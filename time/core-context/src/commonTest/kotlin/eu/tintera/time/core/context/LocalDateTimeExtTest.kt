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
    fun testGenerateSequenceWithDirectionContext() {
        val start = LocalDateTime(2023, 1, 1, 0, 0, 0, 0)
        val period = DateTimePeriod(days = 1)
        val listForward = with(tz) {
            start.generateSequence(eu.tintera.time.core.SequenceDirection.Forward, period).take(2).toList()
        }
        assertEquals(2, listForward.size)
        assertEquals(start..<LocalDateTime(2023, 1, 2, 0, 0, 0, 0), listForward[0])

        val listBackward = with(tz) {
            start.generateSequence(eu.tintera.time.core.SequenceDirection.Backward, period).take(2).toList()
        }
        assertEquals(2, listBackward.size)
        assertEquals(LocalDateTime(2022, 12, 31, 0, 0, 0, 0)..<start, listBackward[0])
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
        assertEquals(LocalDateTime(2023, 1, 2, 0, 0), found.start)
        assertEquals(LocalDateTime(2023, 1, 3, 0, 0), found.endExclusive)
    }

    @Test
    fun testFindTimeIntervalContext() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0)
        val duration = 1.hours
        val found = with(tz) {
            target.findTimeInterval(duration)
        }
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), found.start)

        // Test with anchor
        val anchor = LocalDateTime(2023, 1, 1, 10, 10, 0, 0)
        val foundWithAnchor = with(tz) {
            target.findTimeInterval(duration, anchor)
        }
        assertEquals(LocalDateTime(2023, 1, 1, 10, 10, 0, 0), foundWithAnchor.start)
    }

    @Test
    fun testAnchorAwareRoundingContext() {
        val target = LocalDateTime(2023, 1, 12, 12, 0)
        val period = DateTimePeriod(days = 5)
        val anchor = LocalDateTime(2023, 1, 1, 0, 0)

        val floored = with(tz) { target.floorTo(period, anchor) }
        val ceiled = with(tz) { target.ceilTo(period, anchor) }
        val rounded = with(tz) { target.roundTo(period, anchor) }

        assertEquals(LocalDateTime(2023, 1, 11, 0, 0), floored)
        assertEquals(LocalDateTime(2023, 1, 16, 0, 0), ceiled)
        assertEquals(LocalDateTime(2023, 1, 11, 0, 0), rounded)
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

    @Test
    fun testMinusDateTimePeriodContext() {
        val ldt = LocalDateTime(2023, 3, 4, 14, 0, 0, 0)
        val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4)
        val result = with(tz) {
            ldt.minus(period)
        }
        assertEquals(LocalDateTime(2022, 1, 1, 10, 0, 0, 0), result)
    }

    @Test
    fun testRoundToContext() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0)
        val period = DateTimePeriod(hours = 1)
        val rounded = with(tz) { target.roundTo(period) }
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), rounded)

        val target2 = LocalDateTime(2023, 1, 1, 10, 45, 0, 0)
        val rounded2 = with(tz) { target2.roundTo(period) }
        assertEquals(LocalDateTime(2023, 1, 1, 11, 0, 0, 0), rounded2)
    }

    @Test
    fun testModifyContext() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0)
        val modified = with(tz) {
            initial.modify {
                plusDays(2)
            }
        }
        assertEquals(LocalDateTime(2023, 5, 12, 12, 0), modified)
    }

    @Test
    fun testInvokeContext() {
        val initial = LocalDateTime(2023, 5, 10, 12, 0)
        val modified = with(tz) {
            initial {
                plusDays(2)
            }
        }
        assertEquals(LocalDateTime(2023, 5, 12, 12, 0), modified)
    }
}
