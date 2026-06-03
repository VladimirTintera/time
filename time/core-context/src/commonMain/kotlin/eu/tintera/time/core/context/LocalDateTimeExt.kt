package eu.tintera.time.core.context

import eu.tintera.time.core.*
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Duration

/**
 * Returns a new [LocalDateTime] with the given [DateTimePeriod] added to this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * with(TimeZone.UTC) {
 *     val result = ldt.plus(period)
 * }
 * ```
 *
 * @param period The period to add.
 * @return A [LocalDateTime] representing the sum.
 */
context(timeZone: TimeZone)
operator fun LocalDateTime.plus(period: DateTimePeriod): LocalDateTime =
    plus(period, timeZone)

/**
 * Returns a new [LocalDateTime] with the time component of the given [LocalTime] added to this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val time = LocalTime(1, 30)
 * with(TimeZone.UTC) {
 *     val result = ldt.plus(time)
 * }
 * ```
 *
 * @param time The local time duration to add.
 * @return A [LocalDateTime] representing the sum.
 */
context(timeZone: TimeZone)
operator fun LocalDateTime.plus(time: LocalTime): LocalDateTime =
    plus(time, timeZone)

/**
 * Returns a new [LocalDateTime] with the given Kotlin [Duration] added to this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = 2.hours
 * with(TimeZone.UTC) {
 *     val result = ldt.plus(duration)
 * }
 * ```
 *
 * @param duration The duration to add.
 * @return A [LocalDateTime] representing the sum.
 */
context(timeZone: TimeZone)
operator fun LocalDateTime.plus(duration: Duration): LocalDateTime =
    plus(duration, timeZone)

/**
 * Generates an infinite sequence of consecutive, non-overlapping intervals starting from this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * with(TimeZone.UTC) {
 *     val seq = ldt.generateSequence(period)
 * }
 * ```
 *
 * @param period The period of each interval.
 * @return A sequence of [OpenEndRange] of [LocalDateTime].
 * @throws IllegalArgumentException if the [period] does not move time forward.
 */
context(timeZone: TimeZone)
fun LocalDateTime.generateSequence(
    period: DateTimePeriod,
): Sequence<OpenEndRange<LocalDateTime>> = generateSequence(period, timeZone)

/**
 * Generates an infinite sequence of consecutive, non-overlapping intervals starting from this [LocalDateTime] in a specified direction.
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * with(TimeZone.UTC) {
 *     val seq = ldt.generateSequence(SequenceDirection.Forward, period)
 * }
 * ```
 *
 * @param direction The direction of sequence generation (Forward or Backward).
 * @param period The period of each interval. Must be positive.
 * @return A sequence of [OpenEndRange] of [LocalDateTime].
 * @throws IllegalArgumentException if the [period] is not positive.
 */
context(timeZone: TimeZone)
fun LocalDateTime.generateSequence(
    direction: SequenceDirection,
    period: DateTimePeriod
): Sequence<OpenEndRange<LocalDateTime>> = generateSequence(direction, period, timeZone)

/**
 * Slices this range of [LocalDateTime] into smaller consecutive intervals of the specified [DateTimePeriod].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val start = LocalDateTime(2025, 4, 15, 12, 0)
 * val end = LocalDateTime(2025, 4, 18, 12, 0)
 * val range = start..<end
 * val period = DateTimePeriod(days = 1)
 * with(TimeZone.UTC) {
 *     val slices = range.slice(period)
 * }
 * ```
 *
 * @param period The period of each slice.
 * @return A sequence of sliced intervals.
 */
context(timeZone: TimeZone)
infix fun OpenEndRange<LocalDateTime>.slice(
    period: DateTimePeriod
): Sequence<OpenEndRange<LocalDateTime>> =
    slice(period, timeZone)

/**
 * Finds the interval in which this [LocalDateTime] lies, according to the specified [DateTimePeriod].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(hours = 2)
 * with(TimeZone.UTC) {
 *     val interval = ldt.findInterval(period)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @return The interval containing this date-time, or null if it cannot be determined.
 */
context(timeZone: TimeZone)
fun LocalDateTime.findInterval(
    period: DateTimePeriod
): OpenEndRange<LocalDateTime>? =
    findInterval(period, timeZone)

/**
 * Finds the interval in which this [LocalDateTime] lies, according to the specified [DateTimePeriod] and [anchor].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(hours = 2)
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * with(TimeZone.UTC) {
 *     val interval = ldt.findInterval(period, anchor)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @param anchor The start date-time anchor of the interval grid.
 * @return The interval containing this date-time, or null if it cannot be determined.
 */
context(timeZone: TimeZone)
fun LocalDateTime.findInterval(
    period: DateTimePeriod,
    anchor: LocalDateTime
): OpenEndRange<LocalDateTime>? =
    findInterval(period, anchor, timeZone)

/**
 * Finds the time-based interval of the specified [Duration] that contains this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = 2.hours
 * with(TimeZone.UTC) {
 *     val interval = ldt.findTimeInterval(duration)
 * }
 * ```
 *
 * @param duration The duration defining the grid size.
 * @return The interval containing this date-time.
 * @throws IllegalArgumentException if [duration] is not positive.
 */
context(timeZone: TimeZone)
fun LocalDateTime.findTimeInterval(
    duration: Duration
): OpenEndRange<LocalDateTime> = findTimeInterval(duration, timeZone)

/**
 * Finds the time-based interval of the specified [Duration] and [anchor] that contains this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val duration = 2.hours
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * with(TimeZone.UTC) {
 *     val interval = ldt.findTimeInterval(duration, anchor)
 * }
 * ```
 *
 * @param duration The duration defining the grid size.
 * @param anchor The start date-time anchor of the interval grid.
 * @return The interval containing this date-time.
 */
context(timeZone: TimeZone)
fun LocalDateTime.findTimeInterval(
    duration: Duration,
    anchor: LocalDateTime
): OpenEndRange<LocalDateTime> = findTimeInterval(duration, anchor, timeZone)

/**
 * Truncates (floors) this [LocalDateTime] to the start of the interval defined by the specified [DateTimePeriod].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * with(TimeZone.UTC) {
 *     val result = ldt.floorTo(period)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @return The floored [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.floorTo(period: DateTimePeriod): LocalDateTime = floorTo(period, timeZone)

/**
 * Truncates (floors) this [LocalDateTime] to the start of the interval defined by [DateTimePeriod] and [anchor].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * with(TimeZone.UTC) {
 *     val result = ldt.floorTo(period, anchor)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @param anchor The start date-time anchor of the interval grid.
 * @return The floored [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.floorTo(
    period: DateTimePeriod,
    anchor: LocalDateTime
): LocalDateTime = floorTo(period, timeZone, anchor)

/**
 * Rounds up (ceils) this [LocalDateTime] to the end (exclusive) of the interval defined by the specified [DateTimePeriod].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * with(TimeZone.UTC) {
 *     val result = ldt.ceilTo(period)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @return The ceiled [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.ceilTo(period: DateTimePeriod): LocalDateTime = ceilTo(period, timeZone)

/**
 * Rounds up (ceils) this [LocalDateTime] to the end (exclusive) of the interval defined by [DateTimePeriod] and [anchor].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * with(TimeZone.UTC) {
 *     val result = ldt.ceilTo(period, anchor)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @param anchor The start date-time anchor of the interval grid.
 * @return The ceiled [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.ceilTo(
    period: DateTimePeriod,
    anchor: LocalDateTime
): LocalDateTime = ceilTo(period, timeZone, anchor)

/**
 * Returns a new [LocalDateTime] with the given [DateTimePeriod] subtracted from this [LocalDateTime].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 0)
 * val period = DateTimePeriod(days = 1)
 * with(TimeZone.UTC) {
 *     val result = ldt.minus(period)
 * }
 * ```
 *
 * @param period The period to subtract.
 * @return A [LocalDateTime] representing the difference.
 */
context(timeZone: TimeZone)
operator fun LocalDateTime.minus(period: DateTimePeriod): LocalDateTime =
    minus(period, timeZone)

/**
 * Rounds this [LocalDateTime] to the nearest interval defined by the specified [DateTimePeriod].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * with(TimeZone.UTC) {
 *     val result = ldt.roundTo(period)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @return The rounded [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.roundTo(period: DateTimePeriod): LocalDateTime = roundTo(period, timeZone)

/**
 * Rounds this [LocalDateTime] to the nearest interval boundary of [DateTimePeriod] and [anchor].
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context to perform the calculation.
 *
 * Example:
 * ```kotlin
 * val ldt = LocalDateTime(2025, 4, 15, 12, 34)
 * val period = DateTimePeriod(hours = 1)
 * val anchor = LocalDateTime(2025, 4, 15, 10, 0)
 * with(TimeZone.UTC) {
 *     val result = ldt.roundTo(period, anchor)
 * }
 * ```
 *
 * @param period The period defining the interval grid.
 * @param anchor The start date-time anchor of the interval grid.
 * @return The rounded [LocalDateTime].
 */
context(timeZone: TimeZone)
fun LocalDateTime.roundTo(
    period: DateTimePeriod,
    anchor: LocalDateTime
): LocalDateTime = roundTo(period, timeZone, anchor)