package eu.tintera.time.format.context

import eu.tintera.locale.AppLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

// =========================================================================
// AppLocale & TimeZone Dependent Formats
// =========================================================================

/**
 * Creates a [DateFormat] instance using the implicit context.
 */
context(locale: AppLocale, timeZone: TimeZone)
operator fun DateFormat.Companion.invoke(
    block: context(AppLocale, TimeZone) DateFormatScope<LocalDate>.() -> Unit = { DateFormatScope.defaultConfig<LocalDate>().invoke(this) }
): BaseDateFormat<LocalDate> = DateFormat {
    block()
}

/**
 * Creates a [TimeFormat] instance using the implicit context.
 */
context(locale: AppLocale, timeZone: TimeZone)
operator fun TimeFormat.Companion.invoke(
    block: context(AppLocale, TimeZone) TimeFormatScope<LocalTime>.() -> Unit = { TimeFormatScope.defaultConfig<LocalTime>().invoke(this) }
): BaseTimeFormat<LocalTime> = TimeFormat {
    block()
}

/**
 * Creates a [DateTimeFormat] instance using the implicit context.
 */
context(locale: AppLocale, timeZone: TimeZone)
operator fun DateTimeFormat.Companion.invoke(
    block: context(AppLocale, TimeZone) DateTimeFormatScope<LocalDateTime, LocalDate, LocalTime>.() -> Unit = {
        DateTimeFormatScope.defaultConfig<LocalDateTime, LocalDate, LocalTime>().invoke(this)
    }
): DateTimeFormat = DateTimeFormat {
    block()
}

/**
 * Creates a [DateIntervalFormat] instance using the implicit context.
 */
context(locale: AppLocale, timeZone: TimeZone)
operator fun DateIntervalFormat.Companion.invoke(
    block: context(AppLocale, TimeZone) DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = {
        DateFormatScope.defaultConfig<OpenEndRange<LocalDate>>().invoke(this)
    }
): DateIntervalFormat = DateIntervalFormat {
    block()
}

/**
 * Creates a [DateTimeIntervalFormat] instance using the implicit context.
 */
context(locale: AppLocale, timeZone: TimeZone)
operator fun DateTimeIntervalFormat.Companion.invoke(
    block: context(AppLocale, TimeZone) DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = {
        DateTimeFormatScope.defaultConfig<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>().invoke(this)
    }
): DateTimeIntervalFormat = DateTimeIntervalFormat {
    block()
}

/**
 * Creates a [RelativeDateTimeFormat] instance using the implicit context.
 */
context(locale: AppLocale, timeZone: TimeZone)
operator fun RelativeDateTimeFormat.Companion.invoke(
    block: context(AppLocale, TimeZone) RelativeDateTimeFormatScope.() -> Unit = {
        RelativeDateTimeFormatScope.defaultConfig.invoke(this)
    }
): RelativeDateTimeFormat = RelativeDateTimeFormat {
    block()
}

// =========================================================================
// AppLocale Only Dependent Formats
// =========================================================================

/**
 * Creates a [DatePeriodFormat] instance using the implicit context.
 */
context(locale: AppLocale)
operator fun DatePeriodFormat.Companion.invoke(
    block: context(AppLocale) DatePeriodFormatScope.() -> Unit = {
        DatePeriodFormatScope.defaultConfig.invoke(this)
    }
): DatePeriodFormat = DatePeriodFormat {
    block()
}

/**
 * Creates a [DateTimePeriodFormat] instance using the implicit context.
 */
context(locale: AppLocale)
operator fun DateTimePeriodFormat.Companion.invoke(
    block: context(AppLocale) DateTimePeriodFormatScope.() -> Unit = {
        DateTimePeriodFormatScope.defaultConfig.invoke(this)
    }
): DateTimePeriodFormat = DateTimePeriodFormat {
    block()
}

/**
 * Creates a [DurationFormat] instance using the implicit context.
 */
context(locale: AppLocale)
operator fun DurationFormat.Companion.invoke(
    block: context(AppLocale) DurationFormatScope.() -> Unit = {
        DurationFormatScope.defaultConfig.invoke(this)
    }
): DurationFormat = DurationFormat {
    block()
}

/**
 * Creates a [DurationDigitalFormat] instance using the implicit context.
 */
context(locale: AppLocale)
operator fun DurationDigitalFormat.Companion.invoke(
    block: context(AppLocale) DurationDigitalFormatScope.() -> Unit = {
        DurationDigitalFormatScope.defaultConfig.invoke(this)
    }
): DurationDigitalFormat = DurationDigitalFormat {
    block()
}
