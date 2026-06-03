package eu.tintera.time

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

enum class DurationUnitType { Minutes, Hours, Days, Seconds }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDurationDialog(
    initialDuration: Duration,
    onDismissRequest: () -> Unit,
    onDurationSaved: (Duration) -> Unit
) {
    // Detekce výchozích hodnot
    val isInitiallyNegative = initialDuration.isNegative()
    val absDuration = initialDuration.absoluteValue

    // Zjistíme nejvhodnější jednotku pro zobrazení
    val initialUnit = when {
        absDuration.inWholeDays % 1 == 0L && absDuration.inWholeDays > 0 -> DurationUnitType.Days
        absDuration.inWholeHours % 1 == 0L && absDuration.inWholeHours > 0 -> DurationUnitType.Hours
        else -> DurationUnitType.Minutes
    }

    val initialAmount = when (initialUnit) {
        DurationUnitType.Days -> absDuration.inWholeDays
        DurationUnitType.Hours -> absDuration.inWholeHours
        DurationUnitType.Minutes -> absDuration.inWholeMinutes
        DurationUnitType.Seconds -> absDuration.inWholeSeconds
    }.toString()

    var amount by remember { mutableStateOf(initialAmount) }
    var selectedUnit by remember { mutableStateOf(initialUnit) }
    var isNegative by remember { mutableStateOf(isInitiallyNegative) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Editace Duration") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Přepínač znaménka (Kladná vs Záporná)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Směr / Znaménko:")
                    Spacer(modifier = Modifier.weight(1f))
                    FilterChip(
                        selected = !isNegative,
                        onClick = { isNegative = false },
                        label = { Text("Kladná (+)") }
                    )
                    FilterChip(
                        selected = isNegative,
                        onClick = { isNegative = true },
                        label = { Text("Záporná (-)") }
                    )
                }

                // Vstup množství a jednotky
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = amount,
                        onValueChange = { newValue ->
                            if (newValue.isEmpty() || newValue.toLongOrNull() != null) {
                                amount = newValue
                            }
                        },
                        label = { Text("Množství") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    // Dropdown pro výběr jednotky času
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = it },
                        modifier = Modifier.width(130.dp)
                    ) {
                        OutlinedTextField(
                            value = when (selectedUnit) {
                                DurationUnitType.Minutes -> "Minuty"
                                DurationUnitType.Hours -> "Hodiny"
                                DurationUnitType.Days -> "Dny"
                                DurationUnitType.Seconds -> "Sekundy"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jednotka") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                        )
                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            DurationUnitType.entries.forEach { unit ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            when (unit) {
                                                DurationUnitType.Minutes -> "Minuty"
                                                DurationUnitType.Hours -> "Hodiny"
                                                DurationUnitType.Days -> "Dny"
                                                DurationUnitType.Seconds -> "Sekundy"
                                            }
                                        )
                                    },
                                    onClick = {
                                        selectedUnit = unit
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rawAmount = amount.toLongOrNull() ?: 0L
                    var durationResult = when (selectedUnit) {
                        DurationUnitType.Days -> rawAmount.days
                        DurationUnitType.Hours -> rawAmount.hours
                        DurationUnitType.Minutes -> rawAmount.minutes
                        DurationUnitType.Seconds -> rawAmount.seconds
                    }

                    if (isNegative) {
                        durationResult = -durationResult
                    }

                    onDurationSaved(durationResult)
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