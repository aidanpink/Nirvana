package galena.nirvana.registration;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

/** A typed view of a vanilla registry holder, with no secondary registry. */
public class RegistryEntry<B, T extends B> implements Holder<B>, Supplier<T> {
    private final Holder<B> holder;
    private final T value;
    public RegistryEntry(Holder<B> holder, T value) { this.holder = holder; this.value = value; }
    public T get() { return value; }
    public B value() { return value; }
    public Identifier getId() { return holder.unwrapKey().orElseThrow().identifier(); }
    public boolean isBound() { return holder.isBound(); }
    public boolean areComponentsBound() { return holder.areComponentsBound(); }
    public boolean is(Identifier id) { return holder.is(id); }
    public boolean is(ResourceKey<B> key) { return holder.is(key); }
    public boolean is(Predicate<ResourceKey<B>> test) { return holder.is(test); }
    public boolean is(TagKey<B> tag) { return holder.is(tag); }
    public boolean is(Holder<B> other) { return holder.is(other); }
    public Stream<TagKey<B>> tags() { return holder.tags(); }
    public DataComponentMap components() { return holder.components(); }
    public Either<ResourceKey<B>, B> unwrap() { return holder.unwrap(); }
    public Optional<ResourceKey<B>> unwrapKey() { return holder.unwrapKey(); }
    public Kind kind() { return holder.kind(); }
    public boolean canSerializeIn(HolderOwner<B> owner) { return holder.canSerializeIn(owner); }
}
