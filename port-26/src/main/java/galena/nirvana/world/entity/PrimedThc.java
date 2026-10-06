package galena.nirvana.world.entity;

import galena.nirvana.index.NirvanaEntities;
import galena.nirvana.world.THCCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class PrimedThc extends PrimedTnt {

    private final @Nullable LivingEntity owner;

    public PrimedThc(EntityType<? extends PrimedThc> type, Level level) {
        super(type, level);
        this.owner = null;
    }

    public PrimedThc(Level level, double d, double e, double f, @Nullable LivingEntity igniter) {
        super(NirvanaEntities.THC.get(), level);
        this.setPos(d, e, f);
        double g = level.getRandom().nextDouble() * Math.PI * 2F;
        setDeltaMovement(-Math.sin(g) * 0.02D, 0.2F, -Math.cos(g) * 0.02D);
        setFuse(80);
        this.xo = d;
        this.yo = e;
        this.zo = f;
        this.owner = igniter;
    }

    @Override
    public @Nullable LivingEntity getOwner() {
        return owner;
    }

    protected void explode() {
        THCCloud.spawnCloud(level(), position(), 1.5F, 15, 60);
    }

}
