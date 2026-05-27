package eu.tintera.time.core

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Extracts the date portion of this [DateTimePeriod] as a [DatePeriod].
 *
 * This includes the years, months, and days from this period.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(years = 1, months = 2, days = 3, hours = 4)
 * val datePart = period.datePeriod
 * ```
 */
val DateTimePeriod.datePeriod: DatePeriod
    get() = DatePeriod(
        years = years,
        months = months,
        days = days
    )

/**
 * Extracts the time portion of this [DateTimePeriod] as a Kotlin [Duration].
 *
 * This calculates the duration represented by the hours, minutes, seconds,
 * and nanoseconds components of this period.
 *
 * Example:
 * ```kotlin
 * val period = DateTimePeriod(hours = 2, minutes = 30)
 * val durationPart = period.timeDuration
 * ```
 */
val DateTimePeriod.timeDuration: Duration
    get() = hours.toDuration(DurationUnit.HOURS) +
            minutes.toDuration(DurationUnit.MINUTES) +
            seconds.toDuration(DurationUnit.SECONDS) +
            nanoseconds.toDuration(DurationUnit.NANOSECONDS)
