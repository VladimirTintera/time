package eu.tintera.time.core

import kotlinx.datetime.*

/**
 * Adjusts this [LocalDate] to the nearest date that falls on the specified [DayOfWeek].
 *
 * Example:
 * ```kotlin
 * val today = LocalDate(2026, 6, 4) // Thursday
 * val previousMonday = today.moveTo(DayOfWeek.MONDAY, MoveDirection.Backward)
 * ```
 *
 * @param target The target day of the week to navigate to.
 * @param direction The direction to search (default is [MoveDirection.Backward]).
 * @return A new [LocalDate] adjusted to the target weekday.
 */
fun LocalDate.moveTo(
    target: DayOfWeek,
    direction: MoveDirection = MoveDirection.Backward
): LocalDate {
    val currentIsoValue = this.dayOfWeek.isoDayNumber
    val firstIsoValue = target.isoDayNumber

    val diff = (currentIsoValue - firstIsoValue + 7) % 7

    return when (direction) {
        MoveDirection.Backward -> {
            val daysToSubtract = if (diff == 0) 7 else diff
            minus(daysToSubtract, DateTimeUnit.DAY)
        }

        MoveDirection.Forward -> {
            val daysToPlus = if (diff == 0) 7 else (7 - diff)
            plus(daysToPlus, DateTimeUnit.DAY)
        }
    }
}


/**
 * Adjusts this [LocalDate] to the nearest date that falls on the specified [DayOfWeek],
 * or returns this date if it already falls on the target weekday.
 *
 * Example:
 * ```kotlin
 * val today = LocalDate(2026, 6, 4) // Thursday
 * val nextThursdayOrCurrent = today.moveToOrCurrent(DayOfWeek.THURSDAY, MoveDirection.Forward)
 * ```
 *
 * @param target The target day of the week to navigate to.
 * @param direction The direction to search if this date does not match the target weekday (default is [MoveDirection.Backward]).
 * @return A new [LocalDate] adjusted to the target weekday, or this date.
 */
fun LocalDate.moveToOrCurrent(
    target: DayOfWeek,
    direction: MoveDirection = MoveDirection.Backward
): LocalDate {
    if (this.dayOfWeek == target) return this
    return moveTo(target, direction)
}