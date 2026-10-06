package galena.nirvana.mixins;
import galena.nirvana.client.PeaceShader;
import galena.nirvana.world.effects.PeaceEffect;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
 @Inject(method="render",at=@At(value="INVOKE",target="Lnet/minecraft/client/renderer/LevelRenderer;doEntityOutline()V",shift=At.Shift.AFTER))
 private void renderPeaceShader(DeltaTracker delta,boolean advance,CallbackInfo ci){
  var access=(GameRendererAccessor)this;var minecraft=access.getMinecraft();
  if(access.getPostEffectId()!=null || !PeaceEffect.shouldRenderShader(minecraft.player))return;
  var chain=PeaceShader.load(minecraft);
  if(chain!=null)chain.process(minecraft.getMainRenderTarget(),access.getResourcePool());
 }
}
