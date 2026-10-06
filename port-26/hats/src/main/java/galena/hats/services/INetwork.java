package galena.hats.services;

import galena.hats.network.ClientboundConfigMessage;
import galena.hats.network.ServerboundConfigMessage;
import net.minecraft.server.level.ServerPlayer;

public interface INetwork {

    /**
     * client to server
     */
    void broadcastConfig(ServerboundConfigMessage message);

    /**
     * server to client
     */
    void broadcastConfig(ClientboundConfigMessage message, ServerPlayer player);

}
