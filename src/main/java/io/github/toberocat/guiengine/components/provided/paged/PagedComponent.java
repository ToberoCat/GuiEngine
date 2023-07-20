package io.github.toberocat.guiengine.components.provided.paged;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.action.PreviousPageAction;
import io.github.toberocat.guiengine.action.NextPageAction;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.utils.*;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.xml.XmlComponent;
import io.github.toberocat.toberocore.action.Action;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created: 30.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class PagedComponent extends EmbeddedGuiComponent {
    public static final String TYPE = "paged";

    private final @NotNull List<GuiContext> pages;
    private @Nullable GuiComponent emptyFill;
    protected final @NotNull ParserContext parent;
    protected final int[] pattern;
    protected int showingPage;
    private int currentPatternIndex;


    public PagedComponent(int offsetX,
                          int offsetY,
                          int width,
                          int height,
                          @NotNull RenderPriority priority,
                          @NotNull String id,
                          @NotNull List<GuiFunction> clickFunctions,
                          @NotNull List<GuiFunction> dragFunctions,
                          @NotNull List<GuiFunction> closeFunctions,
                          boolean hidden,
                          @NotNull String targetGui,
                          boolean copyAir,
                          boolean interactions,
                          @NotNull ParserContext parent,
                          int[] pattern,
                          int showingPage) {
        super(offsetX, offsetY, width, height, priority, id, clickFunctions, dragFunctions, closeFunctions, hidden, targetGui, copyAir, interactions);
        this.parent = parent;
        this.pattern = pattern;
        this.showingPage = showingPage;
        this.pages = new ArrayList<>();
    }

    @Override
    public void serialize(@NotNull GeneratorContext gen, @NotNull SerializerProvider serializers) throws IOException {
        super.serialize(gen, serializers);
        JsonUtils.writeArray(gen, "pattern", pattern);
        gen.writeNumberField("showing-page", showingPage);
        gen.writeRaw(parent.node().toString());
    }

    @Override
    public void onViewInit(@NotNull Map<String, String> placeholders) {
        addPage(createEmptyPage());

        try {
            parseComponents(parent, this::addComponent);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<ParserContext> pages = JsonUtils.getOptionalFieldList(parent, "page").orElse(new ArrayList<>());
        for (ParserContext page : pages) {
            createPage(page);
        }

        assert context != null;
        assert api != null;
        emptyFill = JsonUtils.getOptionalNode(parent, "fill-empty").map(x -> {
            try {
                return context.interpreter().createComponent(
                        context.interpreter().xmlComponent(x.node(), api), api, context);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);

        embedded = this.pages.get(showingPage);
    }

    public void addPage(@NotNull GuiContext page) {
        pages.add(page);
    }

    public void addPage(@NotNull GuiContext page, int position) {
        pages.add(position, page);
    }

    public void addComponent(@NotNull GuiComponent component) {
        assert api != null;

        GuiContext page = pages.get(pages.size() - 1);
        int slot = pattern[currentPatternIndex++];

        CoordinatePair pair = Utils.translateFromSlot(slot);
        component.setOffsetX(pair.x());
        component.setOffsetY(pair.y());
        page.add(api, component);

        embedded = this.pages.get(showingPage);
        if (currentPatternIndex < pattern.length)
            return;
        currentPatternIndex = 0;
        addPage(createEmptyPage());
    }

    private @NotNull GuiContext createEmptyPage() {
        assert context != null;
        GuiContext page = new GuiContext(context.interpreter(), "Page " + pages.size(), width, height);
        page.setInventory(context.inventory());
        page.setViewer(context.viewer());
        return page;
    }

    @Override
    public @NotNull String getType() {
        return TYPE;
    }

    @Override
    public void render(@NotNull Player viewer, @NotNull ItemStack[][] buffer) {
        if (emptyFill != null) {
            for (int slot : pattern) {
                CoordinatePair pair = Utils.translateFromSlot(slot);
                emptyFill.setOffsetX(offsetX + pair.x());
                emptyFill.setOffsetY(offsetY + pair.y());
                emptyFill.render(viewer, buffer);
            }
        }
        super.render(viewer, buffer);
    }

    @Override
    public void addActions(@NotNull Set<Action> actions) {
        if (context == null || api == null)
            return;
        actions.add(new NextPageAction(this));
        actions.add(new PreviousPageAction(this));
    }

    public void setShowingPage(int page) {
        if (page < 0 || page >= this.pages.size())
            throw new IllegalArgumentException("Page not in valid bounds");
        this.showingPage = page;
        embedded = this.pages.get(showingPage);

        if (context == null)
            return;

        context.render();
    }

    public int getShowingPage() {
        return showingPage;
    }
    public int getPage() {
        return showingPage + 1;
    }

    public int getAvailablePages() {
        return pages.size();
    }

    private void parseComponents(@NotNull ParserContext parent, @NotNull Consumer<GuiComponent> addToPage)
            throws JsonProcessingException {
        assert context != null;
        assert api != null;

        List<ParserContext> components = JsonUtils.getOptionalFieldList(parent, "component")
                .orElse(new ArrayList<>());
        for (ParserContext component : components) {
            XmlComponent xml = context.interpreter().xmlComponent(component.node(), api);
            GuiComponent guiComponent = context.interpreter().createComponent(xml, api, context);

            if (guiComponent == null)
                continue;
            addToPage.accept(guiComponent);
        }
    }

    private void createPage(@NotNull ParserContext pageNode) {
        assert api != null;

        int position = JsonUtils.getOptionalInt(pageNode, "position").orElse(pages.size() - 1);
        GuiContext page = createEmptyPage();
        try {
            parseComponents(pageNode, component -> page.add(api, component));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        addPage(page, position);
    }
}
