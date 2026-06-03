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
 *
 * @param message The detail message explaining the cause of the formatting error.
 * @param cause The underlying cause of the exception, if any.
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
 *
 * @param message The detail message explaining why the format configuration was empty.
 */
class EmptyFormatConfigurationException(message: String) : FormattingException(message)