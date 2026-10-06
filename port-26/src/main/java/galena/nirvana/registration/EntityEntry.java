package galena.nirvana.registration;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
public class EntityEntry<T extends Entity> extends RegistryEntry<EntityType<?>,EntityType<T>> {
    public T create(net.minecraft.world.level.Level level) { return get().create(level,net.minecraft.world.entity.EntitySpawnReason.CONVERSION); }
    public EntityEntry(Holder<EntityType<?>> holder,EntityType<T> value) { super(holder,value); }
}
