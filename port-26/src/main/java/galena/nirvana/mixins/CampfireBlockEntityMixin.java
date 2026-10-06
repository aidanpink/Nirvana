package galena.nirvana.mixins;

import galena.nirvana.index.NirvanaTags;
import galena.nirvana.world.THCCloud;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {

    @Unique
    private static final int RANGE = 2;

    @Inject(
            method = "particleTick",
            cancellable = true,
            at = @At("HEAD")
    )
    private static void clientTick(Level level, BlockPos pos, BlockState state, CampfireBlockEntity be, CallbackInfo ci) {
        if (!level.getBlockState(pos.below()).is(NirvanaTags.SMOKING_CRATES)) return;
        if (level.getRandom().nextFloat() < 0.1F) return;

        THCCloud.tickCloud(level, pos, RANGE);

        ci.cancel();
    }

}
