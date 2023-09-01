package io.github.toberocat.guiengine.components.container

/**
 * Represents a selectable component that allows users to make a selection from a set of options.
 * Classes that implement this interface must provide methods for managing the selection model and the currently selected option.
 *
 *
 * Created: 21.05.2023
 * Author: Tobias Madlberger (Tobias)
 */
interface Selectable {
    /**
     * Get the selection model containing available options.
     * This method returns an array of strings representing the available options that can be selected.
     *
     * @return An array of strings representing the selection model.
     */
    val selectionModel: Array<String>
    /**
     * Get the index of the currently selected option from the selection model.
     *
     * @return The index of the currently selected option.
     */
    /**
     * Set the index of the selected option from the selection model.
     * The provided index should be a valid index within the selection model array.
     *
     * @param selected The index of the option to be selected.
     */
    var selected: Int
}