package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Duration

/**
 * Formats this [Duration] into a localized text representation.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 * import kotlin.time.Duration.Companion.minutes
 *
 * val duration = 2.hours + 30.minutes
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = duration.format(
 *     format = DurationFormat {
 *         hours = UnitVisibility.Required
 *         minutes = UnitVisibility.Required
 *     },
 *     locale = myLocale
 * )
 * // e.g. "2 hours, 30 minutes"
 * ```
 *
 * @param format The [DurationFormat] configuration specifying style and unit visibility.
 * @param locale The [AppLocale] to use.
 * @return The formatted duration string.
 */
fun Duration.format(
    format: DurationFormat,
    locale: AppLocale
): String = platformDurationFormat(
    duration = this,
    format = format,
    locale = locale
)

/**
 * Formats this [Duration] into a localized text representation using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 *
 * val duration = 2.hours
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = duration.format(locale = czLocale) {
 *     hours = UnitVisibility.Required
 * }
 * // e.g., "2 hours"
 * ```
 *
 * @param locale The [AppLocale] to use.
 * @param block The configuration block applied to the [DurationFormatScope].
 * @return The formatted duration string.
 */
fun Duration.format(
    locale: AppLocale,
    block: DurationFormatScope.() -> Unit = DurationFormatScope.defaultConfig
): String = format(
    format = DurationFormat(block),
    locale = locale
)

/**
 * Formats this [Duration] into a digital clock-style string representation.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.hours
 * import kotlin.time.Duration.Companion.minutes
 * import kotlin.time.Duration.Companion.seconds
 *
 * val duration = 1.hours + 23.minutes + 45.seconds
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = duration.formatDigital(
 *     format = DurationDigitalFormat {
 *         stopwatch()
 *     },
 *     locale = myLocale
 * )
 * // e.g., "01:23:45"
 * ```
 *
 * @param format The [DurationDigitalFormat] configuration.
 * @param locale The [AppLocale] to use.
 * @return The formatted digital duration string.
 */
fun Duration.formatDigital(
    format: DurationDigitalFormat,
    locale: AppLocale
): String = digitalFormat(
    duration = this,
    format = format,
    locale = locale
)

/**
 * Formats this [Duration] into a digital clock-style string representation using a DSL-configured format.
 *
 * Example:
 * ```kotlin
 * import kotlin.time.Duration.Companion.minutes
 * import kotlin.time.Duration.Companion.seconds
 *
 * val duration = 12.minutes + 30.seconds
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = duration.formatDigital(locale = czLocale) {
 *     stopwatch()
 * }
 * // e.g., "12:30" (or "00:12:30" depending on stopwatch config)
 * ```
 *
 * @param locale The [AppLocale] to use.
 * @param block The configuration block applied to the [DurationDigitalFormatScope].
 * @return The formatted digital duration string.
 */
fun Duration.formatDigital(
    locale: AppLocale,
    block: DurationDigitalFormatScope.() -> Unit = DurationDigitalFormatScope.defaultConfig
): String = digitalFormat(
    duration = this,
    format = DurationDigitalFormat(block),
    locale = locale
)

private fun digitalFormat(
    duration: Duration,
    format: DurationDigitalFormat,
    locale: AppLocale,
): String {

    val scope = DurationDigitalFormatScope(duration, locale)
    format.block(scope)

    if (scope.day == null && scope.hour == null && scope.minute == null && scope.second == null)
        throw EmptyFormatConfigurationException("Duration format cannot be empty. At least one duration component must be configured.")

    return duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
        buildList {
            scope.day?.takeIf { days > 0 }?.also { dayStyle ->
                val formattedDays = duration.format(locale) {
                    style = dayStyle
                    this.days = UnitVisibility.Required
                }
                add(formattedDays)
            }

            val time = LocalTime(hours, minutes, seconds, nanoseconds)

            add(
                time.format(locale, TimeZone.UTC) {
                    hour = scope.hour
                    minute = scope.minute
                    second = scope.second
                    fractionalSecond = scope.fractionalSecond
                    periodStyle = DayPeriodStyle.None
                }
            )
        }.joinToString(separator = scope.separator)
    }
}

internal fun platformDurationFormat(
    duration: Duration,
    format: DurationFormat,
    locale: AppLocale
): String {

    val scope = DurationFormatScope(
        value = duration,
        locale = locale,
    )

    format.block(scope)

    if (scope.days == null && scope.hours == null && scope.minutes == null && scope.seconds == null)
        throw EmptyFormatConfigurationException("Duration format cannot be empty. At least one duration component must be configured.")

    val measurables = duration.toComponents { days, hours, minutes, seconds, nanoseconds ->
        val millis = nanoseconds / 1_000_000
        buildList {
            scope.days.ifAvailable({ days != 0L }) { add(Measurable(MeasureUnit.DAYS, days.toInt())) }
            scope.hours.ifAvailable({ hours != 0 }) { add(Measurable(MeasureUnit.HOURS, hours)) }
            scope.minutes.ifAvailable({ minutes != 0 }) { add(Measurable(MeasureUnit.MINUTES, minutes)) }
            scope.seconds.ifAvailable({ seconds != 0 }) { add(Measurable(MeasureUnit.SECONDS, seconds)) }
            scope.fractionalSeconds.ifAvailable({ millis != 0 }) {
                add(
                    Measurable(
                        MeasureUnit.FRACTIONAL_SECONDS,
                        millis
                    )
                )
            }
        }
    }

    return nativeDurationFormat(
        measurables = measurables,
        style = scope.style,
        locale = locale
    ).joinToString(", ")
}

internal expect fun nativeDurationFormat(
    measurables: List<Measurable>,
    style: FormatStyle,
    locale: AppLocale
): List<String>

internal fun <T> UnitVisibility?.ifAvailable(isAutoValid: () -> Boolean, action: () -> T) {
    val isValid = when (this) {
        UnitVisibility.Auto -> isAutoValid()
        UnitVisibility.Required -> true
        null -> false
    }

    if (isValid) action()
}