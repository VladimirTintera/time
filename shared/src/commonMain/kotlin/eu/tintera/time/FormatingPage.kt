package eu.tintera.time

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.tintera.locale.availableLocales
import eu.tintera.time.format.UnitVisibility
import eu.tintera.time.format.context.format
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

@Composable
fun FormatingPage(
    paddingValues: PaddingValues
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

    val locales = remember { availableLocales() }

    LazyVerticalGrid(
        modifier = Modifier.padding(paddingValues).consumeWindowInsets(paddingValues).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        columns = GridCells.Adaptive(minSize = 300.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(locales) {
            LocaleItem(
                modifier = Modifier.fillMaxWidth(),
                locale = it,
                time = time
            )
        }
    }
}