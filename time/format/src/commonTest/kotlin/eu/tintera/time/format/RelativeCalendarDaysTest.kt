package eu.tintera.time.format

import eu.tintera.locale.localeForLanguageTag
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Relative days follow calendar dates, not elapsed 24-hour periods: "yesterday" and "3 days ago" name a day
 * in the calendar, whatever the time of day of either value.
 */
class RelativeCalendarDaysTest {

    private val zone = TimeZone.of("Europe/Prague")
    private val czech = localeForLanguageTag("cs")
    private val now = LocalDateTime(2026, 10, 26, 10, 0)

    private fun unit(target: LocalDateTime, config: RelativeDateTimeFormatScope.() -> Unit = { full() }): Measurable? {
        val scope = RelativeDateTimeFormatScope(RelativeValues(target, now), czech, zone).apply(config)
        return computeRelativeUnit(target.toInstant(zone), now.toInstant(zone), zone, scope)
    }

    private fun days(value: Int) = Measurable(MeasureUnit.DAYS, value)

    @Test
    fun laterTimeOfDayThreeDatesBackIsThreeDaysAgo() {
        // 2 days 19 hours elapsed – counted by elapsed time this was "2 days ago"
        assertEquals(days(-3), unit(LocalDateTime(2026, 10, 23, 15, 0)))
    }

    @Test
    fun earlierTimeOfDayTwoDatesBackIsTwoDaysAgo() {
        assertEquals(days(-2), unit(LocalDateTime(2026, 10, 24, 9, 0)))
    }

    @Test
    fun lateEveningTwoDatesBackIsNotYesterday() {
        // 1 day 11 hours elapsed – counted by elapsed time this was "yesterday"
        assertEquals(days(-2), unit(LocalDateTime(2026, 10, 24, 23, 0)))
    }

    @Test
    fun previousDateMoreThanADayAgoIsYesterday() {
        assertEquals(days(-1), unit(LocalDateTime(2026, 10, 25, 9, 0)))
    }

    @Test
    fun previousDateLessThanADayAgoStaysInHours() {
        assertEquals(Measurable(MeasureUnit.HOURS, -11), unit(LocalDateTime(2026, 10, 25, 23, 0)))
    }

    @Test
    fun futureDatesFollowTheCalendarToo() {
        // 2 days 3 hours ahead, but 3 dates ahead
        assertEquals(days(3), unit(LocalDateTime(2026, 10, 29, 9, 0)))
    }

    @Test
    fun daysThresholdAppliesToCalendarDays() {
        // 2 days 19 hours elapsed, 3 calendar days: a threshold of 3 is met
        assertEquals(days(-3), unit(LocalDateTime(2026, 10, 23, 15, 0)) { days(3); hours() })
    }

    @Test
    fun formattedTextNamesTheCalendarDay() {
        val formatted = LocalDateTime(2026, 10, 23, 15, 0).formatRelative(now, zone, czech)
        assertEquals("před 3 dny", formatted.replace(' ', ' '))
    }
}
