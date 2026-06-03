package eu.tintera.time.format

internal actual val intervalFormatterCache: Cache<IntervalCacheKey, IntervalFormatter>
    get() = CacheImpl()