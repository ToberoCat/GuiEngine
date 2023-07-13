package io.github.toberocat.guiengine;

import io.github.toberocat.guiengine.action.OpenGuiAction;
import io.github.toberocat.guiengine.commands.GuiCommands;
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent;
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponentBuilder;
import io.github.toberocat.guiengine.components.provided.head.HeadItemComponent;
import io.github.toberocat.guiengine.components.provided.head.HeadItemComponentBuilder;
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent;
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponentBuilder;
import io.github.toberocat.guiengine.components.provided.paged.PagedComponent;
import io.github.toberocat.guiengine.components.provided.paged.PagedComponentBuilder;
import io.github.toberocat.guiengine.components.provided.toggle.ToggleItemComponent;
import io.github.toberocat.guiengine.components.provided.toggle.ToggleItemComponentBuilder;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.function.call.ActionFunction;
import io.github.toberocat.guiengine.function.call.AddComponentsFunction;
import io.github.toberocat.guiengine.function.call.EditComponentFunction;
import io.github.toberocat.guiengine.function.call.RemoveComponentFunction;
import io.github.toberocat.guiengine.function.compute.GuiComponentPropertyFunction;
import io.github.toberocat.guiengine.interpreter.InterpreterManager;
import io.github.toberocat.guiengine.interpreter.DefaultInterpreter;
import io.github.toberocat.guiengine.view.DefaultGuiViewManager;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import io.github.toberocat.toberocore.action.ActionCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created: 04/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class GuiEngineApiPlugin extends JavaPlugin {
    private static GuiEngineApiPlugin instance;
    private InterpreterManager interpreterManager;
    private DefaultGuiViewManager guiViewManager;

    @Override
    public void onEnable() {
        instance = this;

        createManagers();
        registerListeners();

        registerInterpreters();
        registerComponents();
        registerFunctions();
        registerActions();

        addDefaultApi();
        registerCommands();
    }

    public static @NotNull GuiEngineApiPlugin getPlugin() {
        return instance;
    }

    public @NotNull InterpreterManager getInterpreterManager() {
        return interpreterManager;
    }

    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return guiViewManager;
    }

    private void createManagers() {
        guiViewManager = new DefaultGuiViewManager();
        interpreterManager = new InterpreterManager();
    }

    private void registerComponents() {
        GuiEngineApi.registerSharedFactory(
                SimpleItemComponent.TYPE,
                SimpleItemComponent.class,
                SimpleItemComponentBuilder.class
        );

        GuiEngineApi.registerSharedFactory(
                HeadItemComponent.TYPE,
                HeadItemComponent.class,
                HeadItemComponentBuilder.class
        );

        GuiEngineApi.registerSharedFactory(
                EmbeddedGuiComponent.TYPE,
                EmbeddedGuiComponent.class,
                EmbeddedGuiComponentBuilder.class
        );

        GuiEngineApi.registerSharedFactory(
                ToggleItemComponent.TYPE,
                ToggleItemComponent.class,
                ToggleItemComponentBuilder.class
        );

        GuiEngineApi.registerSharedFactory(
                PagedComponent.TYPE,
                PagedComponent.class,
                PagedComponentBuilder.class
        );
    }

    private void registerFunctions() {
        FunctionProcessor.registerFunction(AddComponentsFunction.ID, AddComponentsFunction.class);
        FunctionProcessor.registerFunction(EditComponentFunction.ID, EditComponentFunction.class);
        FunctionProcessor.registerFunction(RemoveComponentFunction.ID, RemoveComponentFunction.class);
        FunctionProcessor.registerFunction(ActionFunction.ID, ActionFunction.class);
        FunctionProcessor.registerComputeFunction(new GuiComponentPropertyFunction());
    }

    private void registerCommands() {
        new GuiCommands();
    }

    private void addDefaultApi() {
        GuiEngineApi guiApi = new GuiEngineApi("default", new File(getDataFolder(), "guis"));
        try {
            guiApi.reload();
        } catch (GuiIORuntimeException ignored) {

        }
    }

    private void registerActions() {
        ActionCore.register(new OpenGuiAction());
    }

    private void registerInterpreters() {
        interpreterManager.registerInterpreter(new DefaultInterpreter());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(guiViewManager, this);
    }
}
