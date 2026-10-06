package galena.nirvana.index;

import com.mojang.datafixers.util.Pair;
import galena.nirvana.registration.NativeRegistry;

import galena.nirvana.registration.ItemEntry;
import java.util.function.BiConsumer;
import galena.nirvana.DistinctBy;
import galena.nirvana.platform.Services;
import galena.nirvana.world.item.BongItem;
import galena.nirvana.world.item.CustomMinecartItem;
import galena.nirvana.world.item.FilledPipeItem;
import galena.nirvana.world.item.HerbalSalveItem;
import galena.nirvana.world.item.JointItem;
import galena.nirvana.world.item.PotionBongItem;
import galena.nirvana.world.item.SuspiciousPipeItem;
import java.util.function.IntSupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.equipment.ArmorType;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

public class NirvanaItems {

    private static final NativeRegistry REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final ItemEntry<Item> HEMP = REGISTRATE
        .item("hemp", Item::new)
        .tab(CreativeModeTabs.INGREDIENTS)
        .compostable(0.65F)
        .register();

    public static final ItemEntry<BlockItem> HEMP_SEEDS = REGISTRATE
        .item("hemp_seeds", p -> new BlockItem(NirvanaBlocks.HEMP.get(), p))

        .tab(CreativeModeTabs.NATURAL_BLOCKS)

        .compostable(0.3F)
        .register();

    public static final ItemEntry<Item> WEED = REGISTRATE
        .item("weed", Item::new)

        .tab(CreativeModeTabs.FOOD_AND_DRINKS)

        .register();

    private static FoodProperties createBrownieFood() {
        return new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .build();
    }

    public static final ItemEntry<Item> WEED_BROWNIE = REGISTRATE
        .item("weed_brownie", Item::new)
        .properties(it -> it.food(createBrownieFood(), net.minecraft.world.item.component.Consumable.builder().onConsume(new net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect(new MobEffectInstance(NirvanaEffects.PEACE, 20 * 40, 0), 1.0F)).build()))
        .tab(CreativeModeTabs.FOOD_AND_DRINKS)

        .register();

    public static final ItemEntry<BongItem> BONG = REGISTRATE
        .item("bong", BongItem::new)
        .tab(CreativeModeTabs.FOOD_AND_DRINKS)
        .properties(it -> it.durability(Services.CONFIG.common().getBongHits()))
        .properties(it -> it.craftRemainder(Items.GLASS_BOTTLE))
        .register();

    private static <T extends Item> BiConsumer<ItemEntry<T>, net.minecraft.world.item.CreativeModeTab.Output> addPotionStacks() {
        return (context, modifier) -> BuiltInRegistries.POTION.listElements()
            .filter(it -> !it.is(Potions.WATER))
            .map(it -> {
                var stack = new ItemStack(context.get());
                stack.set(DataComponents.POTION_CONTENTS, new PotionContents(it));
                return stack;
            })
            .forEach(modifier::accept);
    }

    private static <T extends Item> BiConsumer<ItemEntry<T>, net.minecraft.world.item.CreativeModeTab.Output> addSuspiciousStack(IntSupplier factor) {
        return (context, modifier) -> NirvanaRecipeTypes.getSuspiciousVariants(context.get(), factor.getAsInt())
            .map(Pair::getSecond)
            .filter(DistinctBy.of(it -> it.get(DataComponents.SUSPICIOUS_STEW_EFFECTS)))
            .forEach(modifier::accept);
    }

    public static final ItemEntry<PotionBongItem> POTION_BONG = REGISTRATE
        .item("potion_bong", PotionBongItem::new)

        .tab(CreativeModeTabs.FOOD_AND_DRINKS, NirvanaItems.addPotionStacks())

        .properties(it -> it.durability(Services.CONFIG.common().getBongHits()))
        .properties(it -> it.craftRemainder(Items.GLASS_BOTTLE))

        .register();

    public static final ItemEntry<JointItem> JOINT = REGISTRATE
        .item("joint", JointItem::new)
        .properties(it -> it.durability(Services.CONFIG.common().getJointHits()))

        .tab(CreativeModeTabs.FOOD_AND_DRINKS)

        .register();

    public static final ItemEntry<HerbalSalveItem> HERBAL_SALVE = REGISTRATE
        .item("herbal_salve", HerbalSalveItem::new)
        .properties(it -> it.stacksTo(1))
        .properties(it -> it.craftRemainder(Items.BOWL))
        .tab(CreativeModeTabs.FOOD_AND_DRINKS, NirvanaItems.addSuspiciousStack(() -> Services.CONFIG.common().herbalSalveFactor()))
        .register();

    public static final ItemEntry<? extends Item> DISC_JAM = REGISTRATE
        .item("music_disc_jam", Item::new)
        .properties(it -> it.stacksTo(1))
        .properties(it -> it.rarity(Rarity.RARE))
        .properties(it -> it.jukeboxPlayable(NirvanaSounds.JAM_KEY))
        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)

        .register();

    public static final ItemEntry<? extends Item> EMPTY_PIPE = REGISTRATE
        .item("old_pipe", Item::new)
        .properties(it -> it.stacksTo(1))
        .properties(it -> it.rarity(Rarity.UNCOMMON))

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
        .register();

    public static final ItemEntry<? extends Item> STUFFED_PIPE = REGISTRATE
        .item("stuffed_pipe", FilledPipeItem::new)
        .properties(it -> it.durability(Services.CONFIG.common().getPipeHits()))
        .properties(it -> it.rarity(Rarity.UNCOMMON))
        .properties(it -> it.craftRemainder(EMPTY_PIPE.asItem()))

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)

        .register();

    public static final ItemEntry<? extends Item> SUSPICIOUS_PIPE = REGISTRATE
        .item("suspicious_pipe", SuspiciousPipeItem::new)
        .properties(it -> it.durability(Services.CONFIG.common().getPipeHits()))
        .properties(it -> it.rarity(Rarity.UNCOMMON))
        .properties(it -> it.craftRemainder(EMPTY_PIPE.asItem()))

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES, NirvanaItems.addSuspiciousStack(() -> Services.CONFIG.common().suspiciousPipeFactor()))
        .register();

    public static final ItemEntry<? extends Item> REEFER_SPAWN_EGG = REGISTRATE
        .item("reefer_spawn_egg", it -> Services.PLATFORM.createSpawnEggItem(NirvanaEntities.REEFER, 0x619932, 0x2f4f15, it))
        //

        .tab(CreativeModeTabs.SPAWN_EGGS)
        .register();

    public static final ItemEntry<? extends Item> THC_MINECART = REGISTRATE
        .item("thc_minecart", it -> new CustomMinecartItem(it, NirvanaEntities.THC_MINECART))
        .properties(it -> it.stacksTo(1))

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
        .onRegister(CustomMinecartItem::registerDispenseBehaviour)
        .register();

    public static final ItemEntry<? extends Item> PEACE_BANNER_PATTERN = REGISTRATE
        .item("peace_banner_pattern", it -> new Item(it.delayedComponent(DataComponents.PROVIDES_BANNER_PATTERNS, context -> context.getOrThrow(NirvanaTags.PEACE_BANNER_PATTERN))))
        .properties(it -> it.stacksTo(1))
        .properties(it -> it.rarity(Rarity.UNCOMMON))

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
        .register();

    public static final ItemEntry<? extends Item> HEMP_CLOTH = REGISTRATE
        .item("hemp_cloth", Item::new)

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
        .register();

    public static final ItemEntry<? extends Item> DEERSTALKER = REGISTRATE
        .item("deerstalker", Services.PLATFORM::createDeerstalkerItem)
        .properties(it -> it.durability(ArmorType.HELMET.getDurability(5)))

        .tab(CreativeModeTabs.TOOLS_AND_UTILITIES)
        .register();

    public static final ItemEntry<? extends Item> REEFER_HEAD = REGISTRATE
        .item("reefer_head", it -> new StandingAndWallBlockItem(NirvanaBlocks.REEFER_HEAD.get(), NirvanaBlocks.REEFER_WALL_HEAD.get(), Direction.DOWN, it.equippableUnswappable(net.minecraft.world.entity.EquipmentSlot.HEAD)))
        .properties(it -> it.rarity(Rarity.UNCOMMON))

        .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)

        .register();

    public static void register() {
        // loads this class
    }

}
