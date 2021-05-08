package team.kun.javelin.listener

import org.bukkit.entity.Player
import org.bukkit.entity.Trident
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import team.kun.javelin.item.JavelinItem
import team.kun.javelin.metadata.PlayerFlagMetadata
import team.kun.javelin.target.PlayerTarget

class ItemListener(private val plugin: JavaPlugin) : Listener {
    private val playerFlagMetadata = PlayerFlagMetadata(plugin)

    @EventHandler
    fun onClick(event: PlayerInteractEvent) {
        val player = event.player
        if (playerFlagMetadata.getFlag(player)) {
            return
        }
        if (event.action == Action.LEFT_CLICK_AIR) {
            if (JavelinItem().equal(player.inventory.itemInMainHand, plugin)) {
                playerFlagMetadata.avoidTwice(player)
                if (player.isSneaking) {
                    JavelinItem().lockOff(player, plugin)
                } else {
                    JavelinItem().lockOn(player, plugin)
                }
            }
        }
    }

    @EventHandler
    fun onLaunch(event: ProjectileLaunchEvent) {
        val entity = event.entity
        if (entity is Trident && JavelinItem().equal(entity.item, plugin)) {
            val shooter = entity.shooter
            if (shooter is Player) {
                JavelinItem().execute(shooter, entity, plugin)
            }
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        PlayerTarget(player, plugin).move()
    }
}