package team.kun.javelin.ext

import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.plugin.java.JavaPlugin
import team.kun.javelin.metadata.NbtKey

fun <T> PersistentDataContainer.getNbt(plugin: JavaPlugin, nbtKey: NbtKey<T>): T? {
    return get(nbtKey.namespacedKey(plugin), nbtKey.persistentDataType)
}

fun <T> PersistentDataContainer.getNbt(plugin: JavaPlugin, nbtKey: NbtKey<T>, default: T): T {
    return get(nbtKey.namespacedKey(plugin), nbtKey.persistentDataType) ?: default
}

fun <T> PersistentDataContainer.setNbt(plugin: JavaPlugin, nbtKey: NbtKey<T>, value: T?) {
    value ?: return
    set(nbtKey.namespacedKey(plugin), nbtKey.persistentDataType, value)
}

fun <T> PersistentDataContainer.removeNbt(plugin: JavaPlugin, nbtKey: NbtKey<T>) {
    remove(nbtKey.namespacedKey(plugin))
}
