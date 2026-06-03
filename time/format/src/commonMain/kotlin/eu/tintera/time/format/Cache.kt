package eu.tintera.time.format

import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi



internal interface Cache<K, V> {
    fun getOrPut(key: K, createValue: () -> V): V
}

@OptIn(ExperimentalAtomicApi::class)
internal class CacheImpl<K, V> : Cache<K, V> {
    private val cacheRef = AtomicReference<Map<K, V>>(emptyMap())

    override fun getOrPut(key: K, createValue: () -> V): V {

        cacheRef.load()[key]?.let { return it }

        while (true) {
            val currentMap = cacheRef.load()
            currentMap[key]?.let { return it }

            val newValue = createValue()
            val newMap = currentMap + (key to newValue)

            if (cacheRef.compareAndSet(currentMap, newMap)) {
                return newValue
            }
        }
    }
}

internal class EmptyCache<K, V> : Cache<K, V> {
    override fun getOrPut(key: K, createValue: () -> V): V = createValue()
}