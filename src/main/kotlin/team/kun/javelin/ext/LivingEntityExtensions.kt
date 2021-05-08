package team.kun.javelin.ext

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import team.kun.javelin.range.RectRange

fun LivingEntity.getTarget(distance: Int, ignoreBlocks: Boolean = false): Entity? {
    val currentLocation = eyeLocation
    val vector = eyeLocation.direction
    val range = RectRange(0.6, 0.6, 0.6)
    repeat(distance) {
        val targetLocation = currentLocation.clone()
        if (!ignoreBlocks && !targetLocation.block.isEmpty) {
            return null
        }
        val entities = range.getEntities(targetLocation).filter { it != this }
        if (entities.isNotEmpty()) {
            return entities.firstOrNull()
        }
        currentLocation.add(vector)
    }
    return null
}