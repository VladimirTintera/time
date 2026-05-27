package eu.tintera.time.core

/**
 * A DSL marker for the date-time formatting DSL.
 *
 * This annotation is used to prevent accidental nesting of DSL blocks,
 * ensuring that the DSL is used in a structured and predictable way.
 *
 * Example:
 * ```kotlin
 * @TimeDslMarker
 * class MyBuilder
 * ```
 */
@DslMarker
annotation class TimeDslMarker
