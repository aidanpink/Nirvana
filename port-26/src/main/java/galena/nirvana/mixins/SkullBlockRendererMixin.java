package galena.nirvana.mixins;
import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.world.block.renderer.ReeferHeadRenderer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.object.skull.SkullModel;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(SkullBlockRenderer.class)
public class SkullBlockRendererMixin {
 @Inject(method="getSkullRenderType",at=@At("HEAD"),cancellable=true)
 private static void renderType(SkullBlock.Type type,Identifier texture,CallbackInfoReturnable<RenderType> cir){if(type==NirvanaBlocks.REEFER_SKULL_TYPE)cir.setReturnValue(RenderTypes.entityCutoutZOffset(ReeferHeadRenderer.TEXTURE));}
 @Inject(method="createModel",at=@At("HEAD"),cancellable=true)
 private static void model(EntityModelSet set,SkullBlock.Type type,CallbackInfoReturnable<SkullModelBase> cir){if(type==NirvanaBlocks.REEFER_SKULL_TYPE)cir.setReturnValue(new SkullModel(set.bakeLayer(ReeferHeadRenderer.LAYER)));}
}
