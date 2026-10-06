package galena.nirvana.registration;

import galena.nirvana.NirvanaConstants;
import java.util.*;
import java.util.function.*;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.registry.CompostableRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.particles.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

/** Runtime registration through vanilla/Fabric; assets live in the resource pack. */
public final class NativeRegistry {
    public <T extends Item> ItemBuilder<T> item(String name, Function<Item.Properties,T> factory) { return new ItemBuilder<>(name,factory); }
    public <T extends Block> BlockBuilder<T> block(String name, Function<BlockBehaviour.Properties,T> factory) { return new BlockBuilder<>(name,factory); }
    public <T extends Entity> EntityBuilder<T> entity(String name, EntityType.EntityFactory<T> factory, MobCategory category) { return new EntityBuilder<>(name,factory,category); }
    public <B,T extends B> GenericBuilder<B,T> generic(String name, ResourceKey<? extends Registry<B>> key, Supplier<T> factory) { return new GenericBuilder<>(name,key,factory); }
    public GenericBuilder<SoundEvent,SoundEvent> sound(String name) { return generic(name,Registries.SOUND_EVENT,()->SoundEvent.createVariableRangeEvent(id(name))); }
    public GenericBuilder<ParticleType<?>,SimpleParticleType> particle(String name) { return generic(name,Registries.PARTICLE_TYPE,()->FabricParticleTypes.simple()); }
    public <T extends BlockEntity> BlockEntityBuilder<T> blockEntity(String name, BlockEntityFactory<T> factory) { return new BlockEntityBuilder<>(name,factory); }
    private static Identifier id(String name) { return NirvanaConstants.createId(name); }

    public static class ItemBuilder<T extends Item> {
        final String name; final Function<Item.Properties,T> factory;
        Item.Properties props=new Item.Properties();
        final List<BiConsumer<ItemEntry<T>,CreativeModeTab.Output>> fillers=new ArrayList<>();
        final List<ResourceKey<CreativeModeTab>> tabs=new ArrayList<>();
        final List<Consumer<T>> listeners=new ArrayList<>(); float compost=-1;
        ItemBuilder(String name,Function<Item.Properties,T> factory){this.name=name;this.factory=factory;}
        public ItemBuilder<T> properties(UnaryOperator<Item.Properties> apply){props=apply.apply(props);return this;}
        public ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab){return tab(tab,(entry,output)->output.accept(entry));}
        public ItemBuilder<T> tab(ResourceKey<CreativeModeTab> tab,BiConsumer<ItemEntry<T>,CreativeModeTab.Output> fill){tabs.add(tab);fillers.add(fill);return this;}
        public ItemBuilder<T> compostable(float chance){compost=chance;return this;}
        public ItemBuilder<T> onRegister(Consumer<T> callback){listeners.add(callback);return this;}
        public ItemEntry<T> register(){
            var key=ResourceKey.create(Registries.ITEM,id(name));
            T item=factory.apply(props.setId(key));
            Registry.register(BuiltInRegistries.ITEM,key,item);
            var entry=new ItemEntry<T>(BuiltInRegistries.ITEM.wrapAsHolder(item),item);
            if(compost>=0)CompostableRegistry.INSTANCE.add(item,compost);
            for(int i=0;i<tabs.size();i++){var fill=fillers.get(i);CreativeModeTabEvents.modifyOutputEvent(tabs.get(i)).register(output->fill.accept(entry,output));}
            listeners.forEach(callback->callback.accept(item));
            return entry;
        }
    }
    public static class BlockBuilder<T extends Block> {
        final String name; final Function<BlockBehaviour.Properties,T> factory;
        BlockBehaviour.Properties props=BlockBehaviour.Properties.of();
        boolean item; ResourceKey<CreativeModeTab> tab; float compost=-1;
        BlockBuilder(String name,Function<BlockBehaviour.Properties,T> factory){this.name=name;this.factory=factory;}
        public BlockBuilder<T> initialProperties(Supplier<? extends Block> base){props=BlockBehaviour.Properties.ofFullCopy(base.get());return this;}
        public BlockBuilder<T> properties(UnaryOperator<BlockBehaviour.Properties> apply){props=apply.apply(props);return this;}
        public BlockBuilder<T> item(){item=true;return this;}
        public BlockBuilder<T> tab(ResourceKey<CreativeModeTab> tab){this.tab=tab;return this;}
        public BlockBuilder<T> compostable(float chance){compost=chance;return this;}
        public BlockBuilder<T> build(){return this;}
        public BlockEntry<T> register(){
            var key=ResourceKey.create(Registries.BLOCK,id(name));
            T block=factory.apply(props.setId(key));
            Registry.register(BuiltInRegistries.BLOCK,key,block);
            if(item){var builder=new ItemBuilder<>(name,p->new BlockItem(block,p.useBlockDescriptionPrefix()));if(tab!=null)builder.tab(tab);if(compost>=0)builder.compostable(compost);builder.register();}
            return new BlockEntry<>(BuiltInRegistries.BLOCK.wrapAsHolder(block),block);
        }
    }
    public static class GenericBuilder<B,T extends B> {
        final String name; final ResourceKey<? extends Registry<B>> key; final Supplier<T> factory;
        GenericBuilder(String name,ResourceKey<? extends Registry<B>> key,Supplier<T> factory){this.name=name;this.key=key;this.factory=factory;}
        @SuppressWarnings("unchecked") public RegistryEntry<B,T> register(){
            Registry<B> registry=(Registry<B>)BuiltInRegistries.REGISTRY.getValue(key.identifier());
            T value=factory.get();Registry.register(registry,id(name),value);
            return new RegistryEntry<>(registry.wrapAsHolder(value),value);
        }
    }
    public static class EntityBuilder<T extends Entity> {
        final String name; final EntityType.Builder<T> builder; Supplier<AttributeSupplier.Builder> attributes;
        EntityBuilder(String name,EntityType.EntityFactory<T> factory,MobCategory category){this.name=name;builder=EntityType.Builder.of(factory,category);}
        public EntityBuilder<T> properties(UnaryOperator<EntityType.Builder<T>> apply){apply.apply(builder);return this;}
        public EntityBuilder<T> attributes(Supplier<AttributeSupplier.Builder> attributes){this.attributes=attributes;return this;}
        @SuppressWarnings("unchecked") public EntityEntry<T> register(){
            var key=ResourceKey.create(Registries.ENTITY_TYPE,id(name));var type=builder.build(key);
            Registry.register(BuiltInRegistries.ENTITY_TYPE,key,type);
            if(attributes!=null)FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>)type,attributes.get());
            return new EntityEntry<>(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type),type);
        }
    }
    @FunctionalInterface public interface BlockEntityFactory<T extends BlockEntity>{T create(BlockEntityType<T> type,BlockPos pos,BlockState state);}
    public static class BlockEntityBuilder<T extends BlockEntity>{
        final String name;final BlockEntityFactory<T> factory;Block[] blocks;
        BlockEntityBuilder(String name,BlockEntityFactory<T> factory){this.name=name;this.factory=factory;}
        public BlockEntityBuilder<T> validBlocks(BlockEntry<?>... entries){blocks=Arrays.stream(entries).map(BlockEntry::get).toArray(Block[]::new);return this;}
        @SuppressWarnings("unchecked") public BlockEntityEntry<T> register(){
            BlockEntityType<T>[] reference=new BlockEntityType[1];
            var type=FabricBlockEntityTypeBuilder.create((BlockPos pos,BlockState state)->factory.create(reference[0],pos,state),blocks).build();reference[0]=type;
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE,id(name),type);
            return new BlockEntityEntry<>(BuiltInRegistries.BLOCK_ENTITY_TYPE.wrapAsHolder(type),type);
        }
    }
}
