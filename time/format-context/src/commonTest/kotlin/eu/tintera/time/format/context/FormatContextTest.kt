package eu.tintera.time.format.context

import eu.tintera.locale.localeForLanguageTag
import eu.tintera.time.format.DateTimeIntervalFormat
import eu.tintera.time.format.MonthFormat
import eu.tintera.time.format.UnitVisibility
import eu.tintera.time.format.WeekDayFormat
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class FormatContextTest {

    private val locale = localeForLanguageTag("en")
    private val tz = TimeZone.UTC

    @Test
    fun testDatePeriodFormat() {
        val period = DatePeriod(years = 1, months = 2)
        with(locale) {
            val formatted = period.formatCalendar {
                years = UnitVisibility.Required
            }
            assertTrue(formatted.isNotEmpty())
        }
    }

    @Test
    fun testDateTimePeriodFormat() {
        val period = DateTimePeriod(years = 1, hours = 3)
        with(locale) {
            val formatted = period.format {
                calendar { years = UnitVisibility.Required }
                clock { hours = UnitVisibility.Required }
            }
            assertTrue(formatted.isNotEmpty())
        }
    }

    @Test
    fun testDurationFormat() {
        val duration = 5.hours + 30.minutes
        with(locale) {
            val formatted = duration.format {
                hours = UnitVisibility.Required
            }
            assertTrue(formatted.isNotEmpty())

            val formattedDigital = duration.formatDigital {
                stopwatch()
            }
            assertTrue(formattedDigital.isNotEmpty())
        }
    }

    @Test
    fun testInstantFormat() {
        val instant = Instant.fromEpochMilliseconds(1672531200000L) // 2023-01-01T00:00:00Z
        val now = instant - 5.minutes
        val to = instant + 2.hours

        with(locale) {
            with(tz) {
                val formatted = instant.format {
                    date { medium() }
                }
                assertTrue(formatted.isNotEmpty())

                val relative = instant.formatRelative(now) {
                    minutes()
                }
                assertTrue(relative.isNotEmpty())

                val interval = instant.formatInterval(to) {
                    time { short() }
                }
                assertTrue(interval.isNotEmpty())
            }
        }
    }

    @Test
    fun testStandaloneFormatInterval() {
        val from = Instant.fromEpochMilliseconds(1672531200000L)
        val to = from + 2.hours

        with(locale) {
            with(tz) {
                val formatted = formatInterval(from, to, DateTimeIntervalFormat {
                    time { short() }
                })
                assertTrue(formatted.isNotEmpty())
            }
        }
    }

    @Test
    fun testLocalFormats() {
        val date = LocalDate(2023, 5, 15)
        val ldt = LocalDateTime(2023, 5, 15, 14, 30)
        val time = LocalTime(14, 30, 0, 0)

        with(locale) {
            // LocalDate format
            assertTrue(date.format { medium() }.isNotEmpty())
            assertTrue(date.formatMonthName(MonthFormat.Name.Full).isNotEmpty())
            assertTrue(date.formatWeekDayName(WeekDayFormat.FullName).isNotEmpty())

            // LocalDateTime format
            assertTrue(ldt.format {
                date { medium() }
            }.isNotEmpty())

            // LocalTime format
            assertTrue(time.format { short() }.isNotEmpty())
        }
    }

    @Test
    fun testIntervalAndRelativeWithContext() {
        val date1 = LocalDate(2023, 5, 15)
        val date2 = LocalDate(2023, 5, 20)
        val ldt1 = LocalDateTime(2023, 5, 15, 14, 30)
        val ldt2 = LocalDateTime(2023, 5, 15, 16, 30)

        with(locale) {
            with(tz) {
                assertTrue(date1.formatInterval(date2) { medium() }.isNotEmpty())
                assertTrue(ldt1.formatInterval(ldt2) { time { short() } }.isNotEmpty())
                assertTrue(ldt1.formatRelative(ldt2) { hours() }.isNotEmpty())
            }
        }
    }

    @Test
    fun testUtilsAndContext() {
        with(locale) {
            assertTrue(Month.JANUARY.formatName().isNotEmpty())
            assertTrue(DayOfWeek.MONDAY.formatName().isNotEmpty())
            assertTrue(getDecimalSeparator().isNotEmpty())
            assertTrue(getFirstDayOfWeek() is DayOfWeek)
        }
    }

    @Test
    fun testWithRegionalContext() {
        val result = withRegionalContext(tz, locale) {
            val instant = Instant.fromEpochMilliseconds(1672531200000L)
            instant.format {
                date { medium() }
            }
        }
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testOpenEndRangeLocalDateTimeFormat() {
        val start = LocalDateTime(2023, 5, 15, 14, 30)
        val end = LocalDateTime(2023, 5, 15, 16, 30)
        val range = start..<end
        with(locale) {
            with(tz) {
                val formatted = range.format {
                    time { short() }
                }
                assertTrue(formatted.isNotEmpty())

                val formattedExplicit = range.format(DateTimeIntervalFormat {
                    time { short() }
                })
                assertEquals(formatted, formattedExplicit)
            }
        }
    }
}
