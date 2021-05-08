package team.kun.javelin.item

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Trident
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import team.kun.javelin.ext.getMeta
import team.kun.javelin.ext.playSound
import team.kun.javelin.ext.setMeta
import team.kun.javelin.metadata.MetadataKey
import team.kun.javelin.rx.Observable
import team.kun.javelin.target.PlayerTarget

class JavelinItem : Item() {
    override val name = "ジャベリン"
    override val description = listOf(
            "${ChatColor.YELLOW}左クリック${ChatColor.GRAY} : 照準をセットする",
            "${ChatColor.YELLOW}左クリック + スニーク${ChatColor.GRAY} : 照準をリセットする",
            "${ChatColor.YELLOW}右クリック${ChatColor.GRAY} : 発射"
    )
    override val itemStack = ItemStack(Material.TRIDENT)

    fun lockOn(player: Player, plugin: JavaPlugin) {
        PlayerTarget(player, plugin).set()
    }

    fun lockOff(player: Player, plugin: JavaPlugin) {
        PlayerTarget(player, plugin).remove()
    }

    fun execute(player: Player, trident: Trident, plugin: JavaPlugin) {
        trident.setGravity(false)
        trident.setMeta(plugin, MetadataKey.Count, 0)
        val target = player.getMeta(MetadataKey.Target)
        trident.location.playSound(Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f)
        Observable.interval(5)
                .take(200)
                .doOnNext {
                    val count = trident.getMeta(MetadataKey.Count, 0)
                    object : BukkitRunnable() {
                        override fun run() {
                            if (it == 0L) {
                                up(player, trident, target)
                            }

                            if (count == 0) {
                                val targetLocation = getUpLocation(player, trident, target)
                                if (Math.abs(targetLocation.x - trident.location.x) <= 3.0 && Math.abs(targetLocation.z - trident.location.z) <= 3.0) {
                                    trident.setMeta(plugin, MetadataKey.Count, 1)
                                    attack(player, trident, target)
                                }
                            } else if (count == 1) {
                                if (trident.isOnGround || trident.location.world?.getNearbyEntities(trident.location, 1.0, 1.0, 1.0)?.isNotEmpty() == true) {
                                    trident.setMeta(plugin, MetadataKey.Count, 2)
                                }
                            }
                        }
                    }.runTaskLater(plugin, 1)
                }
                .doOnCompleteCondition {
                    trident.getMeta(MetadataKey.Count, 0) == 2
                }
                .doOnComplete {
                    object : BukkitRunnable() {
                        override fun run() {
                            trident.location.world?.createExplosion(trident.location, 3.0f)
                            trident.remove()
                        }
                    }.runTaskLater(plugin, 1)
                }
                .subscribe(plugin)
    }

    private fun up(player: Player, trident: Trident, target: Entity?) {
        val targetLocation = getUpLocation(player, trident, target)
        trident.velocity = targetLocation.subtract(trident.location).toVector().normalize().multiply(1.0)
    }

    private fun getUpLocation(player: Player, trident: Trident, target: Entity?): Location {
        return target?.location?.clone()?.add(0.0, 20.0, 0.0)
                ?: player.eyeLocation.add(player.eyeLocation.direction.multiply(20)).clone()?.add(0.0, 20.0, 0.0)

    }

    private fun attack(player: Player, trident: Trident, target: Entity?) {
        val targetLocation = target?.location
                ?: player.eyeLocation.add(player.eyeLocation.direction.multiply(20)).clone()

        trident.velocity = targetLocation.subtract(trident.location).toVector().normalize().multiply(2.0)
        trident.setGravity(true)
    }
}