package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

/**
 * Formats this [LocalTime] into a string representation using the specified format.
 *
 * This function internally converts the [LocalTime] to a [LocalDateTime] (using a dummy date)
 * to apply the [TimeFormat].
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val format = TimeFormat {
 *     hour = HourFormat.Digital24h.Padded
 *     minute = MinuteFormat.Padded
 * }
 * val myLocale = localeForLanguageTag("en-US")
 * val formatted = time.format(format, myLocale)
 * // formatted will be "14:30" (depending on locale)
 * ```
 *
 * @param format The [TimeFormat] configuration to apply.
 * @param locale The [AppLocale] to use for formatting.
 * @return The formatted time string.
 */
@Deprecated(
    message = "Use the overload that explicitly requires a TimeZone, which is needed for contextual evaluation inside the format scope.",
    replaceWith = ReplaceWith("this.format(format, locale, TimeZone.currentSystemDefault())", "import kotlinx.datetime.TimeZone")
)
fun LocalTime.format(
    format: TimeFormat,
    locale: AppLocale
): String = format(
    format = format,
    locale = locale,
    timeZone = TimeZone.currentSystemDefault()
)

fun LocalTime.format(
    format: TimeFormat,
    locale: AppLocale,
    timeZone: TimeZone
): String = platformDateTimeFormat(
    date = LocalDateTime(
        year = 1970,
        month = 1,
        day = 1,
        hour = hour,
        minute = minute,
        second = second,
        nanosecond = nanosecond
    ),
    locale = locale,
    format = DateTimeFormat {
        time { from(format) }
    },
    dateRequired = false,
    timeRequired = true,
    timeZone = timeZone
)

/**
 * Formats this [LocalTime] into a string representation using a DSL-configured format.
 *
 * This function provides a convenient way to define a [TimeFormat] on-the-fly
 * using a DSL and apply it to the [LocalTime].
 *
 * Example:
 * ```kotlin
 * val time = LocalTime(14, 30)
 * val czLocale = localeForLanguageTag("cs-CZ")
 * val formatted = time.format(czLocale) {
 *     short()
 * }
 * // formatted will be "2:30 PM" or "14:30" depending on locale
 * ```
 *
 * @param locale The [AppLocale] to use for formatting.
 * @param block The DSL block for configuring the [TimeFormat].
 * @return The formatted time string.
 */
@Deprecated(
    message = "Use the overload that explicitly requires a TimeZone, which is needed for contextual evaluation inside the format scope.",
    replaceWith = ReplaceWith("this.format(locale, TimeZone.currentSystemDefault(), block)", "import kotlinx.datetime.TimeZone")
)
fun LocalTime.format(
    locale: AppLocale,
    block: TimeFormatScope<LocalTime>.() -> Unit = TimeFormatScope.defaultConfig()
) = format(
    locale = locale,
    timeZone = TimeZone.currentSystemDefault(),
    block = block
)

fun LocalTime.format(
    locale: AppLocale,
    timeZone: TimeZone,
    block: TimeFormatScope<LocalTime>.() -> Unit = TimeFormatScope.defaultConfig()
) = format(
    locale = locale,
    timeZone = timeZone,
    format = TimeFormat(block = block)
)

