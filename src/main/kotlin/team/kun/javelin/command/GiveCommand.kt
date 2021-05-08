package team.kun.javelin.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import team.kun.javelin.item.JavelinItem

class GiveCommand(private val plugin: JavaPlugin) : BaseCommand(plugin) {
    override fun onCommandByPlayer(player: Player, command: Command, label: String, args: CommandArgs): Boolean {
        val selector = args[0] ?: return false
        val players = plugin.server.selectEntities(player, selector).filterIsInstance<Player>()
        val amount = args[1]?.toIntOrNull() ?: 1
        players.forEach {
            it.inventory.addItem(JavelinItem().toItemStack(amount, plugin))
        }
        return true
    }

    override fun onCommandByOther(sender: CommandSender, command: Command, label: String, args: CommandArgs): Boolean {
        sender.sendMessage("You must be a player!")
        return false
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        when (args.size) {
            1 -> {
                val keys = plugin.server.onlinePlayers.map { it.name }.toMutableList()
                keys.addAll(listOf("@a", "@r", "@p"))
                return keys.filter { it.startsWith(args[0]) }
            }
            2 -> {
                return listOf("<amount>")
            }
            else -> {
                return emptyList()
            }
        }
    }

}