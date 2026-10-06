package galena.nirvana.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/** Keeps the original patch distribution after vanilla removed random patches. */
public final class WildHempPatchFeature extends Feature<WildHempPatchFeature.Config> {
    public record Config(int tries, int xzSpread, int ySpread, Holder<PlacedFeature> feature) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(0, 1024).fieldOf("tries").forGetter(Config::tries),
            Codec.intRange(0, 128).fieldOf("xz_spread").forGetter(Config::xzSpread),
            Codec.intRange(0, 128).fieldOf("y_spread").forGetter(Config::ySpread),
            PlacedFeature.CODEC.fieldOf("feature").forGetter(Config::feature)
        ).apply(instance, Config::new));
    }

    public WildHempPatchFeature() { super(Config.CODEC); }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        var config = context.config();
        var random = context.random();
        int placed = 0;
        int horizontal = config.xzSpread() + 1;
        int vertical = config.ySpread() + 1;
        for (int i = 0; i < config.tries(); i++) {
            var pos = context.origin().offset(random.nextInt(horizontal) - random.nextInt(horizontal),
                random.nextInt(vertical) - random.nextInt(vertical),
                random.nextInt(horizontal) - random.nextInt(horizontal));
            if (config.feature().value().place(context.level(), context.chunkGenerator(), random, pos)) placed++;
        }
        return placed > 0;
    }
}
