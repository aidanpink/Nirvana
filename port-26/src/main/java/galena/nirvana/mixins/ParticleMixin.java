package galena.nirvana.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.world.particle.ICollidingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Particle.class)
public class ParticleMixin {

    @Inject(
            method = "move(DDD)V",
            at = @At(
                    value = "FIELD",
                    opcode = 180,
                    target = "Lnet/minecraft/world/phys/Vec3;z:D"
            )
    )
    public void onCollide(double xd, double yd, double zd, CallbackInfo ci, @Local Vec3 actual) {
        if (!(this instanceof ICollidingParticle particle)) return;
        if (
                Math.abs(xd - actual.x) >= 1.0E-5F
                        || Math.abs(yd - actual.y) >= 1.0E-5F
                        || Math.abs(zd - actual.z) >= 1.0E-5F
        ) {
            particle.collide();
        }
    }

}
