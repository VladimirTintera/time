package eu.tintera.time.format

/**
 * Defines the formatting style for the year component.
 *
 * Each style corresponds to a specific pattern for representing the year.
 */
enum class YearFormat(internal val pattern: String) {
    /**
     * Four-digit representation of the year.
     *
     * Example: 2026
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
sealed class MonthFormat {

    internal abstract val pattern: String

    sealed class Name : MonthFormat() {
        /**
         * Full name of the month.
         *
         * Example: January
         */
        data object Full : Name() {
            override val pattern: String
                get() = "MMMM"
        }

        /**
         * Abbreviated name of the month.
         *
         * Example: Jan
         */
        data object Short : Name() {
            override val pattern: String
                get() = "MMM"
        }
    }

    sealed class Digital : MonthFormat() {
        /**
         * Month number with a leading zero.
         *
         * Example: 01
         */
        data object Padded : Digital() {
            override val pattern: String
                get() = "MM"
        }

        /**
         * Month number without a leading zero.
         *
         * Example: 1
         */
        data object Numeric : Digital() {
            override val pattern: String
                get() = "M"
        }
    }
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
    Numeric("d")
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
 */
sealed class HourFormat {
    internal abstract val pattern: String

    /**
     * Styles for representing hours in 24-hour clock format.
     */
    sealed class Digital24h : HourFormat() {
        /**
         * Hour number in 24-hour format without a leading zero (0-23).
         *
         * Example: 9, 13
         */
        data object Numeric : Digital24h() {
            override val pattern = "H"
        }

        /**
         * Hour number in 24-hour format with a leading zero (00-23).
         *
         * Example: 09, 13
         */
        data object Padded : Digital24h() {
            override val pattern = "HH"
        }
    }

    /**
     * Styles for representing hours in a locale-preferred clock format (12-hour or 24-hour).
     */
    sealed class Auto : HourFormat() {
        /**
         * Hour number without a leading zero.
         *
         * Example: 9 AM, 1 PM, or 13:00 depending on the locale.
         */
        data object Numeric : Auto() {
            override val pattern = "j"
        }

        /**
         * Hour number with a leading zero.
         *
         * Example: 09 AM, 01 PM, or 13:00 depending on the locale.
         */
        data object Padded : Auto() {
            override val pattern = "jj"
        }
    }

    /**
     * Styles for representing hours in 12-hour clock format.
     */
    sealed class Digital12 : HourFormat() {
        /**
         * Hour number in 12-hour format without a leading zero (1-12).
         *
         * Example: 9
         */
        data object Numeric : Digital12() {
            override val pattern = "h"
        }

        /**
         * Hour number in 12-hour format with a leading zero (01-12).
         *
         * Example: 09
         */
        data object Padded : Digital12() {
            override val pattern = "hh"
        }
    }
}

/**
 * Defines whether to display the day period marker (AM/PM).
 */
enum class DayPeriodStyle {

    /**
     * Force-displays the AM/PM marker (which typically switches the hours to a 12-hour format).
     */
    Required,

    /**
     * Force-hides the AM/PM marker (where allowed by the platform and locale).
     */
    None
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
    Numeric("m")
}

/**
 * Defines the formatting style for the second component.
 *
 * Each style corresponds to a specific pattern for representing the second.
 */
enum class SecondFormat(internal val pattern: String) {
    /**
     * Second with a leading zero.
     *
     * Example: 09
     */
    Padded("ss"),

    /**
     * Second without a leading zero.
     *
     * Example: 9
     */
    Numeric("s")
}

/**
 * Defines the precision for the fractional seconds component (e.g., milliseconds).
 *
 * Each style specifies the number of decimal digits to display after the seconds.
 */
enum class FractionalSecondFormat(internal val pattern: String, internal val digits: Int) {
    /**
     * Display fractional seconds with three-digit precision (milliseconds).
     *
     * Example: .123
     */
    ThreeDigits("SSS", 3),

    /**
     * Display fractional seconds with two-digit precision.
     *
     * Example: .12
     */
    TwoDigits("SS", 2),

    /**
     * Display fractional seconds with one-digit precision.
     *
     * Example: .1
     */
    OneDigits("S", 1)
}
