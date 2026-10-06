package galena.nirvana.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.index.NirvanaEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @WrapOperation(method = "isInvulnerableToBase", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/Entity;invulnerable:Z", opcode = 180))
    public boolean canAttack(Entity instance, Operation<Boolean> original, @Local(argsOnly = true) DamageSource damageSource) {
        return (instance instanceof LivingEntity
                && damageSource.getEntity() instanceof LivingEntity attacker
                && NirvanaEffects.arePeaceful(instance, attacker)
        ) || original.call(instance);
    }

}
