package team.kun.javelin.range

import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

abstract class Range {
    abstract fun getEntities(location: Location): List<Entity>
}