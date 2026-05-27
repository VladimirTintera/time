package eu.tintera.time.core

import kotlinx.datetime.*
import kotlin.time.Duration
import kotlin.time.Instant

/**
 * Returns a new [LocalDateTime] with the given [DateTimePeriod] added to this [LocalDateTime] in the specified [TimeZone].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * val tz = TimeZone.UTC
 * val result = ldt.plus(period, tz)
 * ```
 *
 * @param period The period to add.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A [LocalDateTime] representing the sum.
 */
fun LocalDateTime.plus(
    period: DateTimePeriod,
    timeZone: TimeZone
): LocalDateTime {
    val instant = date.plus(period.datePeriod).atTime(time).toInstant(timeZone)
    val finalInstant = instant + period.timeDuration
    return finalInstant.toLocalDateTime(timeZone)
}

/**
 * Returns a new [LocalDateTime] with the time component of the given [LocalTime] added to this [LocalDateTime] in the specified [TimeZone].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val time = LocalTime(1, 30)
 * val tz = TimeZone.UTC
 * val result = ldt.plus(time, tz)
 * ```
 *
 * @param time The local time duration to add.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A [LocalDateTime] representing the sum.
 */
fun LocalDateTime.plus(
    time: LocalTime,
    timeZone: TimeZone
): LocalDateTime = plus(
    duration = time.duration,
    timeZone = timeZone
)

/**
 * Returns a new [LocalDateTime] with the given Kotlin [Duration] added to this [LocalDateTime] in the specified [TimeZone].
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = 2.hours
 * val tz = TimeZone.UTC
 * val result = ldt.plus(duration, tz)
 * ```
 *
 * @param duration The duration to add.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A [LocalDateTime] representing the sum.
 */
fun LocalDateTime.plus(
    duration: Duration,
    timeZone: TimeZone
): LocalDateTime = toInstant(timeZone).plus(duration).toLocalDateTime(timeZone)

/**
 * Adds the specified [DatePeriod] to this [LocalDateTime] calendar-wise.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DatePeriod(months = 1)
 * val result = ldt + period
 * ```
 *
 * @param period The date period to add.
 * @return A [LocalDateTime] representing the sum.
 */
operator fun LocalDateTime.plus(
    period: DatePeriod
) = date.plus(period).atTime(time)

/**
 * Generates an infinite sequence of consecutive, non-overlapping intervals starting from this [LocalDateTime].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * val tz = TimeZone.UTC
 * val seq = ldt.generateSequence(period, tz)
 * ```
 *
 * @param period The period of each interval.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A sequence of [OpenEndRange] of [LocalDateTime].
 * @throws IllegalArgumentException if the [period] does not move time forward.
 */
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

/**
 * Slices this range of [LocalDateTime] into smaller consecutive intervals of the specified [DateTimePeriod].
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2025, 4, 15, 12, 0)
 * val end = LocalDateTime(2025, 4, 18, 12, 0)
 * val range = start..<end
 * val period = DateTimePeriod(days = 1)
 * val tz = TimeZone.UTC
 * val slices = range.slice(period, tz)
 * ```
 *
 * @param period The period of each slice.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A sequence of sliced intervals.
 */
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

/**
 * Finds the interval in which this [LocalDateTime] lies, according to the specified [DateTimePeriod].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(hours = 2)
 * val tz = TimeZone.UTC
 * val interval = ldt.findInterval(period, tz)
 * ```
 *
 * @param period The period defining the interval grid.
 * @param timeZone The time zone in which the calculation is performed.
 * @return The interval containing this date-time, or null if it cannot be determined.
 */
fun LocalDateTime.findInterval(
    period: DateTimePeriod,
    timeZone: TimeZone
): OpenEndRange<LocalDateTime>? {

    if (period.years == 0 && period.months == 0 && period.days == 0) {
        return findTimeInterval(period.timeDuration, timeZone)
    }

    return findInterval(
        generateSequence(period, timeZone).takeWhile { interval ->
            interval.start <= this
        }.toList()
    )
}

/**
 * Finds the interval in the provided list of sorted intervals that contains this [LocalDateTime].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val intervals = listOf(
 *     LocalDateTime(2025, 4, 15, 0, 0)..<LocalDateTime(2025, 4, 15, 12, 0),
 *     LocalDateTime(2025, 4, 15, 12, 0)..<LocalDateTime(2025, 4, 16, 0, 0)
 * )
 * val interval = ldt.findInterval(intervals)
 * ```
 *
 * @param intervals A sorted list of non-overlapping intervals.
 * @return The interval containing this date-time, or null if none match.
 */
fun LocalDateTime.findInterval(
    intervals: List<OpenEndRange<LocalDateTime>>
): OpenEndRange<LocalDateTime>? {
    val index = intervals.binarySearch { interval ->
        when {
            this < interval.start -> -1       // Hledaný čas je vlevo od bucketu -> hledej vlevo (záporné číslo)
            this >= interval.endExclusive -> 1  // Hledaný čas je vpravo od bucketu -> hledej vpravo (kladné číslo)
            else -> 0                           // Trefa! To je náš bucket
        }
    }

    return if (index >= 0) intervals[index] else null
}

/**
 * Finds the time-based interval of the specified [Duration] that contains this [LocalDateTime].
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = 2.hours
 * val tz = TimeZone.UTC
 * val interval = ldt.findTimeInterval(duration, tz)
 * ```
 *
 * @param duration The duration defining the grid size.
 * @param timeZone The time zone in which the calculation is performed.
 * @return The interval containing this date-time.
 * @throws IllegalArgumentException if [duration] is not positive.
 */
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

/**
 * Truncates (floors) this [LocalDateTime] to the start of the interval defined by the specified [DateTimePeriod].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * val tz = TimeZone.UTC
 * val result = ldt.floorTo(period, tz)
 * ```
 *
 * @param period The period defining the interval grid.
 * @param timeZone The time zone in which the calculation is performed.
 * @return The floored [LocalDateTime].
 */
fun LocalDateTime.floorTo(
    period: DateTimePeriod,
    timeZone: TimeZone
): LocalDateTime = findInterval(period, timeZone)?.start ?: this

/**
 * Rounds up (ceils) this [LocalDateTime] to the end (exclusive) of the interval defined by the specified [DateTimePeriod].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * val tz = TimeZone.UTC
 * val result = ldt.ceilTo(period, tz)
 * ```
 *
 * @param period The period defining the interval grid.
 * @param timeZone The time zone in which the calculation is performed.
 * @return The ceiled [LocalDateTime].
 */
fun LocalDateTime.ceilTo(
    period: DateTimePeriod,
    timeZone: TimeZone
) = findInterval(period, timeZone)?.endExclusive ?: this