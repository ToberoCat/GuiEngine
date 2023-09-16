package io.github.toberocat.guiengine.exception

open class GuiException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}
