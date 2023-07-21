package io.github.toberocat.guiengine.components.provided.paged;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.toberocat.guiengine.action.NextPageAction;
import io.github.toberocat.guiengine.action.PreviousPageAction;
import io.github.toberocat.guiengine.components.GuiComponent;
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent;
import io.github.toberocat.guiengine.context.GuiContext;
import io.github.toberocat.guiengine.function.GuiFunction;
import io.github.toberocat.guiengine.render.RenderPriority;
import io.github.toberocat.guiengine.utils.*;
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
 * A custom GUI component that represents a paged GUI.
 * It allows displaying multiple pages of components in a specific pattern on a GUI.
 * This component can be navigated using NextPageAction and PreviousPageAction actions.
 * <p>
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


    /**
     * Constructs a new PagedComponent instance with the specified parameters.
     *
     * @param offsetX        The X-axis offset of the component within the GUI.
     * @param offsetY        The Y-axis offset of the component within the GUI.
     * @param width          The width of the component.
     * @param height         The height of the component.
     * @param priority       The rendering priority of the component.
     * @param id             The unique ID of the component.
     * @param clickFunctions The list of click functions associated with the component.
     * @param dragFunctions  The list of drag functions associated with the component.
     * @param closeFunctions The list of close functions associated with the component.
     * @param hidden         Whether the component is hidden or not.
     * @param targetGui      The target GUI to display when this component is clicked.
     * @param copyAir        Whether to copy air items to the target GUI.
     * @param interactions   Whether to enable interactions between this component and the target GUI.
     * @param parent         The parent parser context that contains the GUI definition.
     * @param pattern        The pattern of slots on the GUI to arrange the components.
     * @param showingPage    The index of the currently showing page.
     */
    public PagedComponent(int offsetX, int offsetY, int width, int height, @NotNull RenderPriority priority, @NotNull String id, @NotNull List<GuiFunction> clickFunctions, @NotNull List<GuiFunction> dragFunctions, @NotNull List<GuiFunction> closeFunctions, boolean hidden, @NotNull String targetGui, boolean copyAir, boolean interactions, @NotNull ParserContext parent, int[] pattern, int showingPage) {
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
        List<ParserContext> pages = parent.getOptionalFieldList("page").orElse(new ArrayList<>());
        for (ParserContext page : pages) {
            createPage(page);
        }

        assert context != null;
        assert api != null;
        emptyFill = JsonUtils.getOptionalNode(parent, "fill-empty").map(x -> {
            try {
                return context.interpreter().createComponent(context.interpreter().xmlComponent(x.node(), api), api, context);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).orElse(null);

        embedded = this.pages.get(showingPage);
    }

    /**
     * Adds a new page to the paged component.
     *
     * @param page The new page to add.
     */
    public void addPage(@NotNull GuiContext page) {
        pages.add(page);
    }

    /**
     * Adds a new page to the paged component at the specified position.
     *
     * @param page     The new page to add.
     * @param position The position at which to add the page.
     */
    public void addPage(@NotNull GuiContext page, int position) {
        pages.add(position, page);
    }

    /**
     * Adds a new component to the current page.
     *
     * @param component The component to add.
     */
    public void addComponent(@NotNull GuiComponent component) {
        assert api != null;

        GuiContext page = pages.get(pages.size() - 1);
        int slot = pattern[currentPatternIndex++];

        CoordinatePair pair = Utils.translateFromSlot(slot);
        component.setOffsetX(pair.x());
        component.setOffsetY(pair.y());
        page.add(api, component);

        embedded = this.pages.get(showingPage);
        if (currentPatternIndex < pattern.length) return;
        currentPatternIndex = 0;
        addPage(createEmptyPage());
    }

    /**
     * Creates an empty page for the paged component.
     *
     * @return The created empty page.
     */
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
        if (context == null || api == null) return;
        actions.add(new NextPageAction(this));
        actions.add(new PreviousPageAction(this));
    }

    /**
     * Sets the currently showing page index of the paged component.
     *
     * @param page The index of the page to show.
     * @throws IllegalArgumentException If the specified page is not within the valid bounds.
     */
    public void setShowingPage(int page) {
        if (page < 0 || page >= this.pages.size()) throw new IllegalArgumentException("Page not in valid bounds");
        this.showingPage = page;
        embedded = this.pages.get(showingPage);

        if (context == null) return;

        context.render();
    }

    /**
     * Gets the index of the currently showing page of the paged component.
     *
     * @return The index of the currently showing page.
     */
    public int getShowingPage() {
        return showingPage;
    }

    /**
     * Gets the current page number of the paged component.
     *
     * @return The current page number (1-based index).
     */
    public int getPage() {
        return showingPage + 1;
    }

    /**
     * Gets the total number of available pages in the paged component.
     *
     * @return The total number of available pages.
     */
    public int getAvailablePages() {
        return pages.size();
    }

    /**
     * Parses the components from the parent parser context and adds them to the specified consumer.
     *
     * @param parent    The parent parser context containing the components.
     * @param addToPage The consumer function to add the components to the page.
     * @throws JsonProcessingException If there is an error while processing the JSON data.
     */
    private void parseComponents(@NotNull ParserContext parent, @NotNull Consumer<GuiComponent> addToPage) throws JsonProcessingException {
        assert context != null;
        assert api != null;

        List<ParserContext> components = parent.getOptionalFieldList("component").orElse(new ArrayList<>());
        for (ParserContext component : components) {
            XmlComponent xml = context.interpreter().xmlComponent(component.node(), api);
            GuiComponent guiComponent = context.interpreter().createComponent(xml, api, context);

            if (guiComponent == null) continue;
            addToPage.accept(guiComponent);
        }
    }

    /**
     * Creates a new page for the paged component based on the specified parser context.
     *
     * @param pageNode The parser context representing the page data.
     */
    private void createPage(@NotNull ParserContext pageNode) {
        assert api != null;

        int position = pageNode.getOptionalInt("position").orElse(pages.size() - 1);
        GuiContext page = createEmptyPage();
        try {
            parseComponents(pageNode, component -> page.add(api, component));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        addPage(page, position);
    }
}