package eu.tintera.time.core

import kotlinx.datetime.LocalTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

/**
 * Extracts the duration of this [LocalTime] since the start of the day (00:00:00) as a Kotlin [kotlin.time.Duration].
 *
 * This calculates the duration represented by the hours, minutes, seconds,
 * and nanoseconds components of this local time.
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(12, 30)
 * val duration = time.duration
 * ```
 */
val LocalTime.duration
    get() = hour.toDuration(DurationUnit.HOURS) +
            minute.toDuration(DurationUnit.MINUTES) +
            second.toDuration(DurationUnit.SECONDS) +
            nanosecond.toDuration(DurationUnit.NANOSECONDS)