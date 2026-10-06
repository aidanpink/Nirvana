package galena.nirvana.index;

import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.registration.EntityEntry;
import galena.nirvana.platform.Services;
import galena.nirvana.world.entity.MinecartThc;
import galena.nirvana.world.entity.PrimedThc;
import galena.nirvana.world.entity.Reefer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;

public class NirvanaEntities {

    private static final NativeRegistry REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final EntityEntry<Reefer> REEFER = REGISTRATE
            .entity("reefer", Reefer::new, MobCategory.MONSTER)
            .attributes(Reefer::createAttributes)
            .properties(builder -> builder
                    .sized(0.6F, 1.7F)
                    .clientTrackingRange(8)
            )

            .register();

    public static final EntityEntry<? extends PrimedTnt> THC = REGISTRATE
            .<PrimedThc>entity("thc", PrimedThc::new, MobCategory.MISC)

            .properties(builder -> builder
                    .fireImmune()
                    .sized(0.98F, 0.98F)
                    .clientTrackingRange(10)
                    .updateInterval(10)
            )
            .register();

    public static final EntityEntry<? extends MinecartTNT> THC_MINECART = REGISTRATE
            .<MinecartThc>entity("thc_minecart", MinecartThc::new, MobCategory.MISC)

            .properties(builder -> builder
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
            )

            .register();

    public static void register() {
        // loads this class
    }

}
