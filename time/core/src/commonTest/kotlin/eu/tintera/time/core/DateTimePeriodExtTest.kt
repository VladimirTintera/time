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
}
