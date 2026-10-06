package galena.hats.fabric;

import galena.hats.HatsApi;
import galena.hats.HatsCommand;
import galena.hats.client.HatLayer;
import galena.hats.fabric.services.FabricNetwork;
import galena.hats.storage.ClientConfigStorage;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLayerRegistry.registerModelLayer(HatLayer.LAYER_LOCATION, HatLayer::createLayerDefinition);

        LivingEntityRenderLayerRegistrationCallback.EVENT.register((_, renderer, helper, context) -> {
            var layer = context.bakeLayer(HatLayer.LAYER_LOCATION);

            if (renderer instanceof HumanoidMobRenderer<?, ?, ?> humanoid) {
                helper.register(new HatLayer<>(humanoid, layer));
            }

            if (renderer instanceof AvatarRenderer<?> humanoid) {
                helper.register(new HatLayer<>(humanoid, layer));
            }
        });

        FabricNetwork.registerCodecs();
        FabricNetwork.registerClientHandler();
        FabricNetwork.registerServerHandler();

        ClientPlayConnectionEvents.JOIN.register((_, _, _) -> {
            ClientConfigStorage.broadcastConfig();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            HatsCommand.registerClient(dispatcher);
        });

        ClientConfigStorage.getUUID().ifPresent(HatsApi::getSupporterData);
    }

}
