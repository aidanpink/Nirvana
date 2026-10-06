package galena.nirvana.registration;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
public class ItemEntry<T extends Item> extends RegistryEntry<Item,T> implements ItemLike {
    public ItemEntry(Holder<Item> holder,T value) { super(holder,value); }
    public ItemStack asStack() { return new ItemStack(get()); }
    public Item asItem() { return get(); }
    public boolean isIn(ItemStack stack) { return stack.is(get()); }
}
