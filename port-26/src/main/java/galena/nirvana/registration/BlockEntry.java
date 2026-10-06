package galena.nirvana.registration;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
public class BlockEntry<T extends Block> extends RegistryEntry<Block,T> implements ItemLike {
    public BlockEntry(Holder<Block> holder,T value) { super(holder,value); }
    public Item asItem() { return get().asItem(); }
    public boolean has(BlockState state) { return state.is(get()); }
    public BlockState getDefaultState() { return get().defaultBlockState(); }
}
