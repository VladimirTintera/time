package eu.tintera.time

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import eu.tintera.locale.currentLocale
import eu.tintera.locale.displayName
import eu.tintera.time.format.context.withRegionalContext

enum class Page {
    FORMATING,
    PLAYGROUND
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var page by remember { mutableStateOf(Page.FORMATING) }
        var locale by remember { mutableStateOf(currentLocale) }

        var localeSelection by remember { mutableStateOf(false) }
        if (localeSelection) LocaleSelectionBottomSheet(
            currentLocale = locale,
            onDismissRequest = { localeSelection = false },
            onLocaleChange = {
                locale = it
                localeSelection = false
            }
        )
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        TextButton(onClick = { localeSelection = true }) {
                            Text(locale.displayName)
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = page == Page.FORMATING,
                        onClick = { page = Page.FORMATING },
                        icon = { Text("Showcase") },
                    )

                    NavigationBarItem(
                        selected = page == Page.PLAYGROUND,
                        onClick = { page = Page.PLAYGROUND },
                        icon = { Text("Playground") },
                    )
                }
            }
        ) { padding ->
            withRegionalContext(locale = locale) {
                when (page) {
                    Page.FORMATING -> FormatingPage(padding)
                    Page.PLAYGROUND -> PlaygroundPage(padding)
                }
            }
        }
    }
}