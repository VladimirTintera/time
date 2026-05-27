# Time Library

A Kotlin Multiplatform library for elegant and flexible date, time, interval, and relative time formatting, built on top of `kotlinx-datetime`.

## Core Philosophy

This library is designed to be **lightweight** and performant across multiple platforms by leveraging native platform capabilities:
- **Android**: Uses the system's native `android.icu` library.
- **iOS / macOS / watchOS / tvOS**: Uses the native Foundation framework (`NSLocale`, `NSDateFormatter`, etc.).
- **JS / Wasm**: Uses the standard JavaScript `Intl` API.

**Exception**: On **JVM**, Java's standard library does not package native ICU/CLDR formatting resources in a reliable cross-platform way. Therefore, the JVM target includes a dependency on IBM's **ICU4J** (`com.ibm.icu:icu4j`) to guarantee correct, standard-compliant formatting.

---

## Features
- **DSL-based formatting**: Construct complex formats safely and readably using a Kotlin DSL.
- **Extensions for `kotlinx-datetime` types**: Easily format `LocalDate`, `LocalTime`, `LocalDateTime`, and `Instant`.
- **Interval formatting**: Smartly format time intervals, automatically omitting redundant information (e.g., "Tuesday 19 – Wednesday 20 May 2026").
- **Relative time formatting**: Human-readable relative times in various styles (e.g., "5 minutes ago", "in 2 hours").
- **Locale-aware**: Leverages native platform capabilities for correct, localized formatting and CLDR patterns. Supports specifying custom locales using `AppLocale`.

---

## Module Architecture

The library is split into separate modules to keep the core light and allow opt-in support for Kotlin's context parameters:

* **`:locale`**: Mapped to native platform locale types (`AppLocale`).
* **`:locale-context`**: Provides `LocaleContext` to manage the active locale context.
* **`:time:core`**: Extensions for `kotlinx-datetime` types supporting calendar arithmetic and sequence generation (e.g., `LocalDateTime.plus`, `generateSequence`, `modify` builder).
* **`:time:core-context`**: Context-receiver extensions for timezone-dependent functions (uses `TimeZoneContext`).
* **`:time:format`**: The main DSL formatting API for formatting dates, times, intervals, and relative times.
* **`:time:format-context`**: Context-receiver extensions for the formatting APIs. Integrates `LocaleContext` and `TimeZoneContext` to auto-resolve locale and timezone formatting parameters.

---

## Usage Examples

### Context-Aware Calculations & Formatting (Context Parameters)

With the context-aware modules, you can define a scope with implicit locale and timezone contexts, removing the need to pass them explicitly to every function call:

```kotlin
import eu.tintera.locale.context.*
import eu.tintera.time.core.context.*
import eu.tintera.time.format.context.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

// 1. Define the contexts
val czechLocale = localeContextOf(localeForLangCode("cs-CZ"))
val pragueTimeZone = timeZoneContextOf(TimeZone.of("Europe/Prague"))

// 2. Execute within the context scope
context(czechLocale, pragueTimeZone) {
    val instant = Instant.parse("2026-05-27T10:00:00Z")
    
    // toLocalDateTime resolves the TimeZoneContext automatically
    val localDateTime = instant.toLocalDateTime() 
    
    // format resolves the LocaleContext automatically
    val formattedDate = localDateTime.format {
        full()
    } // "středa 27. května 2026"
    
    // modify resolves the TimeZoneContext automatically
    val modifiedDateTime = localDateTime.modify {
        plusDays(1)
        plusTime(LocalTime(12, 0))
    }
}
```


### Date Formatting
Format dates using predefined styles (`short()`, `medium()`, `long()`, `full()`) or custom configurations. You can also provide a specific `AppLocale` created via `localeForLangCode`:

```kotlin
import eu.tintera.time.format.*
import eu.tintera.locale.*
import kotlinx.datetime.LocalDate

val date = LocalDate(2025, 4, 15)
val defaultLocale = getCurrentLocale()

// Predefined full date format
val fullFormatted = date.format(locale = defaultLocale) {
    full()
} // e.g., "Tuesday, April 15, 2025" (depending on locale)

// Custom format
val customFormatted = date.format(locale = defaultLocale) {
    day = DayFormat.Numeric
    month = MonthFormat.Name.Short
    year = YearFormat.FourDigits
} // e.g., "15 Apr 2025"

// Custom locale
val czechLocale = localeForLangCode("cs-CZ")
val localizedDate = date.format(locale = czechLocale) {
    long()
} // e.g., "15. dubna 2025"
```

### Time Formatting
Format times easily, automatically handling 12/24 hour preferences based on the locale:

```kotlin
import eu.tintera.time.format.*
import eu.tintera.locale.*
import kotlinx.datetime.LocalTime

val time = LocalTime(14, 30)
val defaultLocale = getCurrentLocale()

val formattedTime = time.format(locale = defaultLocale) {
    short()
} // e.g., "2:30 PM" or "14:30" depending on system locale
```

### Date and Time Formatting
Combine date and time formatting using the `DateTimeFormat` builder DSL:

```kotlin
import eu.tintera.time.format.*
import eu.tintera.locale.*
import kotlinx.datetime.LocalDateTime

val dateTime = LocalDateTime(2025, 4, 15, 14, 30)
val defaultLocale = getCurrentLocale()

val formattedDateTime = dateTime.format(locale = defaultLocale) {
    date { short() }
    time { full() }
} // e.g., "4/15/25, 2:30:00 PM" (depending on locale)
```

### Intervals
Format intervals intelligently. The library automatically omits redundant information (like repeating the year or month if both dates fall in the same month/year):

```kotlin
import eu.tintera.time.format.*
import eu.tintera.locale.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

val start = LocalDateTime(2026, 5, 19, 14, 0)
val end = LocalDateTime(2026, 5, 20, 16, 30)
val defaultLocale = getCurrentLocale()
val defaultTimeZone = TimeZone.currentSystemDefault()

val intervalString = start.formatInterval(
    to = end,
    locale = defaultLocale,
    timeZone = defaultTimeZone
) {
    date {
        day = DayFormat.Numeric
        month = MonthFormat.Name.Full
        year = YearFormat.FourDigits
        weekDay = WeekDayFormat.FullName
    }
}
// e.g., "Tuesday 19 – Wednesday 20 May 2026"
```

### Relative Time
Format a time relative to another time (e.g., now) with custom thresholds and style settings:

```kotlin
import eu.tintera.time.format.*
import eu.tintera.locale.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.minutes

val now = Clock.System.now()
val past = now.minus(5.minutes)
val defaultLocale = getCurrentLocale()
val defaultTimeZone = TimeZone.currentSystemDefault()

val relative = past.formatRelative(
    now = now,
    timeZone = defaultTimeZone,
    locale = defaultLocale
) {
    style = FormatStyle.Full
    minutes() // Enable formatting in minutes
}
// e.g., "5 minutes ago" (or "před 5 minutami" in Czech)
```

### Utilities
Get localized names for days, months, and determine the first day of the week:

```kotlin
import eu.tintera.time.format.*
import eu.tintera.locale.*
import kotlinx.datetime.Month
import kotlinx.datetime.DayOfWeek

val defaultLocale = getCurrentLocale()

val monthName = Month.APRIL.formatName(locale = defaultLocale, format = MonthFormat.Name.Full) // "April"
val dayName = DayOfWeek.MONDAY.formatName(locale = defaultLocale, format = WeekDayFormat.ShortName) // "Mon"
val firstDay = getFirstDayOfWeek() // e.g., DayOfWeek.SUNDAY or DayOfWeek.MONDAY
```