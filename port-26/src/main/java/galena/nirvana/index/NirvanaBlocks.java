package galena.nirvana.index;

import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.registration.BlockEntityEntry;
import galena.nirvana.registration.BlockEntry;
import galena.nirvana.compat.DyeColors;
import galena.nirvana.platform.Services;
import galena.nirvana.world.block.CrateBlock;
import galena.nirvana.world.block.HempCropBlock;
import galena.nirvana.world.block.ModdedSkullBlock;
import galena.nirvana.world.block.ModdedWallSkullBlock;
import galena.nirvana.world.block.ThcBlock;
import galena.nirvana.world.block.WildHempBlock;
import galena.nirvana.world.block.entity.ModdedSkullBlockEntity;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.GlazedTerracottaBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;

public class NirvanaBlocks {

    private static final NativeRegistry REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final BlockEntry<HempCropBlock> HEMP = REGISTRATE
        .block("hemp", HempCropBlock::new)
        .initialProperties(() -> Blocks.WHEAT)
        .properties(properties -> properties.mapColor(MapColor.PLANT))

        .register();

    public static final BlockEntry<CrateBlock> HEMP_CRATE = REGISTRATE
        .block("hemp_crate", CrateBlock::new)
        .initialProperties(() -> Blocks.BARREL)

        .item()
        .tab(CreativeModeTabs.BUILDING_BLOCKS)
        .build()
        .register();

    public static final BlockEntry<CrateBlock> WEED_CRATE = REGISTRATE
        .block("weed_crate", CrateBlock::new)
        .initialProperties(() -> Blocks.BARREL)

        .item()
        .tab(CreativeModeTabs.BUILDING_BLOCKS)
        .build()
        .register();

    public static final BlockEntry<DoublePlantBlock> BLISS_BLOOM = REGISTRATE
        .block("bliss_bloom", DoublePlantBlock::new)
        .initialProperties(() -> Blocks.ROSE_BUSH)

        .item()
        .tab(CreativeModeTabs.NATURAL_BLOCKS)

        .compostable(0.85F)
        .build()
        .register();

    public static final BlockEntry<? extends BushBlock> WILD_HEMP = REGISTRATE
        .block("wild_hemp", WildHempBlock::new)
        .initialProperties(() -> Blocks.FERN)

        .item()
        .tab(CreativeModeTabs.NATURAL_BLOCKS)

        .compostable(0.65F)
        .build()
        .register();

    public static final BlockEntry<ThcBlock> THC = REGISTRATE
        .block("thc", ThcBlock::new)

        .initialProperties(() -> Blocks.TNT)

        .item()
        .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)

        .build()
        .register();

    public static final SkullBlock.Type REEFER_SKULL_TYPE = () -> "nirvana:reefer";
    static { SkullBlock.Type.TYPES.put(REEFER_SKULL_TYPE.getSerializedName(), REEFER_SKULL_TYPE); }

    public static final BlockEntry<? extends SkullBlock> REEFER_HEAD = REGISTRATE
        .block("reefer_head", it -> new ModdedSkullBlock(REEFER_SKULL_TYPE, it))

        .initialProperties(() -> Blocks.CREEPER_HEAD)

        .register();

    public static final BlockEntry<? extends WallSkullBlock> REEFER_WALL_HEAD = REGISTRATE
        .block("reefer_wall_head", it -> new ModdedWallSkullBlock(REEFER_SKULL_TYPE, it))

        .initialProperties(() -> Blocks.CREEPER_WALL_HEAD)

        .register();

    public static final BlockEntityEntry<SkullBlockEntity> MODDED_SKULL = REGISTRATE
        .<SkullBlockEntity>blockEntity("skull", ($, pos, state) -> new ModdedSkullBlockEntity(pos, state))

        .validBlocks(REEFER_HEAD, REEFER_WALL_HEAD)
        .register();

    public static final BlockEntry<FlowerPotBlock> POTTED_WILD_HEMP = REGISTRATE
        .block("potted_wild_hemp", it -> new FlowerPotBlock(WILD_HEMP.get(), it))

        .initialProperties(() -> Blocks.POTTED_CACTUS)

        .register();

    private static BlockEntry<GlazedTerracottaBlock> createHempBurlap(@Nullable DyeColor color) {
        var name = "hemp_burlap";
        var translation = "Burlap";
        return REGISTRATE
            .block(color == null ? name : color + "_" + name, GlazedTerracottaBlock::new)

            .initialProperties(() -> Blocks.HAY_BLOCK)
            .properties(it -> it.sound(SoundType.WOOL))
            .properties(it -> color == null ? it.mapColor(MapColor.WOOL) : it.mapColor(color))

            .item()
            .tab(CreativeModeTabs.BUILDING_BLOCKS)

            .build()
            .register();
    }

    public static final BlockEntry<GlazedTerracottaBlock> HEMP_BURLAP = createHempBurlap(null);
    public static final Map<DyeColor, BlockEntry<GlazedTerracottaBlock>> COLORED_HEMP_BURLAP = colored(NirvanaBlocks::createHempBurlap);

    private static BlockEntry<RotatedPillarBlock> createWovenBurlap(@Nullable DyeColor color) {
        var name = "woven_burlap";
        return REGISTRATE
            .block(color == null ? name : color + "_" + name, RotatedPillarBlock::new)
            .initialProperties(() -> Blocks.HAY_BLOCK)
            .properties(it -> it.sound(SoundType.WOOL))
            .properties(it -> color == null ? it.mapColor(MapColor.WOOL) : it.mapColor(color))

            .item()
            .tab(CreativeModeTabs.BUILDING_BLOCKS)

            .build()
            .register();
    }

    public static final BlockEntry<RotatedPillarBlock> WOVEN_BURLAP = createWovenBurlap(null);
    public static final Map<DyeColor, BlockEntry<RotatedPillarBlock>> COLORED_WOVEN_BURLAP = colored(NirvanaBlocks::createWovenBurlap);

    private static <T> Map<DyeColor, T> colored(Function<DyeColor, T> mapper) {
        return DyeColors.supported().collect(Collectors.toMap(it -> it, mapper));
    }

    public static void register() {
        // SkullBlockEntity's vanilla constructor validates against SKULL before our
        // subclass supplies its own type, so these blocks must also be accepted there.
        net.minecraft.world.level.block.entity.BlockEntityType.SKULL.addValidBlock(REEFER_HEAD.get());
        net.minecraft.world.level.block.entity.BlockEntityType.SKULL.addValidBlock(REEFER_WALL_HEAD.get());
    }

}
