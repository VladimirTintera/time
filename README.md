# Time Library

A Kotlin Multiplatform library for elegant and flexible date, time, interval, and relative time formatting, built on top of `kotlinx-datetime`.

## Features
- **DSL-based formatting**: Construct complex formats safely and readably using a Kotlin DSL.
- **Extensions for `kotlinx-datetime` types**: Easily format `LocalDate`, `LocalTime`, `LocalDateTime`, and `Instant`.
- **Interval formatting**: Smartly format time intervals, automatically omitting redundant information (e.g., "Tuesday 19 – Wednesday 20 May 2026").
- **Relative time formatting**: Human-readable relative times in various styles (e.g., "5 minutes ago", "in 2 hours").
- **Locale-aware**: Leverages native platform capabilities for correct, localized formatting and CLDR patterns.

## Usage Examples

### Date Formatting
Format dates using predefined styles (`short()`, `medium()`, `long()`, `full()`) or custom configurations:

```kotlin
import eu.tintera.time.*
import kotlinx.datetime.LocalDate

val date = LocalDate(2025, 4, 15)

// Predefined full date format
val fullFormatted = date.format {
    full()
} // e.g., "Tuesday, April 15, 2025"

// Custom format
val customFormatted = date.format {
    day = DayFormat.Normal
    month = MonthFormat.ShortName
    year = YearFormat.FourDigits
} // e.g., "15 Apr 2025"
```

### Time Formatting
Format times easily handling 12/24 hour preferences automatically:

```kotlin
import eu.tintera.time.*
import kotlinx.datetime.LocalTime

val time = LocalTime(14, 30)

val formattedTime = time.format {
    short()
} // e.g., "2:30 PM" or "14:30" depending on system locale
```

### Date and Time Formatting
Combine date and time formatting using the `dateTimeFormat` builder:

```kotlin
import eu.tintera.time.*
import kotlinx.datetime.LocalDateTime

val dateTime = LocalDateTime(2025, 4, 15, 14, 30)

val formattedDateTime = dateTime.format {
    date { short() }
    time { full() }
}
```

### Intervals
Format intervals intelligently. The library automatically omits redundant information (like repeating the year or month if both dates are in the same month/year):

```kotlin
import eu.tintera.time.*
import kotlinx.datetime.LocalDateTime

val start = LocalDateTime(2026, 5, 19, 14, 0)
val end = LocalDateTime(2026, 5, 20, 16, 30)

val intervalString = start.formatInterval(end) {
    date {
        day = DayFormat.Normal
        month = MonthFormat.FullName
        year = YearFormat.FourDigits
        weekDay = WeekDayFormat.FullName
    }
}
// e.g., "Tuesday 19 – Wednesday 20 May 2026"
```

### Relative Time
Format a time relative to another time (e.g., now):

```kotlin
import eu.tintera.time.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

val now = Clock.System.now()
val past = now.minus(5.minutes)

val relative = past.formatRelative(now = now, style = RelativeUnitStyle.Full)
// e.g., "5 minutes ago" (or "před 5 minutami" in Czech)
```

### Utilities
Get localized names for days, months, and determine the first day of the week:

```kotlin
import eu.tintera.time.*
import kotlinx.datetime.Month
import kotlinx.datetime.DayOfWeek

val monthName = Month.APRIL.formatName(abbrev = false) // "April"
val dayName = DayOfWeek.MONDAY.formatName(abbrev = true) // "Mon"
val firstDay = getFirstDayOfWeek() // e.g., DayOfWeek.SUNDAY or DayOfWeek.MONDAY
```
