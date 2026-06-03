package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TimeZoneCalculationTest {

    private val zones = listOf(
        TimeZone.of("Europe/Prague"),
        TimeZone.of("Asia/Kolkata"),
        TimeZone.of("Australia/Sydney")
    )

    @Test
    fun testCalculationsAcrossZones() {
        // Standard arithmetic: verify 1 day addition works consistently
        val start = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val period = DateTimePeriod(days = 1)

        for (zone in zones) {
            val result = start.plus(period, zone)
            assertEquals(LocalDateTime(2023, 1, 2, 10, 0, 0, 0), result, "Failed adding 1 day in zone ${zone.id}")
            
            val subtracted = result.minus(period, zone)
            assertEquals(start, subtracted, "Failed subtracting 1 day in zone ${zone.id}")
        }
    }

    @Test
    fun testPragueDstSpringForward() {
        val zone = TimeZone.of("Europe/Prague")
        // March 26, 2023: clocks spring forward at 02:00 to 03:00 (UTC+1 to UTC+2)
        val beforeDst = LocalDateTime(2023, 3, 26, 1, 59, 59)
        
        // Adding 1 second should skip the gap and land on 03:00:00
        val afterDst = beforeDst.plus(1.seconds, zone)
        assertEquals(LocalDateTime(2023, 3, 26, 3, 0, 0), afterDst)

        // Subtracting 1 second from 03:00:00 should go back to 01:59:59
        val reverted = afterDst.plus(-1.seconds, zone)
        assertEquals(beforeDst, reverted)

        // Adding 2 hours from 01:00:00
        val start = LocalDateTime(2023, 3, 26, 1, 0, 0)
        val end = start.plus(2.hours, zone)
        // 01:00 + 2 hours -> physical time is 3 hours later. Wait, 01:00 (UTC+1) is 00:00Z.
        // 2 hours later is 02:00Z, which is 04:00 (UTC+2) in Prague.
        // So adding 2 hours to 01:00:00 should yield 04:00:00 in Prague local time!
        assertEquals(LocalDateTime(2023, 3, 26, 4, 0, 0), end)
    }

    @Test
    fun testPragueDstAutumnFallBack() {
        val zone = TimeZone.of("Europe/Prague")
        // October 29, 2023: clocks fall back at 03:00 to 02:00 (UTC+2 to UTC+1)
        // Let's verify that addition behaves correctly across the transition.
        val ldt = LocalDateTime(2023, 10, 29, 1, 30, 0) // Before transition
        val added = ldt.plus(2.hours, zone)
        // 01:30 (UTC+2, i.e., 23:30Z of previous day).
        // 2 hours later is 01:30Z, which is 02:30 (UTC+1) in Prague.
        // Let's assert:
        assertEquals(LocalDateTime(2023, 10, 29, 2, 30, 0), added)
    }

    @Test
    fun testSydneyDstSpringForward() {
        val zone = TimeZone.of("Australia/Sydney")
        // October 1, 2023: clocks spring forward at 02:00 to 03:00 (UTC+10 to UTC+11)
        val beforeDst = LocalDateTime(2023, 10, 1, 1, 59, 59)
        val afterDst = beforeDst.plus(1.seconds, zone)
        assertEquals(LocalDateTime(2023, 10, 1, 3, 0, 0), afterDst)

        val reverted = afterDst.plus(-1.seconds, zone)
        assertEquals(beforeDst, reverted)
    }

    @Test
    fun testSydneyDstAutumnFallBack() {
        val zone = TimeZone.of("Australia/Sydney")
        // April 2, 2023: clocks fall back at 03:00 to 02:00 (UTC+11 to UTC+10)
        val ldt = LocalDateTime(2023, 4, 2, 1, 30, 0) // Before transition
        val added = ldt.plus(2.hours, zone)
        assertEquals(LocalDateTime(2023, 4, 2, 2, 30, 0), added)
    }

    @Test
    fun testKolkataLinearCalculations() {
        val zone = TimeZone.of("Asia/Kolkata")
        // Kolkata has no DST, everything is linear
        val ldt = LocalDateTime(2023, 3, 26, 1, 59, 59)
        val added = ldt.plus(1.seconds, zone)
        assertEquals(LocalDateTime(2023, 3, 26, 2, 0, 0), added)

        val ldt2 = LocalDateTime(2023, 10, 29, 1, 59, 59)
        val added2 = ldt2.plus(2.hours, zone)
        assertEquals(LocalDateTime(2023, 10, 29, 3, 59, 59), added2)
    }

    @Test
    fun testBoundaryFloorCeilRound() {
        val prague = TimeZone.of("Europe/Prague")
        // March 26, 2023 DST transition day
        val target = LocalDateTime(2023, 3, 26, 12, 0, 0)
        val period = DateTimePeriod(days = 1)
        
        val floored = target.floorTo(period, prague)
        val ceiled = target.ceilTo(period, prague)

        assertEquals(LocalDateTime(2023, 3, 26, 0, 0, 0), floored)
        assertEquals(LocalDateTime(2023, 3, 27, 0, 0, 0), ceiled)
    }

    @Test
    fun testGenerateSequenceDST() {
        val prague = TimeZone.of("Europe/Prague")
        val start = LocalDateTime(2023, 3, 25, 0, 0, 0)
        val period = DateTimePeriod(days = 1)
        
        val sequence = start.generateSequence(SequenceDirection.Forward, period, prague)
        val list = sequence.take(3).toList()

        assertEquals(3, list.size)
        // Interval 1: March 25 to March 26
        assertEquals(LocalDateTime(2023, 3, 25, 0, 0, 0)..<LocalDateTime(2023, 3, 26, 0, 0, 0), list[0])
        // Interval 2: March 26 to March 27 (contains DST transition, but start/end are midnight)
        assertEquals(LocalDateTime(2023, 3, 26, 0, 0, 0)..<LocalDateTime(2023, 3, 27, 0, 0, 0), list[1])
        // Interval 3: March 27 to March 28
        assertEquals(LocalDateTime(2023, 3, 27, 0, 0, 0)..<LocalDateTime(2023, 3, 28, 0, 0, 0), list[2])
    }
}
