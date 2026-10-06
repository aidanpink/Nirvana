package galena.nirvana.index;

import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.registration.RegistryEntry;
import galena.nirvana.platform.Services;
import java.util.function.Function;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class NirvanaParticles {

    private static final NativeRegistry REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final RegistryEntry<ParticleType<?>, SimpleParticleType> SMOKE_RING = REGISTRATE
            .particle("smoke_ring")

            .register();

    public static final RegistryEntry<ParticleType<?>, SimpleParticleType> HERBAL_SALVE = REGISTRATE
            .particle("herbal_salve")

            .register();

    public static final RegistryEntry<ParticleType<?>, SimpleParticleType> THC_SMOKE = REGISTRATE
            .particle("thc_smoke")

            .register();

    public static void spawnRing(Level level, LivingEntity user) {
        if (level instanceof ServerLevel serverLevel) {
            var pos = user.getEyePosition();
            var motion = user.getLookAngle();
            serverLevel.sendParticles(SMOKE_RING.get(), pos.x, pos.y, pos.z, 0, motion.x, motion.y, motion.z, 0.1);
        }
    }

    public static void register() {
        // loads this class
    }

    @FunctionalInterface
    public interface ParticleRegister {
        <T extends ParticleOptions> void register(ParticleType<T> options, Function<SpriteSet, ParticleProvider<T>> factory);
    }

}
