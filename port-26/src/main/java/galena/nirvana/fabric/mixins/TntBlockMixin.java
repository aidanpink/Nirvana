package galena.nirvana.fabric.mixins;
import galena.nirvana.world.block.ICustomTntBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(TntBlock.class)
public class TntBlockMixin {
 @Inject(method="prime(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z",at=@At("HEAD"),cancellable=true)
 private static void nirvana$prime(Level level,BlockPos pos,LivingEntity source,CallbackInfoReturnable<Boolean> cir){
  var state=level.getBlockState(pos);
  if(state.getBlock() instanceof ICustomTntBlock tnt){
   boolean enabled=level instanceof net.minecraft.server.level.ServerLevel server && server.getGameRules().get(net.minecraft.world.level.gamerules.GameRules.TNT_EXPLODES);
   if(enabled)tnt.onCaughtFire(state,level,pos,null,source);
   cir.setReturnValue(enabled);
  }
 }
}
