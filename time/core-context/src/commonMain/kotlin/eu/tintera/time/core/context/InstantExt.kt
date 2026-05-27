package eu.tintera.time.core.context

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Converts this [Instant] to a [LocalDateTime] using the implicit [TimeZone] context.
 *
 * This function is context-aware and automatically uses the implicit [TimeZone] context
 * to perform the conversion.
 *
 * Example:
 * ```kotlin
 * val instant = Instant.parse("2025-04-15T12:00:00Z")
 * with(TimeZone.UTC) {
 *     val ldt = instant.toLocalDateTime()
 * }
 * ```
 *
 * @return A [LocalDateTime] representation of this instant in the contextual time zone.
 */
context(timeZone: TimeZone)
fun Instant.toLocalDateTime(): LocalDateTime = toLocalDateTime(timeZone)