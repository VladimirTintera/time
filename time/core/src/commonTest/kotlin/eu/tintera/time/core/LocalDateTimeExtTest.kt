package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.seconds

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
        val sequence = start.generateSequence(period, tz)
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
            start.generateSequence(period, tz)
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
        // Since findInterval uses generateSequence starting from target.floorTo... Wait,
        // Actually, the implementation of findInterval(period, timeZone) is:
        // if (years == 0 && months == 0 && days == 0) return findTimeInterval(timeDuration, timeZone)
        // else return findInterval(generateSequence(period, timeZone).takeWhile { it.start <= this }.toList())
        // Wait, generateSequence starts at `this`!
        // Let's trace it:
        // generateSequence(this) returns a sequence of intervals:
        // [this ..< this + period], [this + period ..< this + 2*period], ...
        // So the first interval starts at `this`.
        // Thus, `it.start <= this` is true for the first interval, and false for the second (since period moves time forward).
        // So the generated list of intervals contains exactly one element: [this ..< this + period].
        // Then `findInterval(intervals)` is called on that list.
        // Since `this` is exactly the start of the first interval, it matches, and returns [this ..< this + period].
        // Let's verify:
        // For target = LocalDateTime(2023, 1, 2, 12, 0) and period = DateTimePeriod(days = 1)
        // The first interval will be LocalDateTime(2023, 1, 2, 12, 0)..<LocalDateTime(2023, 1, 3, 12, 0).
        // So the found interval start should be exactly target (LocalDateTime(2023, 1, 2, 12, 0)).
        assertEquals(target, found.start)
        assertEquals(target.plus(period, tz), found.endExclusive)
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
}
