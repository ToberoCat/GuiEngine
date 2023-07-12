package io.github.toberocat.guiengine.api.components;

/**
 * Created: 21.05.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface Selectable {
    String[] getSelectionModel();
    int getSelected();

    void setSelected(int selected);
}
