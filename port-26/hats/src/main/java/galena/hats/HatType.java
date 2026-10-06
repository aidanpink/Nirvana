package galena.hats;

import com.mojang.serialization.Codec;
import galena.hats.storage.ConfigStorage;
import java.util.*;
import java.util.function.Predicate;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public enum HatType implements StringRepresentable {

    BINOME(),
    BIOME_VOTE(),
    COPPERATIVE(),
    DOOM_GLOOM(),
    FERMION(),
    GILDED(hasFlag("gilded")),
    HEART_CRYSTAL(),
    NIRVANA(),
    OVERWEIGHT_FARMING(),
    TRANS(pride()),
    PRIDE(pride()),
    ARROW_PRIDE(pride(), Set.of(HatPart.ARROW)),
    BI(pride()),
    ACE(pride()),
    GAY_MALE(pride()),
    LESBIAN(pride()),
    NONBINARY(pride()),
    PAN(pride()),
    TRINKETS(),
    WINDSWEPT(),
    ZOMBIE_FRIENDS(),
    ARROW(Set.of(HatPart.ARROW)),
    MEHVAHDJUKAAR(hasFlag("mehvahdjukaar")),
    TUCCUT(hasFlag("tuccut"), Set.of(HatPart.PLANT)),
    YOUTUBE(hasFlag("youtube")),
    METALLICA(Set.of(HatPart.RIM)),
    UNPLEASANT_GRADIENT(Set.of(HatPart.EARS)),
    ;

    private static Predicate<SupporterData> pride() {
        return hasFlag("pride").or(isSupporter());
    }

    private static Predicate<SupporterData> aboveRank(int min) {
        return it -> it.rank() > min;
    }

    private static Predicate<SupporterData> isSupporter() {
        return aboveRank(0);
    }

    private static Predicate<SupporterData> isDeveloper() {
        return aboveRank(99);
    }

    private static Predicate<SupporterData> hasFlag(String flag) {
        return it -> it.flags().contains(flag);
    }

    public static Optional<HatType> of(LivingEntity entity) {
        if (entity instanceof Player player) {
            var config = ConfigStorage.getConfig(player);
            return config
                .filter(ConfigData::enabled)
                .map(ConfigData::type);
        }

        return Optional.empty();

    }

    public static List<HatType> allowed(UUID uuid) {
        return HatsApi.getSupporterData(uuid)
            .map(HatType::allowed)
            .orElseGet(Collections::emptyList);
    }

    public static List<HatType> allowed(SupporterData data) {
        return Arrays.stream(HatType.values())
            .filter(it -> it.predicate.test(data))
            .toList();
    }

    public static final Codec<HatType> CODEC = StringRepresentable.fromEnum(HatType::values);

    public final Identifier texture;
    private final Predicate<SupporterData> predicate;
    public final Collection<HatPart> parts;

    HatType(Predicate<SupporterData> predicate, Collection<HatPart> parts) {
        this.predicate = predicate;
        this.texture = Constants.createId("textures/models/" + getSerializedName() + "_tophat.png");
        this.parts = parts;
    }

    HatType(Predicate<SupporterData> predicate) {
        this(predicate, Collections.emptySet());
    }

    HatType(Collection<HatPart> parts) {
        this(isSupporter(), parts);
    }

    HatType() {
        this(isSupporter(), Collections.emptySet());
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

}
