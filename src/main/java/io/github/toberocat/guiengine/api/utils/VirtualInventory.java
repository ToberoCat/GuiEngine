package io.github.toberocat.guiengine.api.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Created: 29.04.2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class VirtualInventory implements Inventory {
    private final int height;
    private final Runnable reload;

    public VirtualInventory(int height, Runnable reload) {
        this.height = height;
        this.reload = reload;
    }

    @Override
    public int getSize() {
        return height * 9;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setMaxStackSize(int size) {
    }

    @Nullable
    @Override
    public ItemStack getItem(int index) {
        return null;
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item) {

    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> addItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        return new HashMap<>();
    }

    @NotNull
    @Override
    public HashMap<Integer, ItemStack> removeItem(@NotNull ItemStack... items) throws IllegalArgumentException {
        return new HashMap<>();
    }

    @NotNull
    @Override
    public ItemStack[] getContents() {
        return new ItemStack[54];
    }

    @Override
    public void setContents(@NotNull ItemStack[] items) throws IllegalArgumentException {
        reload.run(); // ToDo: Maybe make this one only reload own inventory
    }

    @NotNull
    @Override
    public ItemStack[] getStorageContents() {
        return new ItemStack[0];
    }

    @Override
    public void setStorageContents(@NotNull ItemStack[] items) throws IllegalArgumentException {

    }

    @Override
    public boolean contains(@NotNull Material material) throws IllegalArgumentException {
        return false;
    }

    @Override
    public boolean contains(@Nullable ItemStack item) {
        return false;
    }

    @Override
    public boolean contains(@NotNull Material material, int amount) throws IllegalArgumentException {
        return false;
    }

    @Override
    public boolean contains(@Nullable ItemStack item, int amount) {
        return false;
    }

    @Override
    public boolean containsAtLeast(@Nullable ItemStack item, int amount) {
        return false;
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@NotNull Material material) throws IllegalArgumentException {
        return new HashMap<>();
    }

    @NotNull
    @Override
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
        return new HashMap<>();
    }

    @Override
    public int first(@NotNull Material material) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public int first(@NotNull ItemStack item) {
        return 0;
    }

    @Override
    public int firstEmpty() {
        return 0;
    }

    @Override
    public void remove(@NotNull Material material) throws IllegalArgumentException {

    }

    @Override
    public void remove(@NotNull ItemStack item) {

    }

    @Override
    public void clear(int index) {

    }

    @Override
    public void clear() {

    }

    @NotNull
    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public InventoryType getType() {
        return InventoryType.CHEST;
    }

    @Nullable
    @Override
    public InventoryHolder getHolder() {
        return null;
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator() {
        return new ArrayList<ItemStack>().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<ItemStack> iterator(int index) {
        return new ArrayList<ItemStack>().listIterator();
    }

    @Nullable
    @Override
    public Location getLocation() {
        return null;
    }
}
