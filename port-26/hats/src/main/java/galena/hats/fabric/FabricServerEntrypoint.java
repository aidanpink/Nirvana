package galena.hats.fabric;

import galena.hats.HatsCommand;
import galena.hats.fabric.services.FabricNetwork;
import galena.hats.storage.ServerConfigStorage;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class FabricServerEntrypoint implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> {
            HatsCommand.registerServer(dispatcher);
        });

        ServerPlayConnectionEvents.JOIN.register((listener, sender, server) -> {
            ServerConfigStorage.notifyCached(listener.getPlayer());
        });

        FabricNetwork.registerCodecs();
        FabricNetwork.registerServerHandler();
    }

}
