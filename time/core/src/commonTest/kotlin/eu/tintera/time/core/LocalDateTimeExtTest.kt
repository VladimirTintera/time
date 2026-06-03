package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.hours

class LocalDateTimeExtTest {

    private val tz = TimeZone.UTC

    @Test
    fun testLocalDateTimePlusDateTimePeriod() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4)
        val result = ldt.plus(period, tz)
        assertEquals(LocalDateTime(2024, 3, 4, 14, 0, 0, 0), result)
    }

    @Test
    fun testLocalDateTimePlusLocalTime() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val localTime = LocalTime(5, 30, 0, 0)
        val result = ldt.plus(localTime, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 15, 30, 0, 0), result)
    }

    @Test
    fun testLocalDateTimePlusDuration() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val result = ldt.plus(12.hours, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 22, 0, 0, 0), result)
    }

    @Test
    fun testLocalDateTimePlusDatePeriodOperator() {
        val ldt = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val datePeriod = DatePeriod(years = 1, months = 1, days = 1)
        val result = ldt + datePeriod
        assertEquals(LocalDateTime(2024, 2, 2, 10, 0, 0, 0), result)
    }

    @Test
    fun testGenerateSequence() {
        val start = LocalDateTime(2023, 1, 1, 0, 0, 0, 0)
        val period = DateTimePeriod(days = 1)
        val sequence = start.generateAscendingSequence(period, tz)
        val list = sequence.take(3).toList()

        assertEquals(3, list.size)
        assertEquals(start..<LocalDateTime(2023, 1, 2, 0, 0, 0, 0), list[0])
        assertEquals(LocalDateTime(2023, 1, 2, 0, 0, 0, 0)..<LocalDateTime(2023, 1, 3, 0, 0, 0, 0), list[1])
        assertEquals(LocalDateTime(2023, 1, 3, 0, 0, 0, 0)..<LocalDateTime(2023, 1, 4, 0, 0, 0, 0), list[2])
    }

    @Test
    fun testGenerateSequenceFailsForNonProgressivePeriod() {
        val start = LocalDateTime(2023, 1, 1, 0, 0, 0, 0)
        val period = DateTimePeriod() // 0 length
        assertFailsWith<IllegalArgumentException> {
            start.generateAscendingSequence(period, tz)
        }
    }

    @Test
    fun testSlice() {
        val start = LocalDateTime(2023, 1, 1, 0, 0, 0, 0)
        val end = LocalDateTime(2023, 1, 3, 12, 0, 0, 0)
        val range = start..<end
        val period = DateTimePeriod(days = 1)
        val slices = range.slice(period, tz).toList()

        assertEquals(3, slices.size)
        assertEquals(LocalDateTime(2023, 1, 1, 0, 0, 0, 0)..<LocalDateTime(2023, 1, 2, 0, 0, 0, 0), slices[0])
        assertEquals(LocalDateTime(2023, 1, 2, 0, 0, 0, 0)..<LocalDateTime(2023, 1, 3, 0, 0, 0, 0), slices[1])
        assertEquals(LocalDateTime(2023, 1, 3, 0, 0, 0, 0)..<LocalDateTime(2023, 1, 3, 12, 0, 0, 0), slices[2])
    }

    @Test
    fun testFindIntervalFromList() {
        val intervals = listOf(
            LocalDateTime(2023, 1, 1, 0, 0)..<LocalDateTime(2023, 1, 2, 0, 0),
            LocalDateTime(2023, 1, 2, 0, 0)..<LocalDateTime(2023, 1, 3, 0, 0),
            LocalDateTime(2023, 1, 3, 0, 0)..<LocalDateTime(2023, 1, 4, 0, 0)
        )

        val target = LocalDateTime(2023, 1, 2, 12, 0)
        val found = target.findInterval(intervals)
        assertNotNull(found)
        assertEquals(intervals[1], found)

        val outOfBounds = LocalDateTime(2023, 1, 5, 0, 0)
        assertNull(outOfBounds.findInterval(intervals))
    }

    @Test
    fun testFindIntervalWithPeriod() {
        val target = LocalDateTime(2023, 1, 2, 12, 0)
        val period = DateTimePeriod(days = 1)
        val found = target.findInterval(period, tz)
        assertNotNull(found)
        // Now it correctly aligns to the default anchor (start of the month: 2023-01-01T00:00)
        assertEquals(LocalDateTime(2023, 1, 2, 0, 0), found.start)
        assertEquals(LocalDateTime(2023, 1, 3, 0, 0), found.endExclusive)
    }

    @Test
    fun testFindIntervalWithExplicitAnchor() {
        val target = LocalDateTime(2023, 1, 12, 12, 0)
        val period = DateTimePeriod(days = 5)
        val anchor = LocalDateTime(2023, 1, 1, 0, 0)
        
        // Grid: 01-01..<01-06, 01-06..<01-11, 01-11..<01-16
        val found = target.findInterval(period, anchor, tz)
        assertNotNull(found)
        assertEquals(LocalDateTime(2023, 1, 11, 0, 0), found.start)
        assertEquals(LocalDateTime(2023, 1, 16, 0, 0), found.endExclusive)

        // Test before anchor: target = 2022-12-28T12:00
        val targetBefore = LocalDateTime(2022, 12, 28, 12, 0)
        // Grid before anchor: 2022-12-27..<2023-01-01
        val foundBefore = targetBefore.findInterval(period, anchor, tz)
        assertNotNull(foundBefore)
        assertEquals(LocalDateTime(2022, 12, 27, 0, 0), foundBefore.start)
        assertEquals(LocalDateTime(2023, 1, 1, 0, 0), foundBefore.endExclusive)
    }

    @Test
    fun testCalendarPeriodRounding() {
        val target = LocalDateTime(2023, 1, 12, 12, 0)
        val period = DateTimePeriod(days = 5)
        
        // Default anchor is start of month (2023-01-01T00:00)
        // Grid: 01-01..<01-06, 01-06..<01-11, 01-11..<01-16
        val floored = target.floorTo(period, tz)
        val ceiled = target.ceilTo(period, tz)
        val rounded = target.roundTo(period, tz)

        assertEquals(LocalDateTime(2023, 1, 11, 0, 0), floored)
        assertEquals(LocalDateTime(2023, 1, 16, 0, 0), ceiled)
        assertEquals(LocalDateTime(2023, 1, 11, 0, 0), rounded)

        // Month period: months = 2
        val targetMonth = LocalDateTime(2023, 4, 15, 12, 0)
        val periodMonth = DateTimePeriod(months = 2)
        // Default anchor is start of year (2023-01-01T00:00)
        // Grid: 01-01..<03-01, 03-01..<05-01, 05-01..<07-01
        val flooredMonth = targetMonth.floorTo(periodMonth, tz)
        val ceiledMonth = targetMonth.ceilTo(periodMonth, tz)

        assertEquals(LocalDateTime(2023, 3, 1, 0, 0), flooredMonth)
        assertEquals(LocalDateTime(2023, 5, 1, 0, 0), ceiledMonth)
    }

    @Test
    fun testFindTimeInterval() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0) // UTC epoch millisecond: 1672568100000
        // 1672568100000 ms / 1 hour (3600000 ms) = 464602.25 hours
        // floor to 1 hour = 464602 hours = 1672567200000 ms -> 2023-01-01T10:00:00
        val duration = 1.hours
        val found = target.findTimeInterval(duration, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), found.start)
        assertEquals(LocalDateTime(2023, 1, 1, 11, 0, 0, 0), found.endExclusive)

        // Test with explicit anchor
        val anchor = LocalDateTime(2023, 1, 1, 10, 10, 0, 0)
        // 10-minute offset, grid: 10:10, 11:10, etc.
        val duration2 = 1.hours
        val foundWithAnchor = target.findTimeInterval(duration2, anchor, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 10, 10, 0, 0), foundWithAnchor.start)
        assertEquals(LocalDateTime(2023, 1, 1, 11, 10, 0, 0), foundWithAnchor.endExclusive)
    }

    @Test
    fun testFindTimeIntervalFailsForNonPositiveDuration() {
        val target = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        assertFailsWith<IllegalArgumentException> {
            target.findTimeInterval((-1).hours, tz)
        }
    }

    @Test
    fun testFloorToAndCeilTo() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0)
        val period = DateTimePeriod(hours = 1)
        val floored = target.floorTo(period, tz)
        val ceiled = target.ceilTo(period, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), floored)
        assertEquals(LocalDateTime(2023, 1, 1, 11, 0, 0, 0), ceiled)
    }

    @Test
    fun testMinusDateTimePeriod() {
        val ldt = LocalDateTime(2023, 3, 4, 14, 0, 0, 0)
        val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4)
        val result = ldt.minus(period, tz)
        assertEquals(LocalDateTime(2022, 1, 1, 10, 0, 0, 0), result)
    }

    @Test
    fun testRoundTo() {
        val target = LocalDateTime(2023, 1, 1, 10, 15, 0, 0)
        val period = DateTimePeriod(hours = 1)
        val rounded = target.roundTo(period, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 10, 0, 0, 0), rounded)

        val target2 = LocalDateTime(2023, 1, 1, 10, 45, 0, 0)
        val rounded2 = target2.roundTo(period, tz)
        assertEquals(LocalDateTime(2023, 1, 1, 11, 0, 0, 0), rounded2)
    }

    @Test
    fun testMinusLocalDateTime() {
        val start = LocalDateTime(2023, 1, 1, 12, 0)
        val end = LocalDateTime(2023, 1, 1, 10, 0)
        val duration = start.minus(end)
        assertEquals(2.hours, duration)
    }

    @Test
    fun testGenerateSequenceWithDirection() {
        val start = LocalDateTime(2023, 1, 1, 0, 0)
        val period = DateTimePeriod(days = 1)
        
        // Forward
        val seqForward = start.generateSequence(SequenceDirection.Forward, period, tz)
        val forwardList = seqForward.take(2).toList()
        assertEquals(LocalDateTime(2023, 1, 1, 0, 0)..<LocalDateTime(2023, 1, 2, 0, 0), forwardList[0])
        assertEquals(LocalDateTime(2023, 1, 2, 0, 0)..<LocalDateTime(2023, 1, 3, 0, 0), forwardList[1])

        // Backward
        val seqBackward = start.generateSequence(SequenceDirection.Backward, period, tz)
        val backwardList = seqBackward.take(2).toList()
        assertEquals(LocalDateTime(2022, 12, 31, 0, 0)..<LocalDateTime(2023, 1, 1, 0, 0), backwardList[0])
        assertEquals(LocalDateTime(2022, 12, 30, 0, 0)..<LocalDateTime(2022, 12, 31, 0, 0), backwardList[1])
    }

    @Test
    fun testGenerateSequenceWithPeriodSign() {
        val start = LocalDateTime(2023, 1, 1, 0, 0)
        
        // Positive period (Forward)
        val periodPos = DateTimePeriod(days = 1)
        val seqPos = start.generateSequence(periodPos, tz)
        val posList = seqPos.take(2).toList()
        assertEquals(LocalDateTime(2023, 1, 1, 0, 0)..<LocalDateTime(2023, 1, 2, 0, 0), posList[0])

        // Negative period (Backward)
        val periodNeg = -DateTimePeriod(days = 1)
        val seqNeg = start.generateSequence(periodNeg, tz)
        val negList = seqNeg.take(2).toList()
        assertEquals(LocalDateTime(2022, 12, 31, 0, 0)..<LocalDateTime(2023, 1, 1, 0, 0), negList[0])
    }
}
