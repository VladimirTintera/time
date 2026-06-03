package eu.tintera.time.format

internal actual val formatterCache: Cache<FormatterKey, DateTimeFormatter>
    get() = EmptyCache()