package galena.nirvana.mixins;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import galena.nirvana.index.NirvanaItems;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
/** Feed the joint's head model into vanilla's held-to-eye render state. */
@Mixin(AvatarRenderer.class)
public class PlayerItemInHandLayerMixin {
 @WrapOperation(method="extractRenderState",at=@At(value="INVOKE",target="Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"))
 private boolean nirvana$headItem(ItemStack stack,Object item,Operation<Boolean> original){return original.call(stack,item) || item==Items.SPYGLASS && NirvanaItems.JOINT.isIn(stack);}
}
