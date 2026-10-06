package galena.nirvana.fabric.services;
import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
public class FabricPlatformHelper implements IPlatformHelper {
    private final NativeRegistry registry = new NativeRegistry();
    public NativeRegistry getRegistrate(){return registry;}
    public Ingredient createNBTIngredient(ItemStack stack){return DefaultCustomIngredients.components(stack);}
    public boolean isFakePlayer(LivingEntity entity){return entity instanceof FakePlayer;}
    public boolean createLoaded(){return FabricLoader.getInstance().isModLoaded("create");}
}
