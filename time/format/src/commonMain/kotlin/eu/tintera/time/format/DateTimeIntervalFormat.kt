package eu.tintera.time.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime


class DateTimeIntervalFormat internal constructor(
    block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit
) : BaseDateTimeFormat<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>(block) {

    companion object {
        operator fun invoke(
            block: DateTimeFormatScope<OpenEndRange<LocalDateTime>, OpenEndRange<LocalDate>, OpenEndRange<LocalTime>>.() -> Unit = DateTimeFormatScope.defaultConfig()
        ): DateTimeIntervalFormat = DateTimeIntervalFormat(block)
    }
}
