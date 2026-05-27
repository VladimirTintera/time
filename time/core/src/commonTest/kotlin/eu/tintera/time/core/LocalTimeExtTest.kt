package eu.tintera.time.core

import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.nanoseconds

class LocalTimeExtTest {

    @Test
    fun testDuration() {
        val time = LocalTime(12, 34, 56, 789)
        val expected = 12.hours + 34.minutes + 56.seconds + 789.nanoseconds
        assertEquals(expected, time.duration)
    }

    @Test
    fun testDurationZero() {
        val time = LocalTime(0, 0, 0, 0)
        assertEquals(0.seconds, time.duration)
    }

    @Test
    fun testDurationMax() {
        val time = LocalTime(23, 59, 59, 999999999)
        val expected = 23.hours + 59.minutes + 59.seconds + 999999999.nanoseconds
        assertEquals(expected, time.duration)
    }
}
