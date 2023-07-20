package io.github.toberocat.guiengine;

import io.github.toberocat.guiengine.action.OpenGuiAction;
import io.github.toberocat.guiengine.commands.GuiCommands;
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponent;
import io.github.toberocat.guiengine.components.provided.embedded.EmbeddedGuiComponentBuilder;
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponent;
import io.github.toberocat.guiengine.components.provided.item.SimpleItemComponentBuilder;
import io.github.toberocat.guiengine.components.provided.paged.PagedComponent;
import io.github.toberocat.guiengine.components.provided.paged.PagedComponentBuilder;
import io.github.toberocat.guiengine.components.provided.toggle.ToggleItemComponent;
import io.github.toberocat.guiengine.components.provided.toggle.ToggleItemComponentBuilder;
import io.github.toberocat.guiengine.exception.GuiIORuntimeException;
import io.github.toberocat.guiengine.function.FunctionProcessor;
import io.github.toberocat.guiengine.function.call.ActionFunction;
import io.github.toberocat.guiengine.function.call.AddComponentsFunction;
import io.github.toberocat.guiengine.function.call.EditComponentFunction;
import io.github.toberocat.guiengine.function.call.RemoveComponentFunction;
import io.github.toberocat.guiengine.function.compute.GuiComponentPropertyFunction;
import io.github.toberocat.guiengine.function.compute.HasNotPermissionFunction;
import io.github.toberocat.guiengine.function.compute.HasPermissionFunction;
import io.github.toberocat.guiengine.interpreter.DefaultInterpreter;
import io.github.toberocat.guiengine.interpreter.InterpreterManager;
import io.github.toberocat.guiengine.view.DefaultGuiViewManager;
import io.github.toberocat.toberocore.action.ActionCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * This class represents the main plugin class for the GuiEngineApi plugin.
 * It extends JavaPlugin and acts as the entry point for the plugin's functionality.
 * The plugin provides a graphical user interface (GUI) engine for creating and managing custom GUIs in Minecraft.
 * It supports various components like SimpleItemComponent, EmbeddedGuiComponent, ToggleItemComponent, and PagedComponent.
 * Additionally, it allows the registration of functions and actions for GUI interactions.
 * The plugin is licensed under the GNU GENERAL PUBLIC LICENSE.
 * For usage and documentation, refer to the README or the official documentation of the GuiEngineApi.
 *
 * @author Tobias Madlberger (Tobias)
 * @since 04/02/2023
 */
public class GuiEngineApiPlugin extends JavaPlugin {

    /**
     * The singleton instance of the GuiEngineApiPlugin.
     */
    private static GuiEngineApiPlugin instance;

    /**
     * The manager responsible for handling GUI interpreters.
     */
    private InterpreterManager interpreterManager;

    /**
     * The manager responsible for managing GUI views.
     */
    private DefaultGuiViewManager guiViewManager;

    /**
     * Called when the plugin is enabled. Initializes the plugin and registers necessary components, functions, actions, and commands.
     * Creates and initializes the manager instances for GUI views and interpreters.
     * Registers default shared factories for supported GUI components.
     * Register default functions for GUI interactions, such as adding, editing, or removing components.
     * Register default compute functions for evaluating GUI component properties and permissions.
     * Adds a default API instance for handling GUI data.
     * Register default actions for GUI interactions.
     */
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

    /**
     * Returns the singleton instance of the GuiEngineApiPlugin.
     *
     * @return The GuiEngineApiPlugin instance.
     */
    public static @NotNull GuiEngineApiPlugin getPlugin() {
        return instance;
    }

    /**
     * Gets the manager responsible for handling GUI interpreters.
     *
     * @return The InterpreterManager instance.
     */
    public @NotNull InterpreterManager getInterpreterManager() {
        return interpreterManager;
    }

    /**
     * Gets the manager responsible for managing GUI views.
     *
     * @return The DefaultGuiViewManager instance.
     */
    public @NotNull DefaultGuiViewManager getGuiViewManager() {
        return guiViewManager;
    }

    /**
     * Creates the manager instances for GUI views and interpreters.
     */
    private void createManagers() {
        guiViewManager = new DefaultGuiViewManager();
        interpreterManager = new InterpreterManager();
    }

    /**
     * Registers the supported GUI components and their corresponding builders to the GuiEngineApi.
     * This allows the creation of instances with these components from their respective builders.
     */
    private void registerComponents() {
        GuiEngineApi.registerSharedFactory(
                SimpleItemComponent.TYPE,
                SimpleItemComponent.class,
                SimpleItemComponentBuilder.class
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

    /**
     * Registers the default functions for GUI interactions to the FunctionProcessor.
     * These functions include AddComponentsFunction, EditComponentFunction, RemoveComponentFunction, and ActionFunction.
     * Additionally, it registers default compute functions for evaluating GUI component properties and permissions.
     */
    private void registerFunctions() {
        FunctionProcessor.registerFunction(AddComponentsFunction.ID, AddComponentsFunction.class);
        FunctionProcessor.registerFunction(EditComponentFunction.ID, EditComponentFunction.class);
        FunctionProcessor.registerFunction(RemoveComponentFunction.ID, RemoveComponentFunction.class);
        FunctionProcessor.registerFunction(ActionFunction.ID, ActionFunction.class);
        FunctionProcessor.registerComputeFunction(new GuiComponentPropertyFunction());
        FunctionProcessor.registerComputeFunction(new HasPermissionFunction());
        FunctionProcessor.registerComputeFunction(new HasNotPermissionFunction());
    }

    /**
     * Registers custom commands provided by the plugin using the GuiCommands class.
     */
    private void registerCommands() {
        new GuiCommands();
    }

    /**
     * Creates and adds a default API instance named "default" for handling GUI data.
     * The API is initialized with a directory for storing GUI configurations.
     * If an exception occurs while reloading the API, it is ignored.
     */
    private void addDefaultApi() {
        GuiEngineApi guiApi = new GuiEngineApi("default", new File(getDataFolder(), "guis"));
        try {
            guiApi.reload();
        } catch (GuiIORuntimeException ignored) {
            // Ignored if an exception occurs while reloading the API.
        }
    }

    /**
     * Registers the default action for GUI interactions, specifically the OpenGuiAction.
     */
    private void registerActions() {
        ActionCore.register(new OpenGuiAction());
    }

    /**
     * Registers the default interpreter, DefaultInterpreter, to the InterpreterManager.
     */
    private void registerInterpreters() {
        interpreterManager.registerInterpreter(new DefaultInterpreter());
    }

    /**
     * Registers the GUI view manager to Bukkit's event system for handling GUI events.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(guiViewManager, this);
    }
}
