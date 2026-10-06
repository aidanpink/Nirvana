package galena.nirvana.world;
import com.mojang.serialization.MapCodec;
import galena.nirvana.index.NirvanaTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/** Preserve the original uniform choice from the live burlap tag. */
public final class RandomBurlapFunction implements LootItemFunction {
 public static final MapCodec<RandomBurlapFunction> CODEC=MapCodec.unit(new RandomBurlapFunction());
 public MapCodec<RandomBurlapFunction> codec(){return CODEC;}
 public ItemStack apply(ItemStack stack,LootContext context){
  return context.getLevel().registryAccess().lookupOrThrow(Registries.ITEM).get(NirvanaTags.BURLAP)
   .flatMap(tag->tag.getRandomElement(context.getRandom())).map(item->new ItemStack(item,stack.getCount())).orElse(ItemStack.EMPTY);
 }
}
