package galena.nirvana.world.entity.renderer;
import galena.nirvana.index.NirvanaBlocks;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.TntRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.world.entity.item.PrimedTnt;
public class CustomTntRenderer extends TntRenderer {
 private final BlockModelResolver models;
 public CustomTntRenderer(EntityRendererProvider.Context context){super(context);models=context.getBlockModelResolver();}
 @Override public void extractRenderState(PrimedTnt entity,TntRenderState state,float partialTick){super.extractRenderState(entity,state,partialTick);models.update(state.blockState,NirvanaBlocks.THC.getDefaultState(),BLOCK_DISPLAY_CONTEXT);}
}
