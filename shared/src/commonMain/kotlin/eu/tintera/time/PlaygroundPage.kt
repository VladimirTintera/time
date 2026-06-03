package eu.tintera.time

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.tintera.locale.AppLocale
import eu.tintera.time.core.context.*
import eu.tintera.time.core.minus
import eu.tintera.time.core.periodDays
import eu.tintera.time.core.timeDuration
import eu.tintera.time.format.DateTimeFormat
import eu.tintera.time.format.FractionalSecondFormat
import eu.tintera.time.format.UnitVisibility
import eu.tintera.time.format.context.format
import eu.tintera.time.format.context.formatRelative
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant


context(locale: AppLocale, timeZone: TimeZone)
@Composable
fun PlaygroundPage(paddingValues: PaddingValues) {
    var time by remember {
        mutableStateOf(Clock.System.now().toLocalDateTime())
    }
    var period by remember { mutableStateOf(1.periodDays) }

    LazyVerticalGrid(
        modifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        columns = GridCells.Adaptive(minSize = 300.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BaseCard {
                Text(1.5.hours.format {
                    hours = UnitVisibility.Required
                    minutes = UnitVisibility.Required
                })
                var dateDialog by remember {
                    mutableStateOf(false)
                }
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = time.date.atStartOfDayIn(TimeZone.UTC)
                        .toEpochMilliseconds()
                )
                if (dateDialog) DatePickerDialog(
                    onDismissRequest = { dateDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                datePickerState.selectedDateMillis?.also {
                                    time = Instant.fromEpochMilliseconds(it)
                                        .toLocalDateTime(TimeZone.UTC).date.atTime(time.time)
                                    dateDialog = false
                                }
                            }
                        ) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { dateDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
                ConfigRow(
                    title = { Text("Date") }
                ) {
                    TextButton(onClick = { dateDialog = true }) {
                        Text(time.date.format { full() })
                    }
                }

                var timeDialog by remember {
                    mutableStateOf(false)
                }

                val timePickerState = rememberTimePickerState(
                    initialHour = time.time.hour,
                    initialMinute = time.time.minute
                )
                if (timeDialog) TimePickerDialog(
                    onDismissRequest = { timeDialog = false },
                    confirmButton = {
                        Button(
                            onClick = {
                                time = time.date.atTime(timePickerState.hour, timePickerState.minute)
                                timeDialog = false
                            }
                        ) { Text("Ok") }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { timeDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("Pick time") }
                ) {
                    TimePicker(
                        state = timePickerState
                    )
                }

                ConfigRow(
                    title = { Text("Time") }
                ) {
                    TextButton(onClick = { timeDialog = true }) {
                        Text(time.time.format {
                            full()
                            fractionalSecond = FractionalSecondFormat.ThreeDigits
                        })
                    }
                }

                ConfigRow(
                    title = { Text("Period") }
                ) {
                    var periodEditDialog by remember { mutableStateOf(false) }

                    if (periodEditDialog) EditDateTimePeriodDialog(
                        period,
                        onDismissRequest = { periodEditDialog = false },
                        onPeriodSaved = {
                            period = it
                            periodEditDialog = false
                        }
                    )

                    TextButton(
                        onClick = { periodEditDialog = true }
                    ) {
                        Text(period.format())
                    }
                }
            }
        }

        item {
            BaseCard {
                val format = remember {
                    DateTimeFormat {
                        date { medium() }
                        time {
                            full()
                            fractionalSecond = FractionalSecondFormat.ThreeDigits
                        }
                    }
                }
                Column {
                    ConfigRow(
                        title = { Text("Rounded") },
                    ) {
                        Text(time.roundTo(period).format(format))
                    }

                    ConfigRow(
                        title = { Text("Floored") },
                    ) {
                        Text(time.floorTo(period).format(format))
                    }

                    ConfigRow(
                        title = { Text("Ceiled") },
                    ) {
                        Text(time.ceilTo(period).format(format))
                    }
                }
            }
        }

        item {


            Card {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    val firstDate = time.plus(period)

                    Text(firstDate.format())

                    val diff = time.minus(firstDate)

                    Text(
                        text = "Total difference from start: ${diff.format()}"
                    )

                    time.generateSequence(period).take(10).forEach { interval ->
                        Column {
                            Text(
                                interval.format {
                                    date { short() }
                                    if (period.timeDuration != Duration.ZERO)
                                        time { short() }
                                }
                            )

                            Text(
                                interval.start.formatRelative(time),
                                color = LocalContentColor.current.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfigRow(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(.35f)) { title() }
        Column(modifier = Modifier.weight(.65f)) { content() }
    }
}

@Composable
fun BaseCard(
    content: @Composable () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        ) {
            content()
        }
    }
}