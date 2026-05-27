package eu.tintera.time.core.context

import eu.tintera.time.core.*
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlin.time.Duration

/**
 * Returns a new [LocalDateTime] with the given [period] added, resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.plus(period: DateTimePeriod): LocalDateTime =
    plus(period, zone.timeZone)

/**
 * Returns a new [LocalDateTime] with the given [time] duration added, resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.plus(time: LocalTime): LocalDateTime =
    plus(time, zone.timeZone)

/**
 * Returns a new [LocalDateTime] with the given [duration] added, resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.plus(duration: Duration): LocalDateTime =
    plus(duration, zone.timeZone)

/**
 * Generates a sequence of [OpenEndRange] intervals of [LocalDateTime] by repeatedly adding [period],
 * resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.generateSequence(
    period: DateTimePeriod
): Sequence<OpenEndRange<LocalDateTime>> =
    generateSequence(period, zone.timeZone)

/**
 * Slices this range of [LocalDateTime] into smaller intervals of [period] length,
 * resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun OpenEndRange<LocalDateTime>.slice(
    period: DateTimePeriod
): Sequence<OpenEndRange<LocalDateTime>> =
    slice(period, zone.timeZone)

/**
 * Finds the interval from the given [sequence] that contains this [LocalDateTime],
 * resolved using the [TimeZoneContext] from the context if it's a time-only period.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.findInterval(
    period: DateTimePeriod,
    sequence: Sequence<OpenEndRange<LocalDateTime>>
): OpenEndRange<LocalDateTime>? =
    findInterval(period, sequence, zone.timeZone)

/**
 * Finds the time interval of the given [duration] containing this [LocalDateTime],
 * resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.findTimeInterval(
    duration: Duration
): OpenEndRange<LocalDateTime> =
    findTimeInterval(duration, zone.timeZone)
