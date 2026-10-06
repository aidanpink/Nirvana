package galena.nirvana.fabric.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import galena.nirvana.index.NirvanaTags;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Chicken.class)
public class ChickenMixin {

    @ModifyReturnValue(
            method = "isFood",
            at = @At("RETURN")
    )
    private boolean isFoodTag(boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || stack.is(NirvanaTags.CHICKEN_FOOD);
    }

}
