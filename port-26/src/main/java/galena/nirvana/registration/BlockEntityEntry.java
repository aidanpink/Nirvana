package galena.nirvana.registration;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
public class BlockEntityEntry<T extends BlockEntity> extends RegistryEntry<BlockEntityType<?>,BlockEntityType<T>> {
    public T create(net.minecraft.core.BlockPos pos, net.minecraft.world.level.block.state.BlockState state) { return get().create(pos,state); }
    public BlockEntityEntry(Holder<BlockEntityType<?>> holder,BlockEntityType<T> value) { super(holder,value); }
}
