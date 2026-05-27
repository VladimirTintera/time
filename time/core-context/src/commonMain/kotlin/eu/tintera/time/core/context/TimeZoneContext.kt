package eu.tintera.time.core.context

import kotlinx.datetime.TimeZone

interface TimeZoneContext {
    val timeZone: TimeZone
}

internal data class TimeZoneContextImpl(
    override val timeZone: TimeZone
) : TimeZoneContext


fun timeZoneContextOf(timeZone: TimeZone): TimeZoneContext = TimeZoneContextImpl(timeZone)
fun systemDefaultTimeZoneContext(): TimeZoneContext = timeZoneContextOf(TimeZone.currentSystemDefault())