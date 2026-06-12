package eu.tintera.time.format

import eu.tintera.locale.*
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

class LocaleAndTimeZoneFormattingTest {

    private val locales = listOf(localeForLanguageTag("cs"), localeForLanguageTag("en"))
    private val zones = listOf(
        TimeZone.of("Europe/Prague"),
        TimeZone.of("Asia/Tokyo"),
        TimeZone.of("Australia/Sydney")
    )

    private val instant = Instant.fromEpochMilliseconds(1672531200000L) // 2023-01-01T00:00:00Z

    private fun String.clean(): String {
        return this.replace('\u00A0', ' ')
            .replace('\u202F', ' ')
            .replace('\u2009', ' ')
            .replace('\u2013', '-')
            .replace(" ", "")
    }

    @Test
    fun testInstantFormatting() {
        for (locale in locales) {
            for (zone in zones) {
                val formatted = instant.format(zone, locale) {
                    date { medium() }
                    time { short() }
                }.clean()
                
                if (locale.languageTag.startsWith("cs")) {
                    when (zone.id) {
                        "Europe/Prague" -> assertEquals("1.1.20231:00", formatted)
                        "Asia/Tokyo" -> assertEquals("1.1.20239:00", formatted)
                        "Australia/Sydney" -> assertEquals("1.1.202311:00", formatted)
                    }
                } else {
                    when (zone.id) {
                        "Europe/Prague" -> assertEquals("1/1/2023,1:00AM", formatted)
                        "Asia/Tokyo" -> assertEquals("1/1/2023,9:00AM", formatted)
                        "Australia/Sydney" -> assertEquals("1/1/2023,11:00AM", formatted)
                    }
                }
            }
        }
    }

    @Test
    fun testInstantRelativeFormatting() {
        val now = instant
        val target = instant + 15.minutes
        for (locale in locales) {
            for (zone in zones) {
                val formatted = target.formatRelative(now, zone, locale) {
                    minutes()
                }.clean()
                
                if (locale.languageTag.startsWith("cs")) {
                    assertEquals("za15minut", formatted)
                } else {
                    assertEquals("in15minutes", formatted)
                }
            }
        }
    }

    @Test
    fun testInstantIntervalFormatting() {
        val start = instant
        val end = instant + 2.hours
        for (locale in locales) {
            for (zone in zones) {
                val formatted = start.formatInterval(end, zone, locale) {
                    date { medium() }
                    time { short() }
                }.clean()
                
                if (locale.languageTag.startsWith("cs")) {
                    when (zone.id) {
                        "Europe/Prague" -> assertEquals("1.1.20231:00-3:00", formatted)
                        "Asia/Tokyo" -> assertEquals("1.1.20239:00-11:00", formatted)
                        "Australia/Sydney" -> assertEquals("1.1.202311:00-13:00", formatted)
                    }
                } else {
                    when (zone.id) {
                        "Europe/Prague" -> assertEquals("1/1/2023,1:00-3:00AM", formatted)
                        "Asia/Tokyo" -> assertEquals("1/1/2023,9:00-11:00AM", formatted)
                        "Australia/Sydney" -> assertEquals("1/1/2023,11:00AM-1:00PM", formatted)
                    }
                }
            }
        }
    }

    @Test
    fun testLocalDateTimeFormatting() {
        val ldt = LocalDateTime(2023, 5, 15, 14, 30, 0, 0)
        for (locale in locales) {
            val formatted = ldt.format(locale) {
                date { long() }
                time { short() }
            }.clean()
            
            if (locale.languageTag.startsWith("cs")) {
                assertEquals("15.května2023v14:30", formatted)
            } else {
                assertEquals("May15,2023at2:30PM", formatted)
            }
        }
    }

    @Test
    fun testLocalDateFormatting() {
        val date = LocalDate(2023, 5, 15)
        for (locale in locales) {
            val monthNameFull = date.formatMonthName(MonthFormat.Name.Full, locale).clean()
            val monthNameShort = date.formatMonthName(MonthFormat.Name.Short, locale).clean()
            val weekDayNameFull = date.formatWeekDayName(WeekDayFormat.FullName, locale).clean()
            val weekDayNameShort = date.formatWeekDayName(WeekDayFormat.ShortName, locale).clean()

            if (locale.languageTag.startsWith("cs")) {
                assertEquals("květen", monthNameFull)
                assertEquals("kvě", monthNameShort)
                assertEquals("pondělí", weekDayNameFull)
                assertEquals("po", weekDayNameShort)
            } else {
                assertEquals("May", monthNameFull)
                assertEquals("May", monthNameShort)
                assertEquals("Monday", weekDayNameFull)
                assertEquals("Mon", weekDayNameShort)
            }
        }
    }

