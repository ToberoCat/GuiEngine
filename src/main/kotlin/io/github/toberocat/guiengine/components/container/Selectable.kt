package io.github.toberocat.guiengine.components.container

import io.github.toberocat.guiengine.components.GuiComponent

/**
 * Represents a selectable component that allows users to make a selection from a set of options.
 * Classes that implement this interface must provide methods for managing the selection model and the currently selected option.
 *
 *
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
interface Selectable : GuiComponent {
    val selectionModel: Array<String>
    var selected: Int
}