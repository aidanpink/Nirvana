package galena.hats.fabric.services;

import galena.hats.network.ClientboundConfigMessage;
import galena.hats.network.ServerboundConfigMessage;
import galena.hats.services.INetwork;
import galena.hats.storage.ConfigStorage;
import galena.hats.storage.ServerConfigStorage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetwork implements INetwork {

    @Override
    public void broadcastConfig(ServerboundConfigMessage message) {
        ClientPlayNetworking.send(message);
    }

    @Override
    public void broadcastConfig(ClientboundConfigMessage message, ServerPlayer player) {
        ServerPlayNetworking.send(player, message);
    }

    public static void registerCodecs() {
        PayloadTypeRegistry.clientboundPlay().register(ClientboundConfigMessage.TYPE.type(), ClientboundConfigMessage.TYPE.codec());
        PayloadTypeRegistry.serverboundPlay().register(ServerboundConfigMessage.TYPE.type(), ServerboundConfigMessage.TYPE.codec());
    }

    public static void registerClientHandler() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundConfigMessage.TYPE.type(), (message, context) -> {
            message.values().forEach(ConfigStorage::receive);
        });
    }

    public static void registerServerHandler() {
        ServerPlayNetworking.registerGlobalReceiver(ServerboundConfigMessage.TYPE.type(), (message, context) -> {
            context.server().execute(() -> {
                ServerConfigStorage.receive(context.player(), message);
            });
        });
    }

}
