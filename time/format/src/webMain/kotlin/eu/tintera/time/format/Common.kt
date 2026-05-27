package eu.tintera.time.format

import js.intl.*
import kotlin.js.JsAny
import kotlin.js.js

internal fun <T : JsAny> jsObject(): T = js("({})")

internal fun <T : JsAny> jsObject(block: T.() -> Unit): T = jsObject<T>().apply(block)

internal fun dateTimeFormatOptions(
    timeFormat: TimeFormat?,
    dateFormat: DateFormat?,
    timeZone: String
): DateTimeFormatOptions = jsObject {
    when (dateFormat?.year) {
        YearFormat.FourDigits -> js.intl.YearFormat.numeric
        YearFormat.TwoDigits -> js.intl.YearFormat.twoDigit
        null -> null
    }?.also { year = it }

    when (dateFormat?.month) {
        MonthFormat.Name.Full -> js.intl.MonthFormat.long
        MonthFormat.Name.Short -> js.intl.MonthFormat.short
        MonthFormat.Digital.Padded -> js.intl.MonthFormat.twoDigit
        MonthFormat.Digital.Numeric -> js.intl.MonthFormat.numeric
        null -> null
    }?.also { month = it }

    when (dateFormat?.day) {
        DayFormat.Numeric -> js.intl.DayFormat.numeric
        DayFormat.Padded -> js.intl.DayFormat.twoDigit
        null -> null
    }?.also { day = it }

    when (timeFormat?.hour) {
        HourFormat.Auto.Numeric, HourFormat.Digital12.Numeric, HourFormat.Digital24h.Numeric -> js.intl.HourFormat.numeric
        HourFormat.Auto.Padded, HourFormat.Digital12.Padded, HourFormat.Digital24h.Padded -> js.intl.HourFormat.twoDigit
        null -> null
    }?.also { hour = it }


    when (timeFormat?.minute) {
        MinuteFormat.Padded -> js.intl.MinuteFormat.twoDigit
        MinuteFormat.Numeric -> js.intl.MinuteFormat.numeric
        null -> null
    }?.also { minute = it }


    when (timeFormat?.second) {
        SecondFormat.Padded -> js.intl.SecondFormat.twoDigit
        SecondFormat.Numeric -> js.intl.SecondFormat.numeric
        null -> null
    }?.also { second = it }

    if (timeFormat?.second != null) {
        timeFormat.fractionalSecond?.also {
            fractionalSecondDigits = it.digits
        }
    }

    when (dateFormat?.weekDay) {
        WeekDayFormat.FullName -> WeekdayFormat.long
        WeekDayFormat.ShortName -> WeekdayFormat.short
        null -> null
    }?.also { weekday = it }


    when (timeFormat?.periodStyle) {
        DayPeriodStyle.Required -> true
        DayPeriodStyle.None -> false
        else -> null
    }?.also { hour12 = it }

    this.timeZone = timeZone
}