package eu.tintera.time.core.context

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals

class InstantExtTest {

    @Test
    fun testInstantToLocalDateTimeWithContext() {
        val instant = kotlin.time.Instant.fromEpochMilliseconds(1672531200000L) // 2023-01-01T00:00:00Z
        val tz = TimeZone.UTC
        val ldt = with(tz) {
            instant.toLocalDateTime()
        }
        assertEquals(LocalDateTime(2023, 1, 1, 0, 0, 0, 0), ldt)
    }
}
