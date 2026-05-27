package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.locale.currentLocale
import eu.tintera.locale.localeForLanguageTag
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class FormattersAndExtensionsTest {

    private val locale = localeForLanguageTag("en")
    private val tz = TimeZone.UTC

    @Test
    fun testDatePeriodFormat() {
        val period = DatePeriod(years = 1, months = 2, days = 3)
        
        // Success case
        val formatted = period.formatCalendar(locale) {
            years = UnitVisibility.Required
            months = UnitVisibility.Auto
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            period.format(DatePeriodFormat {}, locale)
        }
    }

    @Test
    fun testDateTimePeriodFormat() {
        val period = DateTimePeriod(years = 1, hours = 3)

        // Success case
        val formatted = period.format(locale) {
            calendar { years = UnitVisibility.Required }
            clock { hours = UnitVisibility.Required }
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            period.format(DateTimePeriodFormat {}, locale)
        }
    }

    @Test
    fun testDurationFormat() {
        val duration = 5.hours + 30.minutes

        // Success case
        val formatted = duration.format(locale) {
            hours = UnitVisibility.Required
            minutes = UnitVisibility.Required
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            duration.format(DurationFormat {}, locale)
        }
    }

    @Test
    fun testDurationDigitalFormat() {
        val duration = 1.hours + 20.minutes + 15.seconds

        // Success case
        val formatted = duration.formatDigital(locale) {
            stopwatch()
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            duration.formatDigital(DurationDigitalFormat {}, locale)
        }
    }

    @Test
    fun testInstantFormat() {
        val instant = Instant.fromEpochMilliseconds(1672531200000L) // 2023-01-01T00:00:00Z

        // Success case
        val formatted = instant.format(tz, locale) {
            date { medium() }
            time { short() }
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            instant.format(DateTimeFormat {}, tz, locale)
        }
    }

    @Test
    fun testInstantFormatRelative() {
        val now = Instant.fromEpochMilliseconds(1672531200000L)
        val target = now + 5.minutes

        // Success case
        val formatted = target.formatRelative(now, tz, locale) {
            minutes()
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            target.formatRelative(now, tz, locale, RelativeDateTimeFormat {
                years = null
                months = null
                days = null
                hours = null
                minutes = null
                seconds = null
            })
        }
    }

    @Test
    fun testInstantFormatInterval() {
        val from = Instant.fromEpochMilliseconds(1672531200000L)
        val to = from + 2.hours

        // Success case
        val formatted = from.formatInterval(to, tz, locale) {
            time { short() }
        }
        assertTrue(formatted.isNotEmpty())
    }

    @Test
    fun testLocalDateFormats() {
        val date = LocalDate(2023, 5, 15)

        // Date format
        val formatted = date.format(locale) {
            medium()
        }
        assertTrue(formatted.isNotEmpty())

        // Month name
        val monthName = date.formatMonthName(MonthFormat.Name.Full, locale)
        assertTrue(monthName.isNotEmpty())

        // Weekday name
        val weekdayName = date.formatWeekDayName(WeekDayFormat.FullName, locale)
        assertTrue(weekdayName.isNotEmpty())

        // Date interval
        val toDate = LocalDate(2023, 5, 20)
        val intervalFormatted = date.formatInterval(toDate, locale, tz) {
            medium()
        }
        assertTrue(intervalFormatted.isNotEmpty())
    }

    @Test
    fun testLocalDateTimeFormats() {
        val ldt = LocalDateTime(2023, 5, 15, 14, 30)

        // LocalDateTime format
        val formatted = ldt.format(locale) {
            date { medium() }
            time { short() }
        }
        assertTrue(formatted.isNotEmpty())

        // Relative format
        val now = LocalDateTime(2023, 5, 15, 14, 0)
        val relativeFormatted = ldt.formatRelative(now, tz, locale) {
            minutes()
        }
        assertTrue(relativeFormatted.isNotEmpty())

        // Interval format
        val toLdt = LocalDateTime(2023, 5, 15, 16, 30)
        val intervalFormatted = ldt.formatInterval(toLdt, locale, tz) {
            time { short() }
        }
        assertTrue(intervalFormatted.isNotEmpty())
    }

    @Test
    fun testLocalTimeFormat() {
        val time = LocalTime(14, 30, 0, 0)

        // LocalTime format
        val formatted = time.format(locale) {
            short()
        }
        assertTrue(formatted.isNotEmpty())

        // Empty config throws EmptyFormatConfigurationException
        assertFailsWith<EmptyFormatConfigurationException> {
            time.format(TimeFormat {}, locale)
        }
    }

    @Test
    fun testMonthAndDayOfWeekFormatName() {
        val month = Month.JANUARY
        val monthFormatted = month.formatName(locale, MonthFormat.Name.Full)
        assertTrue(monthFormatted.isNotEmpty())

        val day = DayOfWeek.MONDAY
        val dayFormatted = day.formatName(locale, WeekDayFormat.FullName)
        assertTrue(dayFormatted.isNotEmpty())
    }

    @Test
    fun testGetFirstDayOfWeek() {
        val firstDay = getFirstDayOfWeek()
        assertNotNull(firstDay)
    }

    @Test
    fun testGetDecimalSeparator() {
        val separator = getDecimalSeparator(locale)
        assertTrue(separator.isNotEmpty())
    }
}
