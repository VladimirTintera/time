package eu.tintera.time.format

import eu.tintera.locale.AppLocale
import eu.tintera.time.core.TimeDslMarker

@TimeDslMarker
class DateTimeFormatScope<T : Any, TDate : Any, TTime : Any> internal constructor(
    override val value: T,
    internal val date: TDate,
    internal val time: TTime,
    override val locale: AppLocale
) : FormatScope<T> {
    internal var dateFormatScope: DateFormatScope<TDate> = DateFormatScope(date, locale)
    internal var timeFormatScope: TimeFormatScope<TTime> = TimeFormatScope(time, locale)

    fun date(block: DateFormatScope<TDate>.() -> Unit = DateFormatScope.defaultConfig()) {
        dateFormatScope.block()
    }

    fun time(block: TimeFormatScope<TTime>.() -> Unit = TimeFormatScope.defaultConfig()) {
        timeFormatScope.block()
    }

    fun from(
        dateTimeFormat: BaseDateTimeFormat<T, TDate, TTime>
    ) {
        dateTimeFormat.block(this)
    }

    fun cldrSkeleton(): String = buildString {
        dateFormatScope.cldrSkeleton().takeIf { it.isNotEmpty() }?.also { append(it) }
        timeFormatScope.cldrSkeleton().takeIf { it.isNotEmpty() }?.also { append(it) }
    }

    companion object {
        fun <T : Any, TDate : Any, TTime : Any> defaultConfig(): DateTimeFormatScope<T, TDate, TTime>.() -> Unit = {
            date()
            time()
        }
    }
}