package eu.tintera.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.tintera.locale.AppLocale
import eu.tintera.locale.availableLocales
import eu.tintera.locale.displayName
import eu.tintera.locale.languageTag

@Composable
fun LocaleSelectionBottomSheet(
    currentLocale: AppLocale,
    onLocaleChange: (AppLocale) -> Unit,
    onDismissRequest: () -> Unit,
) = ModalBottomSheet(
    onDismissRequest = onDismissRequest
) {
    LazyColumn {
        items(availableLocales().sortedBy { it.displayName }) { locale ->
            ListItem(
                modifier = Modifier.clickable { onLocaleChange(locale) },
                leadingContent = {
                    Text(if (locale == currentLocale) "Selected" else "")
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