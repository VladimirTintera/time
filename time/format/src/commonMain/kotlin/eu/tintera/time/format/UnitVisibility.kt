package eu.tintera.time.format

/**
 * Visibility rule for displaying a unit in a formatted string.
 */
enum class UnitVisibility {
    /**
     * Display the unit automatically only if it is non-zero.
     */
    Auto,

    /**
     * Force the unit to be displayed even if its value is zero.
     */
    Required
}