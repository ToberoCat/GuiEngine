package io.github.toberocat.guiengine.render;

/**
 * RenderPriority defines the rendering priority levels for GUI components.
 * <p>
 * Created: 04/02/2023
 * Author: Tobias Madlberger (Tobias)
 */
public enum RenderPriority {
    /**
     * The highest rendering priority.
     */
    HIGHEST,

    /**
     * A high-rendering priority.
     */
    HIGH,

    /**
     * The normal rendering priority (default).
     */
    NORMAL,

    /**
     * A low-rendering priority.
     */
    LOW,

    /**
     * The lowest rendering priority.
     */
    LOWEST
}