package eu.tintera.time.core.context

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

context(zone: TimeZoneContext)
fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(zone.timeZone)