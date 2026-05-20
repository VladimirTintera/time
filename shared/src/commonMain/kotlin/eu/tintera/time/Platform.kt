package eu.tintera.time

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform