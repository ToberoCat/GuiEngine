package io.github.toberocat.guiengine.components.container.tab

import io.github.toberocat.guiengine.components.container.LayoutContainer
import io.github.toberocat.guiengine.components.provided.paged.PatternPage
import io.github.toberocat.guiengine.context.GuiContext

interface PagedContainer : LayoutContainer {
    var page: Int
    val availablePages: Int
    fun addPage(context: GuiContext)
    fun addPage(patternPage: PatternPage)
    fun addPage(patternPage: PatternPage, position: Int)

    fun addPage(context: GuiContext, position: Int)
    fun createEmptyPage(): PatternPage?

    fun showNext() = page != availablePages - 1
    fun showPrevious() = page != 0
}