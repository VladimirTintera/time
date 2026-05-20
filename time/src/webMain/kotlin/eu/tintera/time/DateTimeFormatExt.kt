package eu.tintera.time

import js.intl.DateTimeFormatOptions
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

fun DateTimeFormat.toOptions() = createOptions(
    yearOpt = when (dateFormat?.year) {
        YearFormat.FourDigits -> "numeric"
        YearFormat.TwoDigits -> "2-digit"
        null -> null
    },
    monthOpt = when (dateFormat?.month) {
        MonthFormat.FullName -> "long"
        MonthFormat.ShortName -> "short"
        MonthFormat.PaddedNumber -> "2-digit"
        MonthFormat.Number -> "numeric"
        null -> null
    },
    dayOpt = when (dateFormat?.day) {
        DayFormat.Normal -> "numeric"
        DayFormat.Padded -> "2-digit"
        null -> null
    },
    hourOpt = when (timeFormat?.hour) {
        HourFormat.AutoPadded -> "2-digit"
        HourFormat.Auto -> "numeric"
        else -> if (timeFormat?.hour != null) "numeric" else null
    },
    minuteOpt = when (timeFormat?.minute) {
        MinuteFormat.Padded -> "2-digit"
        MinuteFormat.Normal -> "numeric"
        null -> null
    },
    secondOpt = if (timeFormat?.includeMilliseconds == true || timeFormat?.includeSeconds == true) "2-digit" else null,
    fractionalDigits = if (timeFormat?.includeMilliseconds == true) 3 else 0,
    weekDayOpt = when (dateFormat?.weekDay) {
        WeekDayFormat.FullName -> "long"
        WeekDayFormat.ShortName -> "short"
        null -> null
    }
)

@OptIn(ExperimentalWasmJsInterop::class)
private fun createOptions(
    yearOpt: String?,
    monthOpt: String?,
    dayOpt: String?,
    weekDayOpt: String?,
    hourOpt: String?,
    minuteOpt: String?,
    secondOpt: String?,
    fractionalDigits: Int
): DateTimeFormatOptions = js(
    """
    {
        const opts = {};
        if (yearOpt !== null) opts.year = yearOpt;
        if (monthOpt !== null) opts.month = monthOpt;
        if (dayOpt !== null) opts.day = dayOpt;
        if (weekDayOpt !== null) opts.weekday = weekDayOpt;
        if (hourOpt !== null) opts.hour = hourOpt;
        if (minuteOpt !== null) opts.minute = minuteOpt;
        if (secondOpt !== null) opts.second = secondOpt;
        if (fractionalDigits > 0) opts.fractionalSecondDigits = fractionalDigits;
        return opts;
    }
"""
)