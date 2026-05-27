package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.time.Duration

@TimeDslMarker
class LocalDateTimeModifierBuilder internal constructor(
    initial: LocalDateTime,
    timeZone: TimeZone
) {
    private var currentTimeZone = timeZone
    private var currentDate: LocalDate = initial.date
    private var currentTime: LocalTime = initial.time

    fun timeZone(timeZone: TimeZone) {
        currentTimeZone = timeZone
    }

    fun plusYears(value: Int) {
        currentDate = currentDate.plus(value, DateTimeUnit.YEAR)
    }

    fun minusYears(value: Int) = plusMonths(-value)

    fun plusMonths(value: Int) {
        currentDate = currentDate.plus(value, DateTimeUnit.MONTH)
    }

    fun minusMonths(value: Int) = plusMonths(-value)

    fun plusDays(value: Int) {
        currentDate = currentDate.plus(value, DateTimeUnit.DAY)
    }

    fun minusDays(value: Int) = plusDays(-value)

    fun plusTime(time: LocalTime, timeZone: TimeZone = TimeZone.currentSystemDefault()) {
        build().plus(time, timeZone)
    }

    fun plusDuration(duration: Duration) {
        val nextLdt = build().plus(duration, currentTimeZone)
        currentDate = nextLdt.date
        currentTime = nextLdt.time
    }

    fun withTime(hour: Int = 0, minute: Int = 0, second: Int = 0, nanosecond: Int = 0) {
        currentTime = LocalTime(hour, minute, second, nanosecond)
    }

    fun withDate(year: Int, month: Int, day: Int) {
        currentDate = LocalDate(year, month, day)
    }

    fun plus(period: DateTimePeriod) {
        val nextLdt = build().plus(period, currentTimeZone)
        currentDate = nextLdt.date
        currentTime = nextLdt.time
    }

    internal fun build(): LocalDateTime {
        return LocalDateTime(currentDate, currentTime)
    }
}

fun LocalDateTime.modify(
    timeZone: TimeZone,
    block: LocalDateTimeModifierBuilder.() -> Unit
): LocalDateTime = LocalDateTimeModifierBuilder(
    initial = this,
    timeZone = timeZone
).apply(block).build()