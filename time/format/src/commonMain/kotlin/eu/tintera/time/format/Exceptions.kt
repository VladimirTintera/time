package eu.tintera.time.format

/**
 * Base exception for all formatting related errors in the library.
 *
 * Example:
 * ```kotlin
 * try {
 *     // some formatting action
 * } catch (e: FormattingException) {
 *     // handle exception
 * }
 * ```
 */
open class FormattingException(
    message: String,
    cause: Throwable? = null
) : IllegalArgumentException(message, cause)

/**
 * Thrown when a format configuration is completely empty (no components selected to format).
 *
 * Example:
 * ```kotlin
 * try {
 *     // some formatting action with empty config
 * } catch (e: EmptyFormatConfigurationException) {
 *     // handle exception
 * }
 * ```
 */
class EmptyFormatConfigurationException(message: String) : FormattingException(message)