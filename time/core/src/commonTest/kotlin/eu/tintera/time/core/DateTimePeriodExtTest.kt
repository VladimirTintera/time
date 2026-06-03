package eu.tintera.time.core

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DatePeriod
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.nanoseconds

class DateTimePeriodExtTest {

    @Test
    fun testDatePeriod() {
        val period = DateTimePeriod(years = 2, months = 3, days = 5, hours = 4, minutes = 30)
        val datePeriod = period.datePeriod
        assertEquals(2, datePeriod.years)
        assertEquals(3, datePeriod.months)
        assertEquals(5, datePeriod.days)
    }

    @Test
    fun testTimeDuration() {
        val period = DateTimePeriod(hours = 2, minutes = 15, seconds = 30, nanoseconds = 500)
        val expectedDuration = 2.hours + 15.minutes + 30.seconds + 500.nanoseconds
        assertEquals(expectedDuration, period.timeDuration)
    }

    @Test
    fun testEmptyPeriod() {
        val period = DateTimePeriod()
        val datePeriod = period.datePeriod
        assertEquals(0, datePeriod.years)
        assertEquals(0, datePeriod.months)
        assertEquals(0, datePeriod.days)
        assertEquals(0.seconds, period.timeDuration)
    }

    @Test
    fun testPeriodBuilders() {
        assertEquals(DateTimePeriod(years = 3), 3.periodYears)
        assertEquals(DateTimePeriod(months = 6), 6.periodMonths)
        assertEquals(DateTimePeriod(days = 10), 10.periodDays)
        assertEquals(DateTimePeriod(hours = 4), 4.periodHours)
        assertEquals(DateTimePeriod(minutes = 30), 30.periodMinutes)
        assertEquals(DateTimePeriod(seconds = 45), 45.periodSeconds)
        assertEquals(DateTimePeriod(nanoseconds = 500), 500L.periodNanoseconds)
    }

    @Test
    fun testUnaryMinus() {
        val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4, minutes = 5, seconds = 6, nanoseconds = 7)
        val negative = -period
        assertEquals(-1, negative.years)
        assertEquals(-2, negative.months)
        assertEquals(-3, negative.days)
        assertEquals(-4, negative.hours)
        assertEquals(-5, negative.minutes)
        assertEquals(-6, negative.seconds)
        assertEquals(-7, negative.nanoseconds)
    }
}
