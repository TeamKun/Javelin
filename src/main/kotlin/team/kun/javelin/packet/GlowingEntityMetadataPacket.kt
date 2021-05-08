package team.kun.javelin.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.entity.Entity
import org.bukkit.entity.Player


class GlowingEntityMetadataPacket(
        private val entity: Entity,
        private val isGlowing: Boolean
) : PacketClient() {
    override fun send(player: Player) {
        val packetContainer = getPacketContainer()
        sendPacket(player, packetContainer)
    }

    private fun getPacketContainer(): PacketContainer {
        val packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_METADATA)
        packet.modifier.writeDefaults()
        packet.integers.write(0, entity.entityId)

        val watcher = WrappedDataWatcher()

        watcher.entity = entity

        if (isGlowing) {
            watcher.setObject(0, WrappedDataWatcher.Registry.get(java.lang.Byte::class.java), 0x40.toByte())
        } else {
            watcher.setObject(0, WrappedDataWatcher.Registry.get(java.lang.Byte::class.java), 0x00.toByte())
        }

        packet.watchableCollectionModifier.write(0, watcher.watchableObjects)

        return packet
    }
}