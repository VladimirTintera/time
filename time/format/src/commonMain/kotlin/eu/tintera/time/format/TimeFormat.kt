package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker
import kotlinx.datetime.LocalTime

/**
 * Encapsulates a formatting configuration for time values.
 *
 * Example:
 * ```kotlin
 * val format = TimeFormat {
 *     hour = HourFormat.Auto.Numeric
 *     minute = MinuteFormat.Padded
 * }
 * ```
 *
 * @param block The configuration block applied to the [TimeFormatScope] of [LocalTime].
 */
class TimeFormat internal constructor(
    block: TimeFormatScope<LocalTime>.() -> Unit
) : BaseTimeFormat<LocalTime>(block) {
    companion object {
        /**
         * Creates a [TimeFormat] using the specified configuration block.
         *
         * Example:
         * ```kotlin
         * val format = TimeFormat {
         *     short()
         * }
         * ```
         *
         * @param block The configuration block applied to the [TimeFormatScope] of [LocalTime].
         * @return A new [BaseTimeFormat] instance.
         */
        operator fun invoke(
            block: TimeFormatScope<LocalTime>.() -> Unit = TimeFormatScope.defaultConfig()
        ): BaseTimeFormat<LocalTime> = TimeFormat(block)
    }
}

/**
 * Base configuration class for localized time formatting configurations.
 *
 * @param T The type of value being formatted.
 * @param block The configuration block applied to [TimeFormatScope].
 */
abstract class BaseTimeFormat<T : Any> internal constructor(
    val block: TimeFormatScope<T>.() -> Unit
)


/**
 * Scope class for configuring [TimeFormat] instances using a DSL.
 *
 * This class provides a flexible way to configure a [TimeFormat] by specifying
 * the desired format for each time component. It also includes predefined styles
 * for convenience.
 *
 * Example:
 * ```kotlin
 * val format = TimeFormat {
 *     hour = HourFormat.Auto.Numeric
 *     minute = MinuteFormat.Padded
 * }
 * ```
 */
@TimeDslMarker
class TimeFormatScope<T : Any> internal constructor(
    override val value: T,
    override val locale: AppLocale
) : FormatScope<T> {

    /** The format style for the hour component, or null if omitted. */
    var hour: HourFormat? = null

    /** The format style for the minute component, or null if omitted. */
    var minute: MinuteFormat? = null

    /** The format style for the second component, or null if omitted. */
    var second: SecondFormat? = null

    /** The format style for the fractional second component, or null if omitted. */
    var fractionalSecond: FractionalSecondFormat? = null

    /** Indicates whether to include the AM/PM marker in the formatted output. */
    var periodStyle: DayPeriodStyle? = null

    /**
     * Copies the configuration from an existing [TimeFormat].
     *
     * This allows for easily extending or modifying a predefined format.
     *
     * Example:
     * ```kotlin
     * val existingFormat = TimeFormat { short() }
     * val format = TimeFormat {
     *     from(existingFormat)
     *    second = SecondFormat.Padded
     * }
     * ```
     *
     * @param timeFormat The format to copy from.
     */
    fun from(timeFormat: BaseTimeFormat<T>) {
        return timeFormat.block(this)
    }

    /**
     * Applies a short time format.
     *
     * This typically includes an automatic hour format and a padded minute.
     *
     * Example:
     * ```kotlin
     * val format = TimeFormat {
     *     short()
     * }
     * ```
     */
    fun short() {
        hour = HourFormat.Auto.Numeric
        minute = MinuteFormat.Padded
    }

    /**
     * Applies a full time format.
     *
     * This typically includes an automatic hour format, a padded minute, and seconds.
     *
     * Example:
     * ```kotlin
     * val format = TimeFormat {
     *     full()
     * }
     * ```
     */
    fun full() {
        hour = HourFormat.Auto.Numeric
        minute = MinuteFormat.Padded
        second = SecondFormat.Padded
    }

    fun cldrSkeleton(): String = buildString {

        when (periodStyle) {
            DayPeriodStyle.Required -> {
                when (hour) {
                    HourFormat.Auto.Numeric -> append(HourFormat.Digital12.Numeric.pattern)
                    HourFormat.Auto.Padded -> append(HourFormat.Digital12.Padded.pattern)
                    else -> hour?.also { append(it.pattern) }
                }
                append("a")
            }

            DayPeriodStyle.None -> hour?.also {
                when (it) {
                    HourFormat.Auto.Numeric, HourFormat.Digital12.Numeric -> append(HourFormat.Digital24h.Numeric.pattern)
                    HourFormat.Auto.Padded, HourFormat.Digital12.Padded -> append(HourFormat.Digital24h.Padded.pattern)
                    else -> append(it.pattern)
                }
            }

            else -> hour?.also { append(it.pattern) }
        }

        minute?.also { append(it.pattern) }
        second?.also {
            append(it.pattern)
            fractionalSecond?.also { fraction ->
                append(".")
                append(fraction.pattern)
            }
        }
    }

    fun isEmpty() = hour == null && minute == null && second == null

    companion object {
        /**
         * The default configuration block for [TimeFormatScope].
         *
         * By default, it sets the time format to short style.
         *
         * Example:
         * ```kotlin
         * val config = TimeFormatScope.defaultConfig<kotlinx.datetime.LocalTime>()
         * ```
         */
        fun <T : Any> defaultConfig(): TimeFormatScope<T>.() -> Unit = {
            short()
        }
    }
}