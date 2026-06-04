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
    return toInstant(timeZone).plus(period, timeZone).toLocalDateTime(timeZone)
}

/**
 * Returns a new [LocalDateTime] with the given [DateTimePeriod] subtracted from this [LocalDateTime] in the specified [TimeZone].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * val tz = TimeZone.UTC
 * val result = ldt.minus(period, tz)
 * ```
 *
 * @param period The period to subtract.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A [LocalDateTime] representing the difference.
 */
fun LocalDateTime.minus(
    period: DateTimePeriod,
    timeZone: TimeZone
): LocalDateTime {
    return toInstant(timeZone).minus(period, timeZone).toLocalDateTime(timeZone)
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
internal fun LocalDateTime.generateAscendingSequence(
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

internal fun LocalDateTime.generateDescendingSequence(
    period: DateTimePeriod,
    timeZone: TimeZone
): Sequence<OpenEndRange<LocalDateTime>> {

    // Inicializační krok – ověříme hned první posun vzad (odečtení)
    val firstStart = this.minus(period, timeZone)

    // 👑 Fail-Fast: Pokud perioda neposouvá čas vzad, okamžitě vyhodíme výjimku
    require(firstStart < this) {
        "DateTimePeriod must move time backward. Invalid period: $period"
    }

    // První bucket stavíme od minulosti (firstStart) do teď (this)
    return generateSequence(firstStart..<this) { previous ->
        val nextEnd = previous.start
        val nextStart = nextEnd.minus(period, timeZone)

        // Pojistka pro jistotu i v průběhu (např. kvůli DST anomáliím)
        check(nextStart < nextEnd) { "Sequence generation detected a non-regressive time step." }

        nextStart..<nextEnd
    }
}

/**
 * Generates an infinite sequence of consecutive, non-overlapping intervals starting from this [LocalDateTime] in a specified direction.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * val tz = TimeZone.UTC
 * val seq = ldt.generateSequence(SequenceDirection.Forward, period, tz)
 * ```
 *
 * @param direction The direction of sequence generation (Forward or Backward).
 * @param period The period of each interval. Must be positive.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A sequence of [OpenEndRange] of [LocalDateTime].
 * @throws IllegalArgumentException if the [period] is not positive.
 */
fun LocalDateTime.generateSequence(
    direction: SequenceDirection,
    period: DateTimePeriod,
    timeZone: TimeZone
): Sequence<OpenEndRange<LocalDateTime>> {

    // 👑 Kontrola, že je perioda striktně kladná (posouvá čas dopředu)
    val probe = this.plus(period, timeZone)
    require(probe > this) {
        "For explicit direction, the DateTimePeriod must be positive. Invalid period: $period"
    }

    return when (direction) {
        SequenceDirection.Forward -> generateAscendingSequence(period, timeZone)
        SequenceDirection.Backward -> generateDescendingSequence(period, timeZone)
    }
}

/**
 * Generates an infinite sequence of consecutive, non-overlapping intervals starting from this [LocalDateTime] based on the sign of the period.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * val tz = TimeZone.UTC
 * val seq = ldt.generateSequence(period, tz)
 * ```
 *
 * @param period The period of each interval. A positive period generates ascending intervals, a negative period generates descending intervals.
 * @param timeZone The time zone in which the calculation is performed.
 * @return A sequence of [OpenEndRange] of [LocalDateTime].
 * @throws IllegalArgumentException if the [period] is zero.
 */
fun LocalDateTime.generateSequence(
    period: DateTimePeriod,
    timeZone: TimeZone
): Sequence<OpenEndRange<LocalDateTime>> {
    val probe = this.plus(period, timeZone)

    return when {
        probe > this -> {
            // Perioda je kladná -> posíláme ji tak, jak je
            generateAscendingSequence(period, timeZone)
        }
        probe < this -> {
            // 👑 Perioda je záporná -> OTOČÍME JI unárním mínus!
            // Tím z ní uděláme periodu, která posouvá čas dopředu,
            // a generateDescendingSequence ji pak může bezpečně odčítat.
            generateDescendingSequence(-period, timeZone)
        }
        else -> throw IllegalArgumentException("DateTimePeriod does not shift time at all: $period")
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
) = start.generateAscendingSequence(period, timeZone).takeWhile {
    it.start < endExclusive
}.map { item ->
    if (item.endExclusive > this.endExclusive) {
        item.start..<this.endExclusive
    } else {
        item
    }
}

/**
 * Finds the interval in which this [LocalDateTime] lies, according to the specified [DateTimePeriod] and [anchor] point.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(hours = 2)
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * val tz = TimeZone.UTC
 * val interval = ldt.findInterval(period, anchor, tz)
 * ```
 *
 * @param period The period defining the interval grid.
 * @param anchor The start date-time anchor of the interval grid.
 * @param timeZone The time zone in which the calculation is performed.
 * @return The interval containing this date-time, or null if it cannot be determined.
 */
fun LocalDateTime.findInterval(
    period: DateTimePeriod,
    anchor: LocalDateTime,
    timeZone: TimeZone
): OpenEndRange<LocalDateTime>? {

    if (period.years == 0 && period.months == 0 && period.days == 0) {
        return findTimeInterval(period.timeDuration, anchor, timeZone)
    }

    // 2. Kalendářní cesta (DateTimePeriod)
    val probe = anchor.plus(period, timeZone)

    val intervals = when {
        probe > anchor -> {
            if (this >= anchor) {
                anchor.generateAscendingSequence(period, timeZone)
                    .takeWhile { interval -> interval.start <= this }
                    .toList()
            } else {
                anchor.generateDescendingSequence(period, timeZone)
                    .takeWhile { interval -> interval.endExclusive > this }
                    .toList()
                    .reversed()
            }
        }
        probe < anchor -> {
            if (this <= anchor) {
                anchor.generateDescendingSequence(period, timeZone)
                    .takeWhile { interval -> interval.endExclusive >= this }
                    .toList()
                    .reversed()
            } else {
                anchor.generateAscendingSequence(period, timeZone)
                    .takeWhile { interval -> interval.start <= this }
                    .toList()
            }
        }
        else -> throw IllegalArgumentException("DateTimePeriod does not shift time at all: $period")
    }

    return findInterval(intervals)
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
): OpenEndRange<LocalDateTime>? = findInterval(period, defaultAnchor(period), timeZone)

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
            this < interval.start -> 1          // Search target is before the interval -> look in the left half
            this >= interval.endExclusive -> -1 // Search target is after the interval -> look in the right half
            else -> 0                           // Match! This is the containing interval
        }
    }

    return if (index >= 0) intervals[index] else null
}

/**
 * Finds the time-based interval of the specified [Duration] and [anchor] point that contains this [LocalDateTime].
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = 2.hours
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * val tz = TimeZone.UTC
 * val interval = ldt.findTimeInterval(duration, anchor, tz)
 * ```
 *
 * @param duration The duration defining the grid size.
 * @param anchor The start date-time anchor of the interval grid.
 * @param timeZone The time zone in which the calculation is performed.
 * @return The interval containing this date-time.
 * @throws IllegalArgumentException if [duration] is zero.
 */
fun LocalDateTime.findTimeInterval(
    duration: Duration,
    anchor: LocalDateTime,
    timeZone: TimeZone
): OpenEndRange<LocalDateTime> {

    require(duration > Duration.ZERO) { "Duration must be positive" }

    val absoluteDuration = duration.absoluteValue
    val totalDurationMs = absoluteDuration.inWholeMilliseconds

    val anchorMs = anchor.toInstant(timeZone).toEpochMilliseconds()
    val currentMs = toInstant(timeZone).toEpochMilliseconds()

    // Calculate relative offset from anchor
    val diffMs = currentMs - anchorMs
    val bucketOffsetMs = diffMs.floorDiv(totalDurationMs) * totalDurationMs
    val bucketStartMs = anchorMs + bucketOffsetMs

    val startLdt = Instant.fromEpochMilliseconds(bucketStartMs).toLocalDateTime(timeZone)
    val endLdt = startLdt.plus(absoluteDuration, timeZone)

    return startLdt..<endLdt
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
 */
fun LocalDateTime.findTimeInterval(
    duration: Duration,
    timeZone: TimeZone
): OpenEndRange<LocalDateTime> = findTimeInterval(
    duration = duration,
    anchor = Instant.fromEpochMilliseconds(0).toLocalDateTime(timeZone),
    timeZone = timeZone
)

internal fun LocalDateTime.defaultAnchor(period: DateTimePeriod): LocalDateTime {
    return when {
        period.years != 0 || period.months != 0 -> LocalDateTime(this.year, 1, 1, 0, 0, 0, 0)
        period.days != 0 -> LocalDateTime(this.year, this.month, 1, 0, 0, 0, 0)
        else -> LocalDateTime(this.year, this.month, this.day, 0, 0, 0, 0)
    }
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
 * @param anchor The start date-time anchor of the interval grid.
 * @return The floored [LocalDateTime].
 */
fun LocalDateTime.floorTo(
    period: DateTimePeriod,
    timeZone: TimeZone,
    anchor: LocalDateTime = defaultAnchor(period)
): LocalDateTime = findInterval(period, anchor, timeZone)?.start ?: this

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
 * @param anchor The start date-time anchor of the interval grid.
 * @return The ceiled [LocalDateTime].
 */
fun LocalDateTime.ceilTo(
    period: DateTimePeriod,
    timeZone: TimeZone,
    anchor: LocalDateTime = defaultAnchor(period)
): LocalDateTime = findInterval(period, anchor, timeZone)?.endExclusive ?: this

/**
 * Rounds this [LocalDateTime] to the nearest interval boundary of [DateTimePeriod] in the specified [TimeZone].
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 15)
 * val period = DateTimePeriod(hours = 1)
 * val tz = TimeZone.UTC
 * val rounded = ldt.roundTo(period, tz)
 * ```
 *
 * @param period The period defining the interval grid.
 * @param timeZone The time zone in which the calculation is performed.
 * @param anchor The start date-time anchor of the interval grid.
 * @return The rounded [LocalDateTime].
 */
fun LocalDateTime.roundTo(
    period: DateTimePeriod,
    timeZone: TimeZone,
    anchor: LocalDateTime = defaultAnchor(period)
): LocalDateTime {
    val interval = findInterval(period, anchor, timeZone) ?: return this

    val distanceToStart = this.minus(interval.start)
    val distanceToEnd = interval.endExclusive.minus(this)

    return if (distanceToStart < distanceToEnd) {
        interval.start
    } else {
        interval.endExclusive
    }
}

/**
 * Calculates the duration between this [LocalDateTime] and another [LocalDateTime] in UTC.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2025, 4, 15, 10, 0)
 * val end = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = start.minus(end)
 * ```
 *
 * @param other The other [LocalDateTime] to subtract.
 * @return The [Duration] from this [LocalDateTime] to [other] (computed as other - this).
 */
fun LocalDateTime.minus(other: LocalDateTime): Duration {
    val thisInstant = this.toInstant(TimeZone.UTC)
    val otherInstant = other.toInstant(TimeZone.UTC)
    return thisInstant - otherInstant
}