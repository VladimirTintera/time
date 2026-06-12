package eu.tintera.time.core.context

import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class TimeZoneCalculationContextTest {

    private val zones = listOf(
        TimeZone.of("Europe/Prague"),
        TimeZone.of("Asia/Tokyo"),
        TimeZone.of("Australia/Sydney")
    )

    @Test
    fun testCalculationsAcrossZonesContext() {
        val start = LocalDateTime(2023, 1, 1, 10, 0, 0, 0)
        val period = DateTimePeriod(days = 1)

        for (zone in zones) {
            with(zone) {
                val result = start.plus(period)
                assertEquals(LocalDateTime(2023, 1, 2, 10, 0, 0, 0), result)
                
                val subtracted = result.minus(period)
                assertEquals(start, subtracted)
            }
        }
    }

    @Test
    fun testPragueDstSpringForwardContext() {
        val zone = TimeZone.of("Europe/Prague")
        val beforeDst = LocalDateTime(2023, 3, 26, 1, 59, 59)
        
        with(zone) {
            val afterDst = beforeDst.plus(1.seconds)
            assertEquals(LocalDateTime(2023, 3, 26, 3, 0, 0), afterDst)

            val reverted = afterDst.plus(-1.seconds)
            assertEquals(beforeDst, reverted)
        }
    }

    @Test
    fun testPragueDstAutumnFallBackContext() {
        val zone = TimeZone.of("Europe/Prague")
        val ldt = LocalDateTime(2023, 10, 29, 1, 30, 0)
        with(zone) {
            val added = ldt.plus(2.hours)
            assertEquals(LocalDateTime(2023, 10, 29, 2, 30, 0), added)
        }
    }

    @Test
    fun testSydneyDstSpringForwardContext() {
        val zone = TimeZone.of("Australia/Sydney")
        val beforeDst = LocalDateTime(2023, 10, 1, 1, 59, 59)
        with(zone) {
            val afterDst = beforeDst.plus(1.seconds)
            assertEquals(LocalDateTime(2023, 10, 1, 3, 0, 0), afterDst)

            val reverted = afterDst.plus(-1.seconds)
            assertEquals(beforeDst, reverted)
        }
    }

    @Test
    fun testSydneyDstAutumnFallBackContext() {
        val zone = TimeZone.of("Australia/Sydney")
        val ldt = LocalDateTime(2023, 4, 2, 1, 30, 0)
        with(zone) {
            val added = ldt.plus(2.hours)
            assertEquals(LocalDateTime(2023, 4, 2, 2, 30, 0), added)
        }
    }

    @Test
    fun testBoundaryFloorCeilRoundContext() {
        val prague = TimeZone.of("Europe/Prague")
        val target = LocalDateTime(2023, 3, 26, 12, 0, 0)
        val period = DateTimePeriod(days = 1)
        
        with(prague) {
            val floored = target.floorTo(period)
            val ceiled = target.ceilTo(period)

            assertEquals(LocalDateTime(2023, 3, 26, 0, 0, 0), floored)
            assertEquals(LocalDateTime(2023, 3, 27, 0, 0, 0), ceiled)
        }
    }

    @Test
    fun testGenerateSequenceDSTContext() {
        val prague = TimeZone.of("Europe/Prague")
        val start = LocalDateTime(2023, 3, 25, 0, 0, 0)
        val period = DateTimePeriod(days = 1)
        
        with(prague) {
            val sequence = start.generateSequence(period)
            val list = sequence.take(3).toList()

            assertEquals(3, list.size)
            assertEquals(LocalDateTime(2023, 3, 25, 0, 0, 0)..<LocalDateTime(2023, 3, 26, 0, 0, 0), list[0])
            assertEquals(LocalDateTime(2023, 3, 26, 0, 0, 0)..<LocalDateTime(2023, 3, 27, 0, 0, 0), list[1])
            assertEquals(LocalDateTime(2023, 3, 27, 0, 0, 0)..<LocalDateTime(2023, 3, 28, 0, 0, 0), list[2])
        }
    }
}
