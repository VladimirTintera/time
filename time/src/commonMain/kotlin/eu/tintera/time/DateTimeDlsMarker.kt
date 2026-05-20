package eu.tintera.time

/**
 * A DSL marker for the date-time formatting DSL.
 *
 * This annotation is used to prevent accidental nesting of DSL blocks,
 * ensuring that the DSL is used in a structured and predictable way.
 */
@DslMarker
annotation class DateTimeDslMarker
