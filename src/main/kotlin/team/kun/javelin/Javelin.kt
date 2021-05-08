package team.kun.javelin

import org.bukkit.permissions.PermissionDefault
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.java.annotation.command.Command
import org.bukkit.plugin.java.annotation.command.Commands
import org.bukkit.plugin.java.annotation.dependency.Dependency
import org.bukkit.plugin.java.annotation.dependency.DependsOn
import org.bukkit.plugin.java.annotation.permission.Permission
import org.bukkit.plugin.java.annotation.permission.Permissions
import org.bukkit.plugin.java.annotation.plugin.ApiVersion
import org.bukkit.plugin.java.annotation.plugin.Plugin
import org.bukkit.plugin.java.annotation.plugin.author.Author
import team.kun.javelin.command.GiveCommand
import team.kun.javelin.ext.initCommand
import team.kun.javelin.ext.registerListener
import team.kun.javelin.listener.ItemListener

@Plugin(name = "Javelin", version = "1.0-SNAPSHOT")
@Author("ReyADayer")
@DependsOn(
        Dependency("ProtocolLib"),
)
@Commands(
        Command(
                name = PluginCommands.GIVE,
                desc = "give command",
                usage = "/<command>",
                permission = PluginPermissions.ADMIN,
                permissionMessage = "You don't have <permission>"
        ),
)
@Permissions(
        Permission(
                name = PluginPermissions.ADMIN,
                desc = "Gives access to Atlantis admin commands",
                defaultValue = PermissionDefault.OP
        )
)
@ApiVersion(ApiVersion.Target.v1_15)
class Javelin : JavaPlugin() {
    override fun onEnable() {
        registerListener(ItemListener(this))
        initCommand(PluginCommands.GIVE, GiveCommand(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}