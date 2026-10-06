package galena.nirvana.platform.services;
import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.world.item.DeerStalkerItem;
import java.util.function.Supplier;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
public interface IPlatformHelper {
    NativeRegistry getRegistrate();
    default DeerStalkerItem createDeerstalkerItem(Item.Properties properties){return new DeerStalkerItem(properties);}
    default Item createSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type,int primary,int secondary,Item.Properties properties){return new SpawnEggItem(properties.spawnEgg(type.get()));}
    Ingredient createNBTIngredient(ItemStack stack);
    boolean isFakePlayer(LivingEntity entity);
    boolean createLoaded();
}
