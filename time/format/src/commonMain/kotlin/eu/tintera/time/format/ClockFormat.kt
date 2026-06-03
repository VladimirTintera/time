package eu.tintera.time.format

class ClockFormat(
    internal val block: ClockFormatScope.() -> Unit
) {
    companion object {
        operator fun invoke(
            block: ClockFormatScope.() -> Unit
        ): ClockFormat = ClockFormat(block)
    }
}