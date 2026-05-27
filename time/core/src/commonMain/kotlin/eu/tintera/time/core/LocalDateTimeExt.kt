package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.Instant

fun LocalDateTime.plus(
    period: DateTimePeriod,
    timeZone: TimeZone
): LocalDateTime {
    val instant = date.plus(period.datePeriod).atTime(time).toInstant(timeZone)
    val finalInstant = instant + period.timeDuration
    return finalInstant.toLocalDateTime(timeZone)
}

fun LocalDateTime.plus(
    time: LocalTime,
    timeZone: TimeZone
): LocalDateTime = plus(
    duration = time.duration,
    timeZone = timeZone
)

fun LocalDateTime.plus(
    duration: Duration,
    timeZone: TimeZone
): LocalDateTime = toInstant(timeZone).plus(duration).toLocalDateTime(timeZone)

operator fun LocalDateTime.plus(
    period: DatePeriod
) = date.plus(period).atTime(time)

fun LocalDateTime.generateSequence(
    period: DateTimePeriod,
    timeZone: TimeZone
): Sequence<OpenEndRange<LocalDateTime>> {

    // Inicializační krok – ověříme hned první posun
    val firstEnd = this.plus(period, timeZone)

    // 👑 Pokud perioda neposouvá čas vpřed, okamžitě střílíme Fail-Fast výjimku
    require(firstEnd > this) {
        "DateTimePeriod must move time forward. Invalid period: $period"
    }

    return generateSequence(this..<firstEnd) { previous ->
        val nextStart = previous.endExclusive
        val nextEnd = nextStart.plus(period, timeZone)

        // Pojistka pro jistotu i v průběhu (např. kdyby kvůli DST nějaký exotický krok selhal)
        check(nextEnd > nextStart) { "Sequence generation detected a non-progressive time step." }

        nextStart..<nextEnd
    }
}

fun OpenEndRange<LocalDateTime>.slice(
    period: DateTimePeriod,
    timeZone: TimeZone
) = start.generateSequence(period, timeZone).takeWhile {
    it.start < endExclusive
}.map { item ->
    if (item.endExclusive > this.endExclusive) {
        item.start..<this.endExclusive
    } else {
        item
    }
}

fun LocalDateTime.findInterval(
    period: DateTimePeriod,
    sequence: Sequence<OpenEndRange<LocalDateTime>>,
    timeZone: TimeZone
): OpenEndRange<LocalDateTime>? {

    // Pokud je to čistě časová perioda (hodiny/minuty), spočítáme to okamžitě matematicky
    if (period.years == 0 && period.months == 0 && period.days == 0) {
        return findTimeInterval(period.timeDuration, timeZone)
    }

    return findInterval(sequence.toList())
}

fun LocalDateTime.findInterval(
    intervals: List<OpenEndRange<LocalDateTime>>
): OpenEndRange<LocalDateTime>? {
    val index = intervals.binarySearch { interval ->
        when {
            this < interval.start -> 1       // Hledaný čas je před bucketem -> hledej vlevo
            this >= interval.endExclusive -> -1 // Hledaný čas je za bucketem -> hledej vpravo
            else -> 0                         // Trefa! To je náš bucket
        }
    }

    return if (index >= 0) intervals[index] else null
}

fun LocalDateTime.findTimeInterval(
    duration: Duration,
    timeZone: TimeZone
): OpenEndRange<LocalDateTime> {
    require(duration.isPositive()) { "Duration must be positive" }

    val totalDurationMs = duration.inWholeMilliseconds

    val currentMs = toInstant(timeZone).toEpochMilliseconds()

    val bucketStartMs = currentMs.floorDiv(totalDurationMs) * totalDurationMs

    val startLdt = Instant.fromEpochMilliseconds(bucketStartMs).toLocalDateTime(timeZone)
    val endLdt = startLdt.plus(duration, timeZone)

    return startLdt..<endLdt
}