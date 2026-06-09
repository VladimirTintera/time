package eu.tintera.time

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.tintera.locale.currentLocale
import eu.tintera.locale.displayName
import eu.tintera.time.format.context.withRegionalContext
import kotlinx.datetime.TimeZone

enum class AppTab(val title: String) {
    FORMATTING("Formatting"),
    INTERVALS("Intervals"),
    CORE_CALCULATIONS("Rounding & Sequences"),
    DURATION_PERIOD("Duration & Period"),
    RELATIVE_TIME("Relative Time"),
    SYSTEM_COMPARISON("System & Comparison")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        var activeTab by remember { mutableStateOf(AppTab.FORMATTING) }
        var locale by remember { mutableStateOf(currentLocale) }
        var timeZone by remember { mutableStateOf(TimeZone.currentSystemDefault()) }

        var localeSelection by remember { mutableStateOf(false) }
        var timeZoneSelection by remember { mutableStateOf(false) }

        if (localeSelection) {
            LocaleSelectionBottomSheet(
                currentLocale = locale,
                onDismissRequest = { localeSelection = false },
                onLocaleChange = {
                    locale = it
                    localeSelection = false
                }
            )
        }

        if (timeZoneSelection) {
            TimeZoneSelectionDialog(
                currentTimeZone = timeZone,
                onDismissRequest = { timeZoneSelection = false },
                onTimeZoneChange = {
                    timeZone = it
                    timeZoneSelection = false
                }
            )
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "KMP Time Library Playground",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Settings bar (Locale and TimeZone selection)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { localeSelection = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Locale: ${locale.displayName}",
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }

                    OutlinedButton(
                        onClick = { timeZoneSelection = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Zone: ${timeZone.id}",
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                // Tab layout for navigation between playgrounds
                SecondaryScrollableTabRow(
                    selectedTabIndex = activeTab.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = TabRowDefaults.primaryContainerColor,
                    contentColor = TabRowDefaults.primaryContentColor,
                    edgePadding = 16.dp
                ) {
                    AppTab.entries.forEach { tab ->
                        Tab(
                            selected = activeTab == tab,
                            onClick = { activeTab = tab },
                            text = { Text(tab.title) }
                        )
                    }
                }

                // Render selected tab within regional context
                withRegionalContext(timeZone = timeZone, locale = locale) {
                    Box(modifier = Modifier.fillMaxSize().weight(1f)) {
                        when (activeTab) {
                            AppTab.FORMATTING -> FormattingPlayground()
                            AppTab.INTERVALS -> IntervalsPlayground()
                            AppTab.CORE_CALCULATIONS -> CoreCalculationsPlayground()
                            AppTab.DURATION_PERIOD -> DurationPeriodPlayground()
                            AppTab.RELATIVE_TIME -> RelativeTimePlayground()
                            AppTab.SYSTEM_COMPARISON -> SystemComparisonPlayground()
                        }
                    }
                }
            }
        }
    }
}