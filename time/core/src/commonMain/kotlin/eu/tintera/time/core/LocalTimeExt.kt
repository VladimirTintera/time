package eu.tintera.time.core

import kotlinx.datetime.LocalTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val LocalTime.duration
    get() = hour.toDuration(DurationUnit.HOURS) +
            minute.toDuration(DurationUnit.MINUTES) +
            second.toDuration(DurationUnit.SECONDS) +
            nanosecond.toDuration(DurationUnit.NANOSECONDS)