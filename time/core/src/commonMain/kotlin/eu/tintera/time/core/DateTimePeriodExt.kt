package eu.tintera.time.core

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val DateTimePeriod.datePeriod: DatePeriod
    get() = DatePeriod(
        years = years,
        months = months,
        days = days
    )

val DateTimePeriod.timeDuration: Duration
    get() = hours.toDuration(DurationUnit.HOURS) +
            minutes.toDuration(DurationUnit.MINUTES) +
            seconds.toDuration(DurationUnit.SECONDS) +
            nanoseconds.toDuration(DurationUnit.NANOSECONDS)
