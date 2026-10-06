package galena.nirvana.fabric.client;

import galena.nirvana.world.block.renderer.ReeferHeadRenderer;
import galena.nirvana.world.entity.renderer.ReeferRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

public class FabricClientEntrypoint implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(galena.nirvana.index.NirvanaEntities.REEFER.get(), ReeferRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(galena.nirvana.index.NirvanaEntities.THC.get(), galena.nirvana.world.entity.renderer.CustomTntRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(galena.nirvana.index.NirvanaEntities.THC_MINECART.get(), net.minecraft.client.renderer.entity.TntMinecartRenderer::new);
        net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry.register(galena.nirvana.index.NirvanaBlocks.MODDED_SKULL.get(), net.minecraft.client.renderer.blockentity.SkullBlockRenderer::new);
        var particles=net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.getInstance();
        particles.register(galena.nirvana.index.NirvanaParticles.SMOKE_RING.get(), galena.nirvana.world.particle.SmokeRingParticle.Provider::new);
        particles.register(galena.nirvana.index.NirvanaParticles.THC_SMOKE.get(), galena.nirvana.world.particle.ThcSmokeParticle.Provider::new);
        particles.register(galena.nirvana.index.NirvanaParticles.HERBAL_SALVE.get(), net.minecraft.client.particle.SuspendedTownParticle.HappyVillagerProvider::new);
        ModelLayerRegistry.registerModelLayer(ReeferRenderer.LAYER, ReeferRenderer::createLayers);
        ModelLayerRegistry.registerModelLayer(ReeferHeadRenderer.LAYER, ReeferHeadRenderer::createLayers);
    }

}