    @Test
    fun testLocalTimeFormatting() {
        val time = LocalTime(14, 30, 45, 0)
        for (locale in locales) {
            val formatted = time.format(locale) {
                full()
            }.clean()
            
            if (locale.languageTag.startsWith("cs")) {
                assertEquals("14:30:45", formatted)
            } else {
                assertEquals("2:30:45PM", formatted)
            }
        }
    }

    @Test
    fun testDurationFormatting() {
        val duration = 1.hours + 30.minutes
        for (locale in locales) {
            val formatted = duration.format(locale) {
                hours = UnitVisibility.Required
                minutes = UnitVisibility.Required
            }.clean()
            
            val formattedDigital = duration.formatDigital(locale) {
                stopwatch()
            }.clean()
            
            if (locale.languageTag.startsWith("cs")) {
                assertEquals("1hodina,30minut", formatted)
                assertEquals("01:30:00", formattedDigital)
            } else {
                assertEquals("1hour,30minutes", formatted)
                assertEquals("01:30:00", formattedDigital)
            }
        }
    }

    @Test
    fun testLocalTimeFractionalSecondsFormatting() {
        val time = LocalTime(14, 30, 45, 123_000_000) // 14:30:45.123
        for (locale in locales) {
            // Three Digits
            val formattedThree = time.format(locale) {
                hour = HourFormat.Digital24h.Padded
                minute = MinuteFormat.Padded
                second = SecondFormat.Padded
                fractionalSecond = FractionalSecondFormat.ThreeDigits
                periodStyle = DayPeriodStyle.None
            }.clean()
            assertTrue(
                formattedThree == "14:30:45.123" || formattedThree == "14:30:45,123",
                "Formatted string '$formattedThree' does not contain fractional seconds .123 or ,123"
            )

            // Two Digits
            val formattedTwo = time.format(locale) {
                hour = HourFormat.Digital24h.Padded
                minute = MinuteFormat.Padded
                second = SecondFormat.Padded
                fractionalSecond = FractionalSecondFormat.TwoDigits
                periodStyle = DayPeriodStyle.None
            }.clean()
            assertTrue(
                formattedTwo == "14:30:45.12" || formattedTwo == "14:30:45,12",
                "Formatted string '$formattedTwo' does not contain fractional seconds .12 or ,12"
            )

            // One Digit
            val formattedOne = time.format(locale) {
                hour = HourFormat.Digital24h.Padded
                minute = MinuteFormat.Padded
                second = SecondFormat.Padded
                fractionalSecond = FractionalSecondFormat.OneDigits
                periodStyle = DayPeriodStyle.None
            }.clean()
            assertTrue(
                formattedOne == "14:30:45.1" || formattedOne == "14:30:45,1",
                "Formatted string '$formattedOne' does not contain fractional seconds .1 or ,1"
            )
        }
    }

    @Test
    fun testDurationDigitalFractionalSecondsFormatting() {
        val dur = 1.hours + 30.minutes + 15.seconds + 500.milliseconds
        for (locale in locales) {
            // Three Digits
            val formattedThree = dur.formatDigital(locale) {
                hour = HourFormat.Digital24h.Padded
                minute = MinuteFormat.Padded
                second = SecondFormat.Padded
                fractionalSecond = FractionalSecondFormat.ThreeDigits
            }.clean()
            assertTrue(
                formattedThree == "01:30:15.500" || formattedThree == "01:30:15,500",
                "Formatted digital string '$formattedThree' does not contain fractional seconds .500 or ,500"
            )

            // Two Digits
            val formattedTwo = dur.formatDigital(locale) {
                hour = HourFormat.Digital24h.Padded
                minute = MinuteFormat.Padded
                second = SecondFormat.Padded
                fractionalSecond = FractionalSecondFormat.TwoDigits
            }.clean()
            assertTrue(
                formattedTwo == "01:30:15.50" || formattedTwo == "01:30:15,50",
                "Formatted digital string '$formattedTwo' does not contain fractional seconds .50 or ,50"
            )

            // One Digit
            val formattedOne = dur.formatDigital(locale) {
                hour = HourFormat.Digital24h.Padded
                minute = MinuteFormat.Padded
                second = SecondFormat.Padded
                fractionalSecond = FractionalSecondFormat.OneDigits
            }.clean()
            assertTrue(
                formattedOne == "01:30:15.5" || formattedOne == "01:30:15,5",
                "Formatted digital string '$formattedOne' does not contain fractional seconds .5 or ,5"
            )
        }
    }
}
