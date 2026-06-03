package eu.tintera.time

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimePeriod

@Composable
fun EditDateTimePeriodDialog(
    initialPeriod: DateTimePeriod,
    onDismissRequest: () -> Unit,
onPeriodSaved: (DateTimePeriod) -> Unit
) {
    // Lokální stav pro každou složku periody (převádíme na String pro snadnou editaci)
    var years by remember { mutableStateOf(initialPeriod.years.toString()) }
    var months by remember { mutableStateOf(initialPeriod.months.toString()) }
    var days by remember { mutableStateOf(initialPeriod.days.toString()) }
    var hours by remember { mutableStateOf(initialPeriod.hours.toString()) }
    var minutes by remember { mutableStateOf(initialPeriod.minutes.toString()) }
    var seconds by remember { mutableStateOf(initialPeriod.seconds.toString()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Editace DateTimePeriod") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Můžeš zadat i záporné hodnoty pro simulaci reverzního chování.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                PeriodRow(label = "Roky", value = years, onValueChange = { years = it })
                PeriodRow(label = "Měsíce", value = months, onValueChange = { months = it })
                PeriodRow(label = "Dny", value = days, onValueChange = { days = it })

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                PeriodRow(label = "Hodiny", value = hours, onValueChange = { hours = it })
                PeriodRow(label = "Minuty", value = minutes, onValueChange = { minutes = it })
                PeriodRow(label = "Sekundy", value = seconds, onValueChange = { seconds = it })
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Bezpečný parsing s fallbackem na 0 při chybě zadaní
                    val finalPeriod = DateTimePeriod(
                        years = years.toIntOrNull() ?: 0,
                        months = months.toIntOrNull() ?: 0,
                        days = days.toIntOrNull() ?: 0,
                        hours = hours.toIntOrNull() ?: 0,
                        minutes = minutes.toIntOrNull() ?: 0,
                        seconds = seconds.toIntOrNull() ?: 0
                    )
                    onPeriodSaved(finalPeriod)
                }
            ) {
                Text("Uložit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Zrušit")
            }
        }
    )
}

@Composable
private fun PeriodRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)

        // Tlačítko Mínus
        IconButton(
            onClick = {
                val current = value.toIntOrNull() ?: 0
                onValueChange((current - 1).toString())
            }
        ) {
            Text("-", style = MaterialTheme.typography.titleLarge)
        }

        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // Povolíme pouze čísla a případné mínus na začátku
                if (newValue.isEmpty() || newValue == "-" || newValue.toIntOrNull() != null) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier.width(80.dp),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Tlačítko Plus
        IconButton(
            onClick = {
                val current = value.toIntOrNull() ?: 0
                onValueChange((current + 1).toString())
            }
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
}