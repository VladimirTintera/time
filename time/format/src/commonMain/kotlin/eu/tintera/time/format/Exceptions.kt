package eu.tintera.time.format

/**
 * Base exception for all formatting related errors in the library.
 */
open class FormattingException(
    message: String,
    cause: Throwable? = null
) : IllegalArgumentException(message, cause)

/**
 * Thrown when a format configuration is completely empty (no components selected to format).
 */
class EmptyFormatConfigurationException(message: String) : FormattingException(message)