# Time Library

A premium Kotlin Multiplatform library for elegant, type-safe, and locale-aware date, time, interval, relative time, duration, and period formatting. Built directly on top of `kotlinx-datetime`, it offers flexible formatting DSLs and native platform performance.

📖 **Full API Documentation**: [vladimirtintera.github.io/time](https://vladimirtintera.github.io/time/)

---

## Core Philosophy

This library is designed to be **lightweight** and performant across multiple platforms by leveraging native platform capabilities:
- **Android**: Uses the system's native `android.icu` library.
- **iOS / macOS / watchOS / tvOS**: Uses the native Foundation framework (`NSLocale`, `NSDateFormatter`, etc.).
- **JS / Wasm**: Uses the standard JavaScript `Intl` API.
- **JVM**: On the JVM, Java's standard library does not bundle native ICU/CLDR formatting resources in a reliable cross-platform way. Therefore, the JVM target includes a dependency on IBM's **ICU4J** (`com.ibm.icu:icu4j`) to guarantee correct, standard-compliant formatting.

---

## Features

- 🛠️ **DSL-based formatting**: Safe, readable configuration of dates, times, durations, and periods using Kotlin DSL builders.
- 📆 **Extensions for `kotlinx-datetime` types**: Easily format `LocalDate`, `LocalTime`, `LocalDateTime`, and `Instant`.
- 🔀 **Interval formatting**: Smartly format date and time ranges, automatically merging redundant information (e.g., "Tuesday 19 – Wednesday 20 May 2026").
- ⏱️ **Relative time formatting**: Human-readable differences in various styles (e.g., "5 minutes ago", "in 2 hours").
- 🌍 **Locale-aware**: Leverages native platform capabilities for correct, localized formatting and CLDR patterns. Custom locales are represented by the platform-mapped `AppLocale`.
- 🧬 **Context Parameter support**: Opt-in support for Kotlin's context parameters to automatically propagate timezone and locale contexts.

---

## Module Architecture

The library is split into separate modules to keep the core light and allow opt-in support for Kotlin's context parameters:

| Module | Purpose | Key API & Mappings |
| :--- | :--- | :--- |
| **`:locale`** | Native platform locale abstraction. | `AppLocale` (JVM: `java.util.Locale`, Android: `android.icu.util.ULocale`, Apple: `NSLocale`, Web: `String`), `currentLocale`, `availableLocales()`, `localeForLanguageTag(tag)` |
| **`:time:core`** | Calendar calculations and sequence utilities. | timezone-aware extensions, sequence generation (`generateSequence`, `slice`), aligns (`floorTo`, `ceilTo`), and the `modify {}` builder DSL. |
| **`:time:core-context`** | Context-aware version of `:time:core` arithmetic. | Context-parameter functions resolved automatically via `context(timeZone: TimeZone)`. |
| **`:time:format`** | Formatting engine and configuration DSLs. | DSL formats for date/time, interval formatting, relative formatting, duration formatting, and period formatting. |
| **`:time:format-context`** | Context-aware formatting wrappers. | Context-parameter formatting functions resolved automatically via `context(locale: AppLocale)` or `context(locale: AppLocale, timeZone: TimeZone)`. Includes the `withRegionalContext` scope helper. |

---

## Installation & Configuration

Add the desired modules to your Kotlin Multiplatform project.

### 1. Dependency Setup (`build.gradle.kts`)

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // Core formatting and locale module
            implementation("eu.tintera.locale:locale:x.y.z")
            implementation("eu.tintera.time:core:x.y.z")
            implementation("eu.tintera.time:format:x.y.z")
            
            // Optional: Context-aware modules (requires Kotlin 2.2+)
            implementation("eu.tintera.time:core-context:x.y.z")
            implementation("eu.tintera.time:format-context:x.y.z")
        }
    }
}
```

### 2. Enabling Context Parameters

> [!IMPORTANT]
> The context-aware modules (`:time:core-context` and `:time:format-context`) utilize **Kotlin 2.2+ Context Parameters** (which replace context receivers). To use these modules, you must enable the experimental compiler flag in your build configuration:

```kotlin
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }
}
```

---

## Detailed Usage Examples

### 1. Context-Aware Calculations & Formatting (Context Parameters)

With the context-aware modules, you can define a scope with implicit locale and timezone contexts, eliminating the need to pass them explicitly to every function call.

#### Option A: Using the `withRegionalContext` Scope Helper
```kotlin
import eu.tintera.locale.localeForLanguageTag
import eu.tintera.time.core.context.modify
import eu.tintera.time.core.context.toLocalDateTime
import eu.tintera.time.format.context.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone

val czechLocale = localeForLanguageTag("cs-CZ")
val pragueTimeZone = TimeZone.of("Europe/Prague")

// 1. Establish the context scope
withRegionalContext(timeZone = pragueTimeZone, locale = czechLocale) {
    val instant = Instant.parse("2026-05-27T10:00:00Z")
    
    // toLocalDateTime resolves the TimeZone automatically from context
    val localDateTime = instant.toLocalDateTime() // 2026-05-27T12:00:00
    
    // format resolves the AppLocale automatically from context
    val formattedDate = localDateTime.format {
        full()
    } // "středa 27. května 2026"
    
    // modify resolves the TimeZone automatically from context
    val modifiedDateTime = localDateTime.modify {
        plusDays(1)
        plusTime(LocalTime(12, 0))
    }
}
```

#### Option B: Declaring Context Parameters on Functions
```kotlin
import eu.tintera.locale.AppLocale
import eu.tintera.time.core.context.toLocalDateTime
import eu.tintera.time.format.context.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

// Require AppLocale and TimeZone context parameters
context(locale: AppLocale, timeZone: TimeZone)
fun formatAppEvent(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime() // implicitly uses timeZone
    return localDateTime.format { // implicitly uses locale
        date { full() }
        time { short() }
    }
}
```

---

### 2. Standard Formatting (Explicit Parameters)

If you prefer not to use context parameters, you can pass the locale and timezone parameters explicitly.

#### Date Formatting
Format dates using predefined styles (`short()`, `medium()`, `long()`, `full()`) or custom configurations:

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.locale.localeForLanguageTag
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDate

val date = LocalDate(2025, 4, 15)
val defaultLocale = currentLocale

// Predefined full date format
val fullFormatted = date.format(locale = defaultLocale) {
    full()
} // e.g., "Tuesday, April 15, 2025" (depending on system locale)

// Custom format configuration
val customFormatted = date.format(locale = defaultLocale) {
    day = DayFormat.Numeric
    month = MonthFormat.Name.Short
    year = YearFormat.FourDigits
} // e.g., "15 Apr 2025"

// Custom locale
val czechLocale = localeForLanguageTag("cs-CZ")
val localizedDate = date.format(locale = czechLocale) {
    long()
} // e.g., "15. dubna 2025"
```

#### Time Formatting
Format times easily, automatically handling 12/24 hour preferences based on the locale:

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalTime

val time = LocalTime(14, 30)
val defaultLocale = currentLocale

val formattedTime = time.format(locale = defaultLocale) {
    short()
} // e.g., "2:30 PM" or "14:30" depending on system locale
```

#### Date and Time Formatting
Combine date and time formatting using the `DateTimeFormat` builder DSL:

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDateTime

val dateTime = LocalDateTime(2025, 4, 15, 14, 30)
val defaultLocale = currentLocale

val formattedDateTime = dateTime.format(locale = defaultLocale) {
    date { short() }
    time { full() }
} // e.g., "4/15/25, 2:30:00 PM"
```

---

### 3. Interval Formatting

Format intervals intelligently. The library automatically omits redundant information (like repeating the year or month if both dates fall in the same month/year).

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.time.format.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

val start = LocalDateTime(2026, 5, 19, 14, 0)
val end = LocalDateTime(2026, 5, 20, 16, 30)
val defaultLocale = currentLocale
val defaultTimeZone = TimeZone.currentSystemDefault()

// Standard interval formatting
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
} // e.g., "Tuesday 19 – Wednesday 20 May 2026"

// Formatting an OpenEndRange directly
val range = start..<end
val rangeFormatted = range.format(
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
```

You can also provide custom combiners for specific scenarios (same day, same month, same year, or different date):

```kotlin
val customInterval = start.formatInterval(
    to = end,
    locale = defaultLocale,
    timeZone = defaultTimeZone,
    onSameMonth = { interval, startDate, endDate ->
        val startDay = startDate.dayOfMonth
        val endPart = endDate.format(interval.locale) { medium() }
        "$startDay. – $endPart"
    }
) {
    date { medium() }
} // e.g., "19. – 20. května 2026" (for cs-CZ)
```

---

### 4. Relative Time Formatting

Format a time relative to another time (e.g., now) with custom thresholds and style settings.

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.time.format.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.minutes

val now = Clock.System.now()
val past = now.minus(5.minutes)
val defaultLocale = currentLocale
val defaultTimeZone = TimeZone.currentSystemDefault()

val relative = past.formatRelative(
    now = now,
    timeZone = defaultTimeZone,
    locale = defaultLocale
) {
    style = FormatStyle.Full
    minutes() // Enable formatting in minutes
} // e.g., "5 minutes ago" (or "před 5 minutami" in Czech)
```

Within a context parameter scope:
```kotlin
withRegionalContext(locale = defaultLocale, timeZone = defaultTimeZone) {
    val relative = past.formatRelative(now = now) {
        style = FormatStyle.Full
        minutes()
    }
}
```

---

### 5. Duration and Period Formatting

The library supports formatting Kotlin `Duration`, `DatePeriod`, and `DateTimePeriod` textually (e.g., "2 hours, 30 minutes") or digitally (e.g., "02:30:00").

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.time.format.*
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

val duration = 10.days + 5.hours + 10.minutes
val locale = currentLocale

// 1. Textual duration formatting
val textDuration = duration.format(locale = locale) {
    style = FormatStyle.Full
    days = UnitVisibility.Auto
    hours = UnitVisibility.Auto
    minutes = UnitVisibility.Auto
} // e.g. "10 days, 5 hours, 10 minutes"

// 2. Digital clock-style duration formatting
val digitalDuration = duration.formatDigital(locale = locale) {
    day = FormatStyle.Full
    hour = HourFormat.Digital24h.Padded
    minute = MinuteFormat.Padded
    second = SecondFormat.Padded
} // e.g. "10 days 05:10:00"

val datePeriod = DatePeriod(years = 0, months = 3, days = 8)
val dateTimePeriod = DateTimePeriod(years = 0, months = 3, days = 8, hours = 10, minutes = 5)

// 3. DatePeriod calendar formatting
val formattedDatePeriod = datePeriod.formatCalendar(locale = locale) {
    years = UnitVisibility.Auto
    months = UnitVisibility.Required
    days = UnitVisibility.Required
} // e.g. "3 months, 8 days"

// 4. DateTimePeriod formatting (both calendar and clock units)
val formattedDateTimePeriod = dateTimePeriod.format(locale = locale) {
    maxUnitsCount = null // output all configured units
    calendar {
        years = UnitVisibility.Auto
        months = UnitVisibility.Required
        days = UnitVisibility.Required
    }
    clock {
        hours = UnitVisibility.Auto
        minutes = UnitVisibility.Required
    }
} // e.g. "3 months, 8 days, 10 hours, 5 minutes"
```

---

### 6. Core Arithmetic & Sequences (`:time:core`)

`:time:core` includes platform-independent extensions to `LocalDateTime` and ranges. When using `:time:core-context`, the `TimeZone` parameter is automatically resolved from the context.

```kotlin
import eu.tintera.time.core.*
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

val startLdt = LocalDateTime(2026, 5, 27, 9, 0)
val timeZone = TimeZone.of("Europe/Prague")
val period = DateTimePeriod(hours = 2)

// Generate a sequence of consecutive time intervals
val intervals: Sequence<OpenEndRange<LocalDateTime>> = 
    startLdt.generateSequence(period = period, timeZone = timeZone)

// Align LocalDateTime to the start/end of the nearest interval boundary
val floored = startLdt.floorTo(period = period, timeZone = timeZone)
val ceiled = startLdt.ceilTo(period = period, timeZone = timeZone)

// Modify LocalDateTime using a fluid DSL
val modified = startLdt.modify(timeZone = timeZone) {
    plusYears(1)
    plusDays(5)
    withTime(hour = 12, minute = 0)
}
```

With `:time:core-context` inside a context scope:
```kotlin
import eu.tintera.time.core.context.*

context(timeZone: TimeZone)
fun adjustDateTime(ldt: LocalDateTime): LocalDateTime {
    val intervals = ldt.generateSequence(period) // implicitly uses timeZone
    return ldt.modify { // implicitly uses timeZone
        plusDays(1)
    }
}
```

---

### 7. Helper Utilities

Get localized names for days, months, decimal separators, and first day of the week:

```kotlin
import eu.tintera.locale.currentLocale
import eu.tintera.time.format.*
import kotlinx.datetime.Month
import kotlinx.datetime.DayOfWeek

val locale = currentLocale

val monthName = Month.APRIL.formatName(locale = locale, format = MonthFormat.Name.Full) // "April"
val dayName = DayOfWeek.MONDAY.formatName(locale = locale, format = WeekDayFormat.ShortName) // "Mon"
val separator = getDecimalSeparator(locale = locale) // "." or ","

// Platform-aware first day of the week (no locale parameter required)
val firstDay = getFirstDayOfWeek() // e.g., DayOfWeek.SUNDAY or DayOfWeek.MONDAY
```

Under `withRegionalContext` (using `:time:format-context`):
```kotlin
import eu.tintera.time.format.context.*

withRegionalContext(locale = locale) {
    val monthName = Month.APRIL.formatName() // implicit locale
    val dayName = DayOfWeek.MONDAY.formatName(WeekDayFormat.ShortName) // implicit locale
    val separator = getDecimalSeparator() // implicit locale
}
```

---

## Detailed DSL Reference

### Date Format Config (`DateFormatScope`)
- `weekDay`: `WeekDayFormat` (`FullName` e.g., "Monday" or `ShortName` e.g., "Mon")
- `day`: `DayFormat` (`Numeric` e.g., "5" or `Padded` e.g., "05")
- `month`: `MonthFormat`
  - Name: `MonthFormat.Name.Full` (e.g., "January") or `MonthFormat.Name.Short` (e.g., "Jan")
  - Digital: `MonthFormat.Digital.Numeric` (e.g., "1") or `MonthFormat.Digital.Padded` (e.g., "01")
- `year`: `YearFormat` (`FourDigits` e.g., "2026" or `TwoDigits` e.g., "26")
- Predefined styles: `short()`, `medium()`, `long()`, `full()`.

### Time Format Config (`TimeFormatScope`)
- `hour`: `HourFormat`
  - 24h: `HourFormat.Digital24h.Numeric` (e.g., "9", "13") or `HourFormat.Digital24h.Padded` (e.g., "09", "13")
  - 12h: `HourFormat.Digital12.Numeric` (e.g., "9", "1") or `HourFormat.Digital12.Padded` (e.g., "09", "01")
  - Auto (prefers local 12/24h setting): `HourFormat.Auto.Numeric` or `HourFormat.Auto.Padded`
- `minute`: `MinuteFormat` (`Numeric` or `Padded`)
- `second`: `SecondFormat` (`Numeric` or `Padded`)
- `fractionalSecond`: `FractionalSecondFormat` (`OneDigits`, `TwoDigits`, `ThreeDigits`)
- `periodStyle`: `DayPeriodStyle` (`Required` forces display of AM/PM, `None` forces omission)
- Predefined styles: `short()`, `full()`.

### Relative DateTime Config (`RelativeDateTimeFormatScope`)
- `style`: `FormatStyle` (`Full`, `Short`, `Narrow`)
- Unit threshold configuration functions: `years(min: Int?)`, `months(min: Int?)`, `days(min: Int?)`, `hours(min: Int?)`, `minutes(min: Int?)`, `seconds(min: Int?)`.

### Duration Config (`DurationFormatScope`)
- `style`: `FormatStyle` (`Full`, `Short`, `Narrow`)
- `days`, `hours`, `minutes`, `seconds`, `fractionalSeconds`: Set each to `UnitVisibility.Auto`, `UnitVisibility.Required`, or `null` to omit.
- Predefined styles: `short()`, `full()`.

### Duration Digital Config (`DurationDigitalFormatScope`)
- `day`: `FormatStyle?` (textual width style for day component)
- `hour`: `HourFormat.Digital24h?`
- `minute`: `MinuteFormat?`
- `second`: `SecondFormat?`
- `fractionalSecond`: `FractionalSecondFormat?`
- `separator`: `String` (defaults to `" "`)
- Predefined style: `stopwatch()`.

### DatePeriod Config (`DatePeriodFormatScope`)
- `style`: `FormatStyle`
- `maxUnitsCount`: `Int?`
- `years`, `months`, `days`: `UnitVisibility` (`Auto` or `Required`)

### DateTimePeriod Config (`DateTimePeriodFormatScope`)
- `style`: `FormatStyle`
- `maxUnitsCount`: `Int?`
- `calendar {}` block configuration (for years, months, days)
- `clock {}` block configuration (for hours, minutes, seconds)