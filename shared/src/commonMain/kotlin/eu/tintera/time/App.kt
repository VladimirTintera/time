package eu.tintera.time

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.tintera.locale.context.currentLocaleContext
import eu.tintera.locale.languageCode
import eu.tintera.time.core.context.modify
import eu.tintera.time.core.context.systemDefaultTimeZoneContext
import eu.tintera.time.core.context.toLocalDateTime
import eu.tintera.time.format.*
import eu.tintera.time.format.context.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@Composable
@Preview
fun App() {

    with(systemDefaultTimeZoneContext()) {
        with(currentLocaleContext()) {

            SelectionContainer {
                MaterialTheme {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .safeContentPadding()
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        val scope = rememberCoroutineScope()
                        val time by remember {
                            flow {
                                while (true) {
                                    emit(Clock.System.now())
                                    delay(0.5.seconds)
                                }
                            }.stateIn(scope, SharingStarted.WhileSubscribed(5000), Clock.System.now())
                        }.collectAsStateWithLifecycle()


                        Text(
                            time.format {
                                date {
                                    full()
                                }
                                time {
                                    full()
                                    periodStyle = DayPeriodStyle.Required
                                    fractionalSecond = FractionalSecondFormat.OneDigits
                                }
                            }
                        )

                        Text(
                            time.format {
                                date {
                                    full()
                                }
                                time {
                                    full()
                                    fractionalSecond = FractionalSecondFormat.OneDigits
                                }
                            }
                        )


                        Text(
                            time.format {
                                time {
                                    short()
                                }
                            }
                        )

                        Text(
                            time.toLocalDateTime().date.format {
                                full()
                            }
                        )

                        Text(
                            time.toLocalDateTime().time.format {
                                full()
                            }
                        )

                        Text(time.toLocalDateTime().date.month.formatName())
                        Text(getFirstDayOfWeek().formatName())

                        Text(
                            (time - 5.hours).formatRelative(
                                now = Clock.System.now()
                            ) {

                            }
                        )

                        Text(
                            "xxxx" + (time - 1000.days).formatRelative(
                                now = Clock.System.now(),
                            ) {
                                style = FormatStyle.Full
                                years(5)
                                months(100)
                            }
                        )

                        Text(
                            (time + 5.minutes).formatRelative(
                                now = Clock.System.now()
                            ) {
                                style = FormatStyle.Short
                            }
                        )

                        Text(
                            time.formatInterval(
                                to = time - 5.hours
                            ) {
                                date {
                                    short()
                                }
                                time {
                                    short()
                                }
                            }
                        )



                        Text(
                            (10.days + 5.hours + 10.minutes).format { full() }
                        )

                        Text(
                            (10.days + 5.hours + 10.minutes).formatDigital {
                                day = FormatStyle.Full
                                hour = HourFormat.Digital24h.Padded
                                minute = MinuteFormat.Padded
                                second = SecondFormat.Padded
                                fractionalSecond = FractionalSecondFormat.ThreeDigits
                            }
                        )

                        Text(
                            time.toLocalDateTime().let { dt ->
                                dt.formatInterval(
                                    to = dt.modify {
                                        minusDays(10)
                                    },
                                    onSameMonth = { interval, start, end ->
                                        if (interval.locale.languageCode == "cs") {

                                            val endPart = end.format(
                                                locale = interval.locale
                                            ) { medium() }

                                            val startPart = start.format(interval.locale) {
                                                day = DayFormat.Numeric
                                            }.trim()

                                            "$startPart – $endPart"

                                        } else interval.format()
                                    }
                                ) {
                                    date { medium() }
                                }
                            }
                        )

                        Text(
                            time.toLocalDateTime().date.let { date ->
                                date.atTime(time.toLocalDateTime().time)
                                    .formatInterval(
                                        to = date.minus(10, DateTimeUnit.DAY)
                                            .atTime(time.toLocalDateTime().time)
                                    ) {
                                        date {
                                            medium()
                                        }
                                        time { short() }
                                    }
                            }
                        )

                        Text(
                            text = DatePeriod(0, 3, 8).formatCalendar {
                                years = UnitVisibility.Auto
                                months = UnitVisibility.Required
                                days = UnitVisibility.Required
                            }
                        )

                        Text(
                            text = DateTimePeriod(0, 3, 8, hours = 10, minutes = 5).format {
                                maxUnitsCount = null
                                calendar {
                                    years = UnitVisibility.Auto
                                    months = UnitVisibility.Required
                                    days = UnitVisibility.Required
                                }
                                clock {
                                    hours = UnitVisibility.Auto
                                    minutes = UnitVisibility.Required
                                    seconds = UnitVisibility.Auto
                                }
                            }
                        )

                        Text(
                            (time - 3.seconds).formatRelative(
                                now = Clock.System.now()
                            ) {
                                style = FormatStyle.Full
                                years(5)
                                months(100)
                                seconds(10)
                            }
                        )
                    }
                }
            }
        }
    }
}