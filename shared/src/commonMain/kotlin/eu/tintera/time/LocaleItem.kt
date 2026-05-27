package eu.tintera.time

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.tintera.locale.AppLocale
import eu.tintera.locale.displayName
import eu.tintera.locale.languageCode
import eu.tintera.locale.languageTag
import eu.tintera.time.core.context.modify
import eu.tintera.time.core.context.toLocalDateTime
import eu.tintera.time.format.*
import eu.tintera.time.format.context.*
import kotlinx.datetime.*
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant

@Composable
fun LocaleItem(
    modifier: Modifier,
    locale: AppLocale,
    time: Instant
) = withRegionalContext(locale = locale) {
    Card(modifier = modifier) {
        Column(
             modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Text(text = locale.displayName, style = MaterialTheme.typography.headlineSmall)
                Text(text = locale.languageTag, style = MaterialTheme.typography.headlineSmall)
            }
            Spacer(Modifier.height(16.dp))
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
                (time - 1000.days).formatRelative(
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