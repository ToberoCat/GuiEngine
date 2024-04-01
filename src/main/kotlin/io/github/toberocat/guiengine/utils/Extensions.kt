package io.github.toberocat.guiengine.utils

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

fun <T, Z> ItemStack.getPersistentData(key: NamespacedKey, type: PersistentDataType<T, Z>) =
    itemMeta?.persistentDataContainer?.get(key, type)