package eu.tintera.time.core

import kotlinx.datetime.LocalTime
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

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
    get() = hour.hours + minute.minutes + second.seconds + nanosecond.nanoseconds
