package eu.tintera.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import eu.tintera.locale.AppLocale
import eu.tintera.locale.availableLocales
import eu.tintera.locale.displayName
import eu.tintera.locale.languageTag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocaleSelectionBottomSheet(
    currentLocale: AppLocale,
    onLocaleChange: (AppLocale) -> Unit,
    onDismissRequest: () -> Unit,
) = ModalBottomSheet(
    onDismissRequest = onDismissRequest
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredLocales = remember(searchQuery) {
        availableLocales()
            .filter {
                it.displayName.contains(searchQuery, ignoreCase = true) ||
                it.languageTag.contains(searchQuery, ignoreCase = true)
            }
            .sortedBy { it.displayName }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search language / locale...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        LazyColumn(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            items(filteredLocales) { locale ->
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.clickable { onLocaleChange(locale) },
                    leadingContent = {
                        if (locale == currentLocale) {
                            Text(
                                text = "✓",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.width(24.dp))
                        }
                    },
                    headlineContent = {
                        Text(locale.displayName)
                    },
                    supportingContent = {
                        Text(locale.languageTag)
                    }
                )
            }
        }
    }
}