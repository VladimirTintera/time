package eu.tintera.time.format

import kotlinx.datetime.LocalDate

class DateIntervalFormat internal constructor(
    block: DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = DateFormatScope.defaultConfig()
) : BaseDateFormat<OpenEndRange<LocalDate>>(block) {
    companion object {
        operator fun invoke(
            block: DateFormatScope<OpenEndRange<LocalDate>>.() -> Unit = DateFormatScope.defaultConfig()
        ) : DateIntervalFormat = DateIntervalFormat(block)
    }
}