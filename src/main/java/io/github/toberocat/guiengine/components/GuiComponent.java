package io.github.toberocat.guiengine.components;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.GuiEngineApi;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.event.GuiEvents;
import io.github.toberocat.guiengine.utils.CoordinatePair;
import io.github.toberocat.guiengine.utils.Utils;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Set;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public interface GuiComponent extends GuiEvents {

    @NotNull String getId();

    @NotNull String getType();

    @NotNull RenderPriority renderPriority();

    boolean hidden();

    int offsetX();

    int offsetY();

    int width();

    int height();

    void render(@NotNull Player viewer, @NotNull ItemStack[][] inventory);

    void setApi(@NotNull GuiEngineApi api);

    void setContext(@NotNull GuiContext context);

    void setHidden(boolean hidden);

    void setOffsetX(int x);

    void setOffsetY(int y);

    void serialize(@NotNull JsonGenerator gen, @NotNull SerializerProvider serializers) throws IOException;

    default void addActions(@NotNull Set<Action> actions) {
    }

    default boolean isInComponent(int slot) {
        CoordinatePair pair = Utils.translateFromSlot(slot);
        int x = pair.x();
        int y = pair.y();

        return x >= offsetX() && x < offsetX() + width() &&
                y >= offsetY() && y < offsetY() + height();
    }
}
