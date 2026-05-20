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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


@Composable
@Preview
fun App() {
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
                            includeMilliseconds = true
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
                    time.toLocalDateTime(TimeZone.currentSystemDefault()).date.format {
                        full()
                    }
                )

                Text(
                    time.toLocalDateTime(TimeZone.currentSystemDefault()).time.format {
                        full()
                    }
                )

                Text(time.toLocalDateTime(TimeZone.currentSystemDefault()).date.month.formatName())
                Text(getFirstDayOfWeek().formatName())

                Text(
                    (time - 5.hours).formatRelative()
                )

                Text(
                    (time - 1000.days).formatRelative(style = RelativeUnitStyle.Short)
                )

                Text(
                    (time + 5.minutes).formatRelative()
                )

                Text(
                    (time + 5.minutes).formatRelative(style = RelativeUnitStyle.Short)
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
                    time.toLocalDateTime(TimeZone.currentSystemDefault()).date.let {
                        it.formatInterval(
                            to = it.plus(10, DateTimeUnit.DAY)
                        ) {
                            short()
                        }
                    }
                )
            }
        }
    }
}