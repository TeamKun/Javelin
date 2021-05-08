package team.kun.javelin.metadata

import org.bukkit.Location
import org.bukkit.entity.Entity

sealed class MetadataKey<T>(val value: String) {
    object IsPlayerInteract : MetadataKey<Boolean>("IsPlayerInteract")
    object TargetLocation : MetadataKey<Location>("TargetLocation")
    object Target : MetadataKey<Entity>("Target")
    object Count : MetadataKey<Int>("Count")
}
