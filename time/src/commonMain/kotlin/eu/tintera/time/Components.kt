package eu.tintera.time


/**
 * Defines the formatting style for the year component.
 *
 * Each style corresponds to a specific pattern for representing the year.
 */
enum class YearFormat(internal val pattern: String) {
    /**
     * Four-digit representation of the year.
     *
     * Example: 2025
     */
    FourDigits("yyyy"),

    /**
     * Two-digit representation of the year.
     *
     * Example: 25
     */
    TwoDigits("yy")
}

/**
 * Defines the formatting style for the month component.
 *
 * Each style corresponds to a specific pattern for representing the month.
 */
enum class MonthFormat(internal val pattern: String) {
    /**
     * Full name of the month.
     *
     * Example: January
     */
    FullName("MMMM"),

    /**
     * Abbreviated name of the month.
     *
     * Example: Jan
     */
    ShortName("MMM"),

    /**
     * Month number with a leading zero.
     *
     * Example: 01
     */
    PaddedNumber("MM"),

    /**
     * Month number without a leading zero.
     *
     * Example: 1
     */
    Number("M")
}

/**
 * Defines the formatting style for the day of the month component.
 *
 * Each style corresponds to a specific pattern for representing the day.
 */
enum class DayFormat(internal val pattern: String) {
    /**
     * Day number with a leading zero.
     *
     * Example: 04
     */
    Padded("dd"),

    /**
     * Day number without a leading zero.
     *
     * Example: 4
     */
    Normal("d")
}

/**
 * Defines the formatting style for the day of the week component.
 *
 * Each style corresponds to a specific pattern for representing the day of the week.
 */
enum class WeekDayFormat(internal val pattern: String) {
    /**
     * Full name of the day.
     *
     * Example: Monday
     */
    FullName("EEEE"),

    /**
     * Abbreviated name of the day.
     *
     * Example: Mon
     */
    ShortName("E")
}

/**
 * Defines the formatting style for the hour component.
 *
 * Each style corresponds to a specific pattern for representing the hour.
 */
enum class HourFormat(internal val pattern: String) {
    /**
     * Hour in 12 or 24-hour format, depending on system settings.
     *
     * Example: 15 or 3
     */
    Auto("j"),

    /**
     * Hour in 12 or 24-hour format with a leading zero, depending on system settings.
     *
     * Example: 15 or 03
     */
    AutoPadded("jj"),

    /**
     * Hour in 24-hour format.
     *
     * Example: 03, 15
     */
    Military24("HH"),

    /**
     * Hour in 12-hour format.
     *
     * Example: 3
     */
    AmPm12("h")
}

/**
 * Defines the formatting style for the minute component.
 *
 * Each style corresponds to a specific pattern for representing the minute.
 */
enum class MinuteFormat(internal val pattern: String) {
    /**
     * Minute with a leading zero.
     *
     * Example: 05
     */
    Padded("mm"),

    /**
     * Minute without a leading zero.
     *
     * Example: 5
     */
    Normal("m")
}
