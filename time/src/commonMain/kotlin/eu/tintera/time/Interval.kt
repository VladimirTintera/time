package eu.tintera.time

import kotlinx.datetime.LocalDateTime

/**
 * Formats a time interval between two [LocalDateTime] instances into a human-readable string.
 *
 * This function intelligently handles various scenarios to produce a concise and natural-sounding
 * representation of the time range. For example, if the start and end times are on the same day,
 * the date is not repeated.
 *
 * @param from The starting [LocalDateTime] of the interval.
 * @param to The ending [LocalDateTime] of the interval.
 * @param format The [DateTimeFormat] to use for formatting the interval.
 * @return A string representing the formatted time interval.
 */
fun formatInterval(
    from: LocalDateTime,
    to: LocalDateTime,
    format: DateTimeFormat
): String {
    // Ensure that 'start' is always before or equal to 'end'
    val start = if (from <= to) from else to
    val end = if (from <= to) to else from

    // If the interval is a single point in time, format it as a single date-time
    if (start == end) {
        return formatDateTime(start, format)
    }

    val dateCfg = format.dateFormat

    // --- SCENARIO: Same month and year ---
    if (start.year == end.year && start.month == end.month) {

        // 1. Same day, different time (e.g., "May 19, 2026, 2:00 PM – 4:30 PM")
        if (start.day == end.day) {
            val datePart = formatDateTime(start, dateTimeFormat { date { from(dateCfg ?: return@date) }; timeFormat = null })
            val timeStartPart = formatDateTime(start, dateTimeFormat { dateFormat = null; time { from(format.timeFormat ?: return@time) } })
            val timeEndPart = formatDateTime(end, dateTimeFormat { dateFormat = null; time { from(format.timeFormat ?: return@time) } })

            return if (datePart.isEmpty()) {
                "$timeStartPart – $timeEndPart"
            } else {
                "$datePart, $timeStartPart – $timeEndPart"
            }
        }

        // 2. Different days within the same month (e.g., "Tuesday 19 – Wednesday 20 May 2026")
        if (dateCfg != null) {
            // The end part gets the full format
            val endPart = formatDateTime(end, format)

            // The start part only needs the weekday and day of the month
            val startFormat = dateTimeFormat {
                date {
                    day = dateCfg.day
                    weekDay = dateCfg.weekDay
                    month = null // Omit month and year to avoid repetition
                    year = null
                }
            }
            var startPart = formatDateTime(start, startFormat).trim()

            // Add a period if the day format is 'Normal' and the last character is a digit
            if (startPart.isNotEmpty() && startPart.last().isDigit() && dateCfg.day == DayFormat.Normal) {
                startPart += "."
            }

            return "$startPart – $endPart"
        }
    }

    // --- SCENARIO: Different months or years ---
    // In this case, repetition is unavoidable. The safest approach is to format both dates fully.
    val startFull = formatDateTime(start, format)
    val endFull = formatDateTime(end, format)
    return "$startFull – $endFull"
}