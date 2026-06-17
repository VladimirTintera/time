package eu.tintera.time

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.tintera.locale.AppLocale
import eu.tintera.locale.currentLocale
import eu.tintera.locale.displayName
import eu.tintera.locale.languageTag
import eu.tintera.locale.localeForLanguageTag
import eu.tintera.time.core.SequenceDirection
import eu.tintera.time.core.context.*
import eu.tintera.time.core.periodDays
import eu.tintera.time.format.*
import eu.tintera.time.format.context.*
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

enum class DurationUnitType { Minutes, Hours, Days, Seconds }

// ==========================================
// HELPERS
// ==========================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerButton(
    label: String,
    value: LocalDateTime,
    onValueChange: (LocalDateTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value.toInstant(TimeZone.UTC).toEpochMilliseconds()
    )

    val timePickerState = rememberTimePickerState(
        initialHour = value.time.hour,
        initialMinute = value.time.minute
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
                        onValueChange(LocalDateTime(date, value.time))
                    }
                    showDatePicker = false
                }) { Text("Ok") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange(LocalDateTime(value.date, LocalTime(timePickerState.hour, timePickerState.minute)))
                    showTimePicker = false
                }) { Text("Ok") }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) { Text("Cancel") }
            },
            title = { Text("Select time") }
        ) {
            TimePicker(state = timePickerState)
        }
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically).padding(end = 4.dp)
            )
        }
        OutlinedButton(onClick = { showDatePicker = true }) {
            Text(value.date.toString())
        }
        OutlinedButton(onClick = { showTimePicker = true }) {
            val minuteStr = value.time.minute.toString().padStart(2, '0')
            Text("${value.time.hour.toString().padStart(2, '0')}:$minuteStr")
        }
    }
}

@Composable
fun DateTimePickerCard(
    title: String,
    dateTime: LocalDateTime,
    onDateTimeChange: (LocalDateTime) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Date and time:", style = MaterialTheme.typography.bodyMedium)
                DateTimePickerButton(
                    label = "",
                    value = dateTime,
                    onValueChange = onDateTimeChange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun PeriodStepper(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        FilledIconButton(
            onClick = { onValueChange(value - 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Text("-", style = MaterialTheme.typography.titleMedium)
        }
        OutlinedTextField(
            value = value.toString(),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.also { onValueChange(it) }
            },
            modifier = Modifier.width(60.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium
        )
        FilledIconButton(
            onClick = { onValueChange(value + 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Text("+", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun CopyableCodeCard(
    code: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "Kotlin DSL code:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            SelectionContainer {
                Text(
                    text = code,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (label.isNotEmpty()) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.width(160.dp).menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                textStyle = MaterialTheme.typography.bodyMedium
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

// ==========================================
// PLAYGROUND 1: FORMATTING PLAYGROUND
// ==========================================

context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun FormattingPlayground() {
    var dateTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(timeZone)) }

    // Date configuration
    var dateStyle by remember { mutableStateOf("Medium") }
    var customWeekday by remember { mutableStateOf("None") }
    var customDay by remember { mutableStateOf("Numeric") }
    var customMonth by remember { mutableStateOf("Name.Full") }
    var customYear by remember { mutableStateOf("FourDigits") }

    // Time configuration
    var timeStyle by remember { mutableStateOf("Short") }
    var customHour by remember { mutableStateOf("Auto.Numeric") }
    var customMinute by remember { mutableStateOf("Padded") }
    var customSecond by remember { mutableStateOf("Padded") }
    var customFractionalSecond by remember { mutableStateOf("None") }
    var customPeriodStyle by remember { mutableStateOf("None") }

    val formattedResult = remember(
        dateTime, dateStyle, customWeekday, customDay, customMonth, customYear,
        timeStyle, customHour, customMinute, customSecond, customFractionalSecond, customPeriodStyle
    ) {
        try {
            dateTime.format {
                date {
                    when (dateStyle) {
                        "Full" -> full()
                        "Long" -> long()
                        "Medium" -> medium()
                        "Short" -> short()
                        "Custom" -> {
                            weekDay = when (customWeekday) {
                                "FullName" -> WeekDayFormat.FullName
                                "ShortName" -> WeekDayFormat.ShortName
                                else -> null
                            }
                            day = when (customDay) {
                                "Numeric" -> DayFormat.Numeric
                                "Padded" -> DayFormat.Padded
                                else -> null
                            }
                            month = when (customMonth) {
                                "Name.Full" -> MonthFormat.Name.Full
                                "Name.Short" -> MonthFormat.Name.Short
                                "Digital.Numeric" -> MonthFormat.Digital.Numeric
                                "Digital.Padded" -> MonthFormat.Digital.Padded
                                else -> null
                            }
                            year = when (customYear) {
                                "FourDigits" -> YearFormat.FourDigits
                                "TwoDigits" -> YearFormat.TwoDigits
                                else -> null
                            }
                        }
                    }
                }
                time {
                    when (timeStyle) {
                        "Full" -> full()
                        "Short" -> short()
                        "Custom" -> {
                            hour = when (customHour) {
                                "Auto.Numeric" -> HourFormat.Auto.Numeric
                                "Auto.Padded" -> HourFormat.Auto.Padded
                                "Digital24h.Numeric" -> HourFormat.Digital24h.Numeric
                                "Digital24h.Padded" -> HourFormat.Digital24h.Padded
                                "Digital12.Numeric" -> HourFormat.Digital12.Numeric
                                "Digital12.Padded" -> HourFormat.Digital12.Padded
                                else -> null
                            }
                            minute = when (customMinute) {
                                "Numeric" -> MinuteFormat.Numeric
                                "Padded" -> MinuteFormat.Padded
                                else -> null
                            }
                            second = when (customSecond) {
                                "Numeric" -> SecondFormat.Numeric
                                "Padded" -> SecondFormat.Padded
                                else -> null
                            }
                            fractionalSecond = when (customFractionalSecond) {
                                "OneDigits" -> FractionalSecondFormat.OneDigits
                                "TwoDigits" -> FractionalSecondFormat.TwoDigits
                                "ThreeDigits" -> FractionalSecondFormat.ThreeDigits
                                else -> null
                            }
                            periodStyle = when (customPeriodStyle) {
                                "Required" -> DayPeriodStyle.Required
                                "None" -> DayPeriodStyle.None
                                else -> null
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            "Formatting error: ${e.message}"
        }
    }

    val generatedCode = remember(
        dateStyle, customWeekday, customDay, customMonth, customYear,
        timeStyle, customHour, customMinute, customSecond, customFractionalSecond, customPeriodStyle
    ) {
        buildString {
            append("dateTime.format {\n")
            append("    date {\n")
            when (dateStyle) {
                "Full" -> append("        full()\n")
                "Long" -> append("        long()\n")
                "Medium" -> append("        medium()\n")
                "Short" -> append("        short()\n")
                "Custom" -> {
                    if (customWeekday != "None") append("        weekDay = WeekDayFormat.$customWeekday\n")
                    if (customDay != "None") append("        day = DayFormat.$customDay\n")
                    if (customMonth != "None") append("        month = MonthFormat.$customMonth\n")
                    if (customYear != "None") append("        year = YearFormat.$customYear\n")
                }
            }
            append("    }\n")
            append("    time {\n")
            when (timeStyle) {
                "Full" -> append("        full()\n")
                "Short" -> append("        short()\n")
                "Custom" -> {
                    if (customHour != "None") append("        hour = HourFormat.$customHour\n")
                    if (customMinute != "None") append("        minute = MinuteFormat.$customMinute\n")
                    if (customSecond != "None") append("        second = SecondFormat.$customSecond\n")
                    if (customFractionalSecond != "None") append("        fractionalSecond = FractionalSecondFormat.$customFractionalSecond\n")
                    if (customPeriodStyle != "None") append("        periodStyle = DayPeriodStyle.$customPeriodStyle\n")
                }
            }
            append("    }\n")
            append("}")
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DateTimePickerCard("Select input Date and Time", dateTime) { dateTime = it }
        }

        // Live Output Panel
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Formatted output:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    SelectionContainer {
                        Text(
                            text = formattedResult,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Date Controls Card
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Date format settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Full", "Long", "Medium", "Short", "Custom").forEach { style ->
                            FilterChip(
                                selected = dateStyle == style,
                                onClick = { dateStyle = style },
                                label = { Text(style, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }

                    if (dateStyle == "Custom") {
                        HorizontalDivider()
                        Text(
                            "Custom date details:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        DropdownSelector(
                            label = "Weekday",
                            value = customWeekday,
                            options = listOf("None", "FullName", "ShortName")
                        ) { customWeekday = it }
                        DropdownSelector(
                            label = "Day",
                            value = customDay,
                            options = listOf("None", "Numeric", "Padded")
                        ) { customDay = it }
                        DropdownSelector(
                            label = "Month",
                            value = customMonth,
                            options = listOf("None", "Name.Full", "Name.Short", "Digital.Numeric", "Digital.Padded")
                        ) { customMonth = it }
                        DropdownSelector(
                            label = "Year",
                            value = customYear,
                            options = listOf("None", "FourDigits", "TwoDigits")
                        ) { customYear = it }
                    }
                }
            }
        }

        // Time Controls Card
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Time format settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Full", "Short", "Custom").forEach { style ->
                            FilterChip(
                                selected = timeStyle == style,
                                onClick = { timeStyle = style },
                                label = { Text(style, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }

                    if (timeStyle == "Custom") {
                        HorizontalDivider()
                        Text(
                            "Custom time details:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        DropdownSelector(
                            label = "Hour",
                            value = customHour,
                            options = listOf(
                                "None",
                                "Auto.Numeric",
                                "Auto.Padded",
                                "Digital24h.Numeric",
                                "Digital24h.Padded",
                                "Digital12.Numeric",
                                "Digital12.Padded"
                            )
                        ) { customHour = it }
                        DropdownSelector(
                            label = "Minute",
                            value = customMinute,
                            options = listOf("None", "Numeric", "Padded")
                        ) { customMinute = it }
                        DropdownSelector(
                            label = "Second",
                            value = customSecond,
                            options = listOf("None", "Numeric", "Padded")
                        ) { customSecond = it }
                        DropdownSelector(
                            label = "Fractional Second",
                            value = customFractionalSecond,
                            options = listOf("None", "OneDigits", "TwoDigits", "ThreeDigits")
                        ) { customFractionalSecond = it }
                        DropdownSelector(
                            label = "AM/PM (Period)",
                            value = customPeriodStyle,
                            options = listOf("None", "Required")
                        ) { customPeriodStyle = it }
                    }
                }
            }
        }

        item {
            CopyableCodeCard(generatedCode)
        }
    }
}

// ==========================================
// PLAYGROUND 2: INTERVALS PLAYGROUND
// ==========================================

context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun IntervalsPlayground() {
    var startDateTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(timeZone)) }
    var endDateTime by remember { mutableStateOf((Clock.System.now() + 2.days + 3.hours).toLocalDateTime(timeZone)) }

    var dateStyle by remember { mutableStateOf("Medium") }
    var timeStyle by remember { mutableStateOf("Short") }
    var useCustomCombiner by remember { mutableStateOf(false) }

    val formattedInterval = remember(startDateTime, endDateTime, dateStyle, timeStyle, useCustomCombiner) {
        try {
            if (useCustomCombiner) {
                startDateTime.formatInterval(
                    to = endDateTime,
                    onSameMonth = { interval, start, end ->
                        val startPart = start.format(interval.locale, interval.timeZone) { day = DayFormat.Numeric }.trim()
                        val endPart = end.format(interval.locale, interval.timeZone) {
                            medium()
                        }
                        "$startPart. – $endPart (Custom!)"
                    }
                ) {
                    date {
                        when (dateStyle) {
                            "Full" -> full()
                            "Long" -> long()
                            "Medium" -> medium()
                            "Short" -> short()
                        }
                    }
                    time {
                        when (timeStyle) {
                            "Full" -> full()
                            "Short" -> short()
                        }
                    }
                }
            } else {
                startDateTime.formatInterval(to = endDateTime) {
                    date {
                        when (dateStyle) {
                            "Full" -> full()
                            "Long" -> long()
                            "Medium" -> medium()
                            "Short" -> short()
                        }
                    }
                    time {
                        when (timeStyle) {
                            "Full" -> full()
                            "Short" -> short()
                            else -> {}
                        }
                    }
                }
            }
        } catch (e: Exception) {
            "Interval error: ${e.message}"
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Interval range",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    DateTimePickerButton("Start:", startDateTime, { startDateTime = it })
                    DateTimePickerButton("End:  ", endDateTime, { endDateTime = it })

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        "Interval presets:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            endDateTime = startDateTime.plus(2.hours)
                        }) { Text("Same day (+2h)") }

                        Button(onClick = {
                            endDateTime = startDateTime.plus(5.periodDays)
                        }) { Text("Same month (+5d)") }

                        Button(onClick = {
                            endDateTime = startDateTime.plus(DateTimePeriod(years = 1, months = 2))
                        }) { Text("Different years (+1y 2m)") }
                    }
                }
            }
        }

        // Live Output Panel
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Formatted interval:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    SelectionContainer {
                        Text(
                            text = formattedInterval,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Configuration Card
        item {
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Interval settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    DropdownSelector(
                        label = "Date Style",
                        value = dateStyle,
                        options = listOf("Full", "Long", "Medium", "Short")
                    ) { dateStyle = it }
                    DropdownSelector(
                        label = "Time Style",
                        value = timeStyle,
                        options = listOf("Full", "Short", "None")
                    ) { timeStyle = it }

                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Custom behavior for same month (onSameMonth)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Switch(checked = useCustomCombiner, onCheckedChange = { useCustomCombiner = it })
                    }
                }
            }
        }
    }
}

// ==========================================
// PLAYGROUND 3: CORE CALCULATIONS PLAYGROUND
// ==========================================

context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun CoreCalculationsPlayground() {
    var dateTime by remember { mutableStateOf(Clock.System.now().toLocalDateTime(timeZone)) }

    // DateTimePeriod Step State
    var pYears by remember { mutableStateOf(0) }
    var pMonths by remember { mutableStateOf(0) }
    var pDays by remember { mutableStateOf(1) }
    var pHours by remember { mutableStateOf(0) }
    var pMinutes by remember { mutableStateOf(0) }
    var pSeconds by remember { mutableStateOf(0) }

    val period = remember(pYears, pMonths, pDays, pHours, pMinutes, pSeconds) {
        DateTimePeriod(
            years = pYears,
            months = pMonths,
            days = pDays,
            hours = pHours,
            minutes = pMinutes,
            seconds = pSeconds
        )
    }

    var direction by remember { mutableStateOf(SequenceDirection.Forward) }

    val floored = remember(dateTime, period) {
        try {
            dateTime.floorTo(period)
        } catch (e: Exception) {
            dateTime
        }
    }

    val ceiled = remember(dateTime, period) {
        try {
            dateTime.ceilTo(period)
        } catch (e: Exception) {
            dateTime
        }
    }

    val rounded = remember(dateTime, period) {
        try {
            dateTime.roundTo(period)
        } catch (e: Exception) {
            dateTime
        }
    }

    val sequenceList = remember(dateTime, period, direction) {
        try {
            dateTime.generateSequence(direction, period).take(10).toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            DateTimePickerCard("Input time for calculations and rounding", dateTime) { dateTime = it }
        }

        // DateTimePeriod Editor
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Step (DateTimePeriod)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "Interval grid configuration for rounding and generation.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    PeriodStepper("Years", pYears) { pYears = it }
                    PeriodStepper("Months", pMonths) { pMonths = it }
                    PeriodStepper("Days", pDays) { pDays = it }
                    PeriodStepper("Hours", pHours) { pHours = it }
                    PeriodStepper("Minutes", pMinutes) { pMinutes = it }
                    PeriodStepper("Seconds", pSeconds) { pSeconds = it }
                }
            }
        }

        // Calculation Results Card
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Alignment to step grid:", style = MaterialTheme.typography.titleMedium)

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column {
                            Text(
                                "Floored (down)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                floored.format { date { medium() }; time { short() } },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Column {
                            Text(
                                "Rounded (nearest)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                rounded.format { date { medium() }; time { short() } },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Column {
                            Text(
                                "Ceiled (up)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                ceiled.format { date { medium() }; time { short() } },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }

        // Sequences Card
        item {
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Interval sequence (generateSequence)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        FlowRow {
                            FilterChip(
                                selected = direction == SequenceDirection.Forward,
                                onClick = { direction = SequenceDirection.Forward },
                                label = { Text("Forward") }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = direction == SequenceDirection.Backward,
                                onClick = { direction = SequenceDirection.Backward },
                                label = { Text("Backward") }
                            )
                        }
                    }

                    if (sequenceList.isEmpty()) {
                        Text("Invalid or zero period step.", color = MaterialTheme.colorScheme.error)
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sequenceList.forEach { range ->
                                val startStr = range.start.format { date { short() }; time { short() } }
                                val endStr = range.endExclusive.format { date { short() }; time { short() } }
                                val relativeStr = range.start.formatRelative(dateTime) {
                                    style = FormatStyle.Full
                                    full()
                                }

                                ListItem(
                                    headlineContent = { Text("$startStr – $endStr") },
                                    supportingContent = { Text("Start: $relativeStr") }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// PLAYGROUND 4: DURATION & PERIOD PLAYGROUND
// ==========================================

context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun DurationPeriodPlayground() {
    // Duration amount
    var durValue by remember { mutableStateOf(95L) }
    var durUnit by remember { mutableStateOf(DurationUnitType.Minutes) }

    val duration = remember(durValue, durUnit) {
        when (durUnit) {
            DurationUnitType.Days -> durValue.days
            DurationUnitType.Hours -> durValue.hours
            DurationUnitType.Minutes -> durValue.minutes
            DurationUnitType.Seconds -> durValue.seconds
        }
    }

    // Textual duration styles
    val durTextFull = remember(duration) {
        duration.format {
            style = FormatStyle.Full; days = UnitVisibility.Auto; hours = UnitVisibility.Auto; minutes =
            UnitVisibility.Auto; seconds = UnitVisibility.Auto
        }
    }
    val durTextShort = remember(duration) {
        duration.format {
            style = FormatStyle.Short; days = UnitVisibility.Auto; hours = UnitVisibility.Auto; minutes =
            UnitVisibility.Auto
        }
    }
    val durTextNarrow = remember(duration) {
        duration.format {
            style = FormatStyle.Narrow; days = UnitVisibility.Auto; hours = UnitVisibility.Auto; minutes =
            UnitVisibility.Auto
        }
    }

    // Digital duration style
    val durDigitalStopwatch = remember(duration) { duration.formatDigital { stopwatch() } }
    val durDigitalCustom = remember(duration) {
        duration.formatDigital {
            day = FormatStyle.Full
            hour = HourFormat.Digital24h.Padded
            minute = MinuteFormat.Padded
            second = SecondFormat.Padded
        }
    }

    // Period properties
    var pYears by remember { mutableStateOf(0) }
    var pMonths by remember { mutableStateOf(3) }
    var pDays by remember { mutableStateOf(8) }
    var pHours by remember { mutableStateOf(10) }
    var pMinutes by remember { mutableStateOf(5) }

    val datePeriod = remember(pYears, pMonths, pDays) { DatePeriod(pYears, pMonths, pDays) }
    val dateTimePeriod = remember(pYears, pMonths, pDays, pHours, pMinutes) {
        DateTimePeriod(years = pYears, months = pMonths, days = pDays, hours = pHours, minutes = pMinutes)
    }

    val formattedCalendarPeriod = remember(datePeriod) {
        datePeriod.formatCalendar {
            years = UnitVisibility.Auto
            months = UnitVisibility.Required
            days = UnitVisibility.Required
        }
    }

    val formattedDateTimePeriod = remember(dateTimePeriod) {
        dateTimePeriod.format {
            maxUnitsCount = null
            calendar {
                years = UnitVisibility.Auto
                months = UnitVisibility.Required
                days = UnitVisibility.Required
            }
            clock {
                hours = UnitVisibility.Auto
                minutes = UnitVisibility.Required
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Kotlin Duration Config Card
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Kotlin Duration",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = durValue.toString(),
                            onValueChange = { durValue = it.toLongOrNull() ?: 0L },
                            label = { Text("Amount") },
                            modifier = Modifier.weight(1.5f),
                            singleLine = true
                        )

                        Box(modifier = Modifier.weight(1.5f)) {
                            DropdownSelector(
                                label = "",
                                value = durUnit.name,
                                options = DurationUnitType.entries.map { it.name }) {
                                durUnit = DurationUnitType.valueOf(it)
                            }
                        }
                    }

                    HorizontalDivider()

                    Text(
                        "Textual format (Kotlin Duration):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text("Full:   $durTextFull", fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    Text("Short:  $durTextShort", fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                    Text("Narrow: $durTextNarrow", fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)

                    HorizontalDivider()

                    Text(
                        "Digital format (Clock / Stopwatch):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        "Stopwatch: $durDigitalStopwatch",
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Text(
                        "Custom:    $durDigitalCustom",
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }
            }
        }

        // Periods Config Card
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "DatePeriod & DateTimePeriod",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    PeriodStepper("Years", pYears) { pYears = it }
                    PeriodStepper("Months", pMonths) { pMonths = it }
                    PeriodStepper("Days", pDays) { pDays = it }
                    PeriodStepper("Hours", pHours) { pHours = it }
                    PeriodStepper("Minutes", pMinutes) { pMinutes = it }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        "Formatted DatePeriod (Calendar):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        formattedCalendarPeriod,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        "Formatted DateTimePeriod (Combined):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        formattedDateTimePeriod,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

// ==========================================
// PLAYGROUND 5: RELATIVE TIME PLAYGROUND
// ==========================================

@Composable
fun RelativeUnitRow(
    label: String,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    minValue: Int,
    onMinValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = enabled, onCheckedChange = onEnabledChange)
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
        if (enabled) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    "Min threshold:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = minValue.toString(),
                    onValueChange = { newValue ->
                        newValue.toIntOrNull()?.also { onMinValueChange(it) }
                    },
                    modifier = Modifier.width(64.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun RelativeTimePlayground() {
    var baseline by remember { mutableStateOf(Clock.System.now().toLocalDateTime(timeZone)) }
    var targetDateTime by remember { mutableStateOf((Clock.System.now() - 5.minutes).toLocalDateTime(timeZone)) }

    var style by remember { mutableStateOf(FormatStyle.Full) }

    // Threshold configurations (Unit -> (Enabled, MinValue))
    var enableYears by remember { mutableStateOf(true) }
    var minYears by remember { mutableStateOf(1) }

    var enableMonths by remember { mutableStateOf(true) }
    var minMonths by remember { mutableStateOf(1) }

    var enableDays by remember { mutableStateOf(true) }
    var minDays by remember { mutableStateOf(1) }

    var enableHours by remember { mutableStateOf(true) }
    var minHours by remember { mutableStateOf(1) }

    var enableMinutes by remember { mutableStateOf(true) }
    var minMinutes by remember { mutableStateOf(1) }

    var enableSeconds by remember { mutableStateOf(true) }
    var minSeconds by remember { mutableStateOf(1) }

    val relativeString = remember(
        baseline, targetDateTime, style,
        enableYears, minYears, enableMonths, minMonths, enableDays, minDays,
        enableHours, minHours, enableMinutes, minMinutes, enableSeconds, minSeconds
    ) {
        try {
            targetDateTime.formatRelative(baseline) {
                this.style = style
                years(if (enableYears) minYears else null)
                months(if (enableMonths) minMonths else null)
                days(if (enableDays) minDays else null)
                hours(if (enableHours) minHours else null)
                minutes(if (enableMinutes) minMinutes else null)
                seconds(if (enableSeconds) minSeconds else null)
            }
        } catch (e: Exception) {
            "Relative time error: ${e.message}"
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Input values",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    DateTimePickerButton("Baseline (Now):", baseline, { baseline = it })
                    DateTimePickerButton("Target (Goal): ", targetDateTime, { targetDateTime = it })

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        "Target presets:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Button(onClick = { targetDateTime = baseline.plus(-3.seconds) }) { Text("-3 seconds") }
                        Button(onClick = { targetDateTime = baseline.plus(-5.minutes) }) { Text("-5 minutes") }
                        Button(onClick = { targetDateTime = baseline.plus(2.hours) }) { Text("+2 hours") }
                        Button(onClick = { targetDateTime = baseline.plus(-1000.days) }) { Text("-1000 days") }
                    }
                }
            }
        }

        // Live Output Panel
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Relative representation:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    SelectionContainer {
                        Text(
                            text = relativeString,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // Threshold Options Card
        item {
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Threshold configuration",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    DropdownSelector(
                        label = "Relative Style",
                        value = style.name,
                        options = FormatStyle.entries.map { it.name }) {
                        style = FormatStyle.valueOf(it)
                    }

                    HorizontalDivider()

                    Text(
                        "Time unit threshold configurations:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        RelativeUnitRow("Years", enableYears, { enableYears = it }, minYears, { minYears = it })
                        RelativeUnitRow("Months", enableMonths, { enableMonths = it }, minMonths, { minMonths = it })
                        RelativeUnitRow("Days", enableDays, { enableDays = it }, minDays, { minDays = it })
                        RelativeUnitRow("Hours", enableHours, { enableHours = it }, minHours, { minHours = it })
                        RelativeUnitRow(
                            "Minutes",
                            enableMinutes,
                            { enableMinutes = it },
                            minMinutes,
                            { minMinutes = it })
                        RelativeUnitRow(
                            "Seconds",
                            enableSeconds,
                            { enableSeconds = it },
                            minSeconds,
                            { minSeconds = it })
                    }
                }
            }
        }
    }
}

// ==========================================
// PLAYGROUND 6: SYSTEM COMPARISON PLAYGROUND
// ==========================================

context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun SystemComparisonPlayground() {
    var compareTime by remember { mutableStateOf(Clock.System.now()) }

    // Recompute some variables in background
    val firstDay = remember { getFirstDayOfWeek(currentLocale) }
    val decimalSeparator = remember { getDecimalSeparator() }

    val availableMonths = remember { Month.entries }
    val availableWeekdays = remember { DayOfWeek.entries }

    val comparisonLocales = remember {
        listOf(
            localeForLanguageTag("cs-CZ"),
            localeForLanguageTag("en-US"),
            localeForLanguageTag("de-DE"),
            localeForLanguageTag("ja-JP"),
            localeForLanguageTag("es-ES"),
            localeForLanguageTag("fr-FR")
        )
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 340.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // System Config Card
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "System parameters (selected Locale)",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("First day of week: ${firstDay.name}", style = MaterialTheme.typography.bodyLarge)
                    Text("Decimal separator: '$decimalSeparator'", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        // Localized Month/Day Names Card
        item {
            Card(modifier = Modifier.fillMaxHeight()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Localized names",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        "Months:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        availableMonths.forEach { m ->
                            SuggestionChip(onClick = {}, label = { Text(m.formatName(format = MonthFormat.Name.Full)) })
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    Text(
                        "Days of week:",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        availableWeekdays.forEach { d ->
                            SuggestionChip(
                                onClick = {},
                                label = { Text(d.formatName(format = WeekDayFormat.FullName)) })
                        }
                    }
                }
            }
        }

        // Locale Comparison Grid
        item {
            Card {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Locale comparison grid",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Button(onClick = { compareTime = Clock.System.now() }) { Text("Update time") }
                    }

                    Text(
                        "Compare formatting of the selected moment across various language locales.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        comparisonLocales.forEach { appLoc ->
                            withRegionalContext(timeZone = timeZone, locale = appLoc) {
                                val formattedDt = compareTime.toLocalDateTime(timeZone).format {
                                    date { full() }
                                    time { short() }
                                }
                                val formattedRel = (compareTime - 1.5.hours).formatRelative(compareTime) {
                                    style = FormatStyle.Full
                                    full()
                                }

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "${appLoc.displayName} (${appLoc.languageTag})",
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Date & Time: $formattedDt",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "1.5 hours ago: $formattedRel",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable FlowRowScope.() -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = content
    )
}