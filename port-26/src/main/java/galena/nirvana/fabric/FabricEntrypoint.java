package galena.nirvana.fabric;

import static galena.nirvana.NirvanaConstants.MOD_ID;

import galena.nirvana.NirvanaCommon;
import galena.nirvana.NirvanaConstants;
import galena.nirvana.fabric.services.FabricConfigs;
import galena.nirvana.index.NirvanaBlocks;
import galena.nirvana.index.NirvanaItems;
import galena.nirvana.index.NirvanaTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

public class FabricEntrypoint implements ModInitializer {

    private static final ResourceKey<PlacedFeature> WILD_HEMP_FEATURE = ResourceKey.create(Registries.PLACED_FEATURE, NirvanaConstants.createId("patch_wild_hemp"));

    @Override
    public void onInitialize() {
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.FEATURE, NirvanaConstants.createId("wild_hemp_patch"), new galena.nirvana.world.WildHempPatchFeature());
        net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.LOOT_FUNCTION_TYPE,NirvanaConstants.createId("random_burlap"),galena.nirvana.world.RandomBurlapFunction.CODEC);
        FabricConfigs.register();
        NirvanaCommon.init();

        modifyLootTables();

        BiomeModifications.addFeature(BiomeSelectors.tag(NirvanaTags.GENERATES_WILD_HEMP), GenerationStep.Decoration.VEGETAL_DECORATION, WILD_HEMP_FEATURE);

    }

    private static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, table, source, lookup) -> {
            if (!source.isBuiltin()) return;
            if (BuiltInLootTables.CHARGED_CREEPER.equals(key)) {
                table.withPool(LootPool.lootPool().add(LootItem.lootTableItem(NirvanaItems.REEFER_HEAD))
                    .when(net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition.hasProperties(
                        net.minecraft.world.level.storage.loot.LootContext.EntityTarget.THIS,
                        net.minecraft.advancements.criterion.EntityPredicate.Builder.entity()
                            .of(lookup.lookupOrThrow(Registries.ENTITY_TYPE), galena.nirvana.index.NirvanaEntities.REEFER.get()))));
            }
            if (BuiltInLootTables.SNIFFER_DIGGING.equals(key)) {
                table.modifyPools(it -> {
                    it.add(LootItem.lootTableItem(NirvanaBlocks.BLISS_BLOOM));
                });
            } else if (BuiltInLootTables.IGLOO_CHEST.equals(key)) {
                table.withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(NirvanaItems.EMPTY_PIPE))
                        .when(LootItemRandomChanceCondition.randomChance(0.1F))
                );
            } else if (BuiltInLootTables.WOODLAND_MANSION.equals(key)) {
                table.withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(NirvanaItems.EMPTY_PIPE))
                        .when(LootItemRandomChanceCondition.randomChance(0.33F))
                );
            }
        });
    }

}
