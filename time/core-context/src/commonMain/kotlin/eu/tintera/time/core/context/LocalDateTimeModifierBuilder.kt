package eu.tintera.time.core.context

import eu.tintera.time.core.LocalDateTimeModifierBuilder
import eu.tintera.time.core.modify
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

/**
 * Modifies this [LocalDateTime] using the [LocalDateTimeModifierBuilder] DSL,
 * resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTime.modify(
    block: LocalDateTimeModifierBuilder.() -> Unit
): LocalDateTime = modify(zone.timeZone, block)

/**
 * Adds the duration of [time] to the current date-time within the builder,
 * resolved using the [TimeZoneContext] from the context.
 */
context(zone: TimeZoneContext)
fun LocalDateTimeModifierBuilder.plusTime(time: LocalTime) {
    plusTime(time, zone.timeZone)
}
