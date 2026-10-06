package galena.nirvana.index;

import com.mojang.datafixers.util.Pair;
import galena.nirvana.registration.NativeRegistry;
import galena.nirvana.registration.RegistryEntry;
import galena.nirvana.platform.Services;
import galena.nirvana.world.recipe.SuspiciousCraftingRecipe;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.FlowerBlock;

public class NirvanaRecipeTypes {

    private static final NativeRegistry REGISTRATE = Services.PLATFORM.getRegistrate();

    public static final RegistryEntry<RecipeSerializer<?>, RecipeSerializer<SuspiciousCraftingRecipe>> SUSPICIOUS_RECIPE_SERIALIZER = REGISTRATE
            .generic("suspicious_crafting", Registries.RECIPE_SERIALIZER, () -> new RecipeSerializer<>(SuspiciousCraftingRecipe.Serializer.CODEC, SuspiciousCraftingRecipe.Serializer.STREAM_CODEC))
            .register();

    public static Stream<Pair<ItemLike, ItemStack>> getSuspiciousVariants(ItemLike output, int factor) {
        return BuiltInRegistries.ITEM.get(ItemTags.SMALL_FLOWERS)
                .stream()
                .flatMap(HolderSet.ListBacked::stream)
                .map(Holder::value)
                .filter(BlockItem.class::isInstance)
                .map(item -> ((BlockItem) item).getBlock())
                .filter(FlowerBlock.class::isInstance)
                .map(FlowerBlock.class::cast)
                .map(flower -> {
                    var outputStack = new ItemStack(output);
                    var effects = flower.getSuspiciousEffects().effects();
                    var modifiedEffects = new SuspiciousStewEffects(effects
                            .stream()
                            .map(it -> new SuspiciousStewEffects.Entry(it.effect(), it.duration() * factor))
                            .toList()
                    );
                    outputStack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, modifiedEffects);

                    return new Pair<>(flower, outputStack);
                });
    }

    private static Stream<RecipeHolder<CraftingRecipe>> createSuspiciousRecipes(RecipeHolder<SuspiciousCraftingRecipe> from) {
        var weed = Ingredient.of(NirvanaItems.WEED);

        return getSuspiciousVariants(from.value().getContainer().getItem(), from.value().durationFactor).map(pair -> {
            var flowerBlock = pair.getFirst().asItem();
            var output = pair.getSecond();
            var type = BuiltInRegistries.ITEM.getKey(flowerBlock);

            Ingredient flower = Ingredient.of(flowerBlock);
            NonNullList<Ingredient> inputs = NonNullList.createWithCapacity(from.value().requiredFlowers + from.value().requiredWeed + 1);
            for (int i = 0; i < from.value().requiredFlowers; i++) inputs.add(flower);
            for (int i = 0; i < from.value().requiredWeed; i++) inputs.add(weed);
            inputs.add(from.value().base);

            var id = from.id().identifier().withSuffix("/" + type.getNamespace() + "/" + type.getPath());
            var recipe = new ShapelessRecipe(new net.minecraft.world.item.crafting.Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, from.id().identifier().getPath()), net.minecraft.world.item.ItemStackTemplate.fromNonEmptyStack(output), inputs);
            return new RecipeHolder<>(net.minecraft.resources.ResourceKey.create(Registries.RECIPE, id), recipe);
        });
    }

    public static List<RecipeHolder<CraftingRecipe>> createSuspiciousRecipes() {
        var recipeManager = ((net.fabricmc.fabric.api.recipe.v1.FabricRecipeAccess) Minecraft.getInstance().getConnection().recipes()).getSynchronizedRecipes();
        return recipeManager.getAllOfType(RecipeType.CRAFTING)
                .stream()
                .filter(it -> it.value() instanceof SuspiciousCraftingRecipe)
                .map(it -> new RecipeHolder<>(it.id(), (SuspiciousCraftingRecipe) it.value()))
                .flatMap(NirvanaRecipeTypes::createSuspiciousRecipes)
                .toList();
    }

    public static void register() {
        net.fabricmc.fabric.api.recipe.v1.sync.RecipeSynchronization.synchronizeRecipeSerializer(SUSPICIOUS_RECIPE_SERIALIZER.get());
    }

}
