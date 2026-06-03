package eu.tintera.time.format.context

import eu.tintera.locale.*
import eu.tintera.time.format.*
import kotlinx.datetime.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class LocaleAndTimeZoneFormattingContextTest {

    private val locales = listOf(localeForLanguageTag("cs"), localeForLanguageTag("en"))
    private val zones = listOf(
        TimeZone.of("Europe/Prague"),
        TimeZone.of("Asia/Kolkata"),
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
    fun testInstantFormattingContext() {
        for (locale in locales) {
            for (zone in zones) {
                with(locale) {
                    with(zone) {
                        val formatted = instant.format {
                            date { medium() }
                            time { short() }
                        }.clean()
                        
                        if (locale.languageTag.startsWith("cs")) {
                            when (zone.id) {
                                "Europe/Prague" -> assertEquals("1.1.20231:00", formatted)
                                "Asia/Kolkata" -> assertEquals("1.1.20235:30", formatted)
                                "Australia/Sydney" -> assertEquals("1.1.202311:00", formatted)
                            }
                        } else {
                            when (zone.id) {
                                "Europe/Prague" -> assertEquals("1/1/2023,1:00AM", formatted)
                                "Asia/Kolkata" -> assertEquals("1/1/2023,5:30AM", formatted)
                                "Australia/Sydney" -> assertEquals("1/1/2023,11:00AM", formatted)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testInstantRelativeFormattingContext() {
        val now = instant
        val target = instant + 15.minutes
        for (locale in locales) {
            for (zone in zones) {
                with(locale) {
                    with(zone) {
                        val formatted = target.formatRelative(now) {
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
        }
    }

    @Test
    fun testInstantIntervalFormattingContext() {
        val start = instant
        val end = instant + 2.hours
        for (locale in locales) {
            for (zone in zones) {
                with(locale) {
                    with(zone) {
                        val formatted = start.formatInterval(end) {
                            date { medium() }
                            time { short() }
                        }.clean()
                        
                        if (locale.languageTag.startsWith("cs")) {
                            when (zone.id) {
                                "Europe/Prague" -> assertEquals("1.1.20231:00-3:00", formatted)
                                "Asia/Kolkata" -> assertEquals("1.1.20235:30-7:30", formatted)
                                "Australia/Sydney" -> assertEquals("1.1.202311:00-13:00", formatted)
                            }
                        } else {
                            when (zone.id) {
                                "Europe/Prague" -> assertEquals("1/1/2023,1:00-3:00AM", formatted)
                                "Asia/Kolkata" -> assertEquals("1/1/2023,5:30-7:30AM", formatted)
                                "Australia/Sydney" -> assertEquals("1/1/2023,11:00AM-1:00PM", formatted)
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testLocalDateTimeFormattingContext() {
        val ldt = LocalDateTime(2023, 5, 15, 14, 30, 0, 0)
        for (locale in locales) {
            with(locale) {
                val formatted = ldt.format {
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
    }

    @Test
    fun testLocalDateFormattingContext() {
        val date = LocalDate(2023, 5, 15)
        for (locale in locales) {
            with(locale) {
                val monthNameFull = date.formatMonthName(MonthFormat.Name.Full).clean()
                val monthNameShort = date.formatMonthName(MonthFormat.Name.Short).clean()
                val weekDayNameFull = date.formatWeekDayName(WeekDayFormat.FullName).clean()
                val weekDayNameShort = date.formatWeekDayName(WeekDayFormat.ShortName).clean()

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
    }

    @Test
    fun testLocalTimeFormattingContext() {
        val time = LocalTime(14, 30, 45, 0)
        for (locale in locales) {
            with(locale) {
                val formatted = time.format {
                    full()
                }.clean()
                
                if (locale.languageTag.startsWith("cs")) {
                    assertEquals("14:30:45", formatted)
                } else {
                    assertEquals("2:30:45PM", formatted)
                }
            }
        }
    }

    @Test
    fun testDurationFormattingContext() {
        val duration = 1.hours + 30.minutes
        for (locale in locales) {
            with(locale) {
                val formatted = duration.format {
                    hours = UnitVisibility.Required
                    minutes = UnitVisibility.Required
                }.clean()
                
                val formattedDigital = duration.formatDigital {
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
    }
}
