package galena.nirvana.world.entity;

import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.world.THCCloud;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MinecartThc extends MinecartTNT {

    public MinecartThc(EntityType<? extends MinecartTNT> type, Level level) {
        super(type, level);
    }

    @Override
    protected void explode(@Nullable DamageSource damageSource, double d) {
        if(level().isClientSide()) return;
        THCCloud.spawnCloud(level(), position(), 1.5F, 15, 60);
        discard();
    }

    @Override
    public BlockState getDisplayBlockState() {
        return NirvanaBlocks.THC.getDefaultState();
    }

}
