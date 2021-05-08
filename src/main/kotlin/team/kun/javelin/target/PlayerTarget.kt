package team.kun.javelin.target

import org.bukkit.ChatColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import team.kun.javelin.ext.getMeta
import team.kun.javelin.ext.getTarget
import team.kun.javelin.ext.removeMeta
import team.kun.javelin.ext.setMeta
import team.kun.javelin.metadata.MetadataKey
import team.kun.javelin.packet.GlowingEntityMetadataPacket

class PlayerTarget(private val player: Player, private val plugin: JavaPlugin) {
    companion object {
        const val TARGET_DISTANCE = 40
    }

    fun set() {
        val target = player.getTarget(TARGET_DISTANCE) ?: return
        val currentTarget = player.getMeta(MetadataKey.Target)
        if (currentTarget != null && target == currentTarget) {
            return
        }
        if (currentTarget != null) {
            if (target == currentTarget) {
                return
            }
            removeTarget(currentTarget, false)
        }
        setTarget(target)
    }

    fun remove() {
        val entity = player.getMeta(MetadataKey.Target) ?: return
        removeTarget(entity)
    }

    fun move() {
        val target = player.getMeta(MetadataKey.Target) ?: return
        if (target.location.distance(player.location) > TARGET_DISTANCE || target.isDead) {
            removeTarget(target)
        }
    }

    private fun setTarget(entity: Entity, showLog: Boolean = true) {
        GlowingEntityMetadataPacket(entity, true).send(player)
        player.setMeta(plugin, MetadataKey.Target, entity)
        if (showLog) {
            player.sendMessage("${entity.name}${ChatColor.WHITE}をターゲットにした")
        }
    }

    private fun removeTarget(entity: Entity, showLog: Boolean = true) {
        GlowingEntityMetadataPacket(entity, false).send(player)
        player.removeMeta(plugin, MetadataKey.Target)
        if (showLog) {
            player.sendMessage("${entity.name}${ChatColor.WHITE}のターゲットを解除した")
        }
    }
}