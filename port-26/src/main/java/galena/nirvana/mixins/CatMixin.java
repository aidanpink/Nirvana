package galena.nirvana.mixins;
import galena.nirvana.NirvanaConstants;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.client.renderer.entity.state.CatRenderState;
import net.minecraft.world.entity.animal.feline.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(CatRenderer.class)
public class CatMixin {
 @Inject(method="extractRenderState(Lnet/minecraft/world/entity/animal/feline/Cat;Lnet/minecraft/client/renderer/entity/state/CatRenderState;F)V",at=@At("TAIL"))
 private void nirvana$texture(Cat cat,CatRenderState state,float partialTick,CallbackInfo ci){
  var name=cat.getCustomName();
  if(name!=null && "sprigatito".equals(ChatFormatting.stripFormatting(name.getString()).toLowerCase(Locale.ROOT)))state.texture=NirvanaConstants.createId("textures/entity/cat/sprigatito.png");
 }
}
