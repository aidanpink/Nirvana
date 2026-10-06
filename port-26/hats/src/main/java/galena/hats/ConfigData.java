package galena.hats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.network.FriendlyByteBuf;

public record ConfigData(HatType type, boolean enabled) {
    public static ConfigData DEFAULT = new ConfigData(HatType.BINOME, false);

    public static final Codec<ConfigData> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    HatType.CODEC.optionalFieldOf("type").xmap(it -> it.orElse(DEFAULT.type()), Optional::ofNullable).forGetter(ConfigData::type),
                    Codec.BOOL.optionalFieldOf("enabled").xmap(it -> it.orElse(DEFAULT.enabled()), Optional::ofNullable).forGetter(ConfigData::enabled)
            ).apply(builder, ConfigData::new)
    );

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(type());
        buffer.writeBoolean(enabled());
    }

    public static ConfigData decode(FriendlyByteBuf buffer) {
        return new ConfigData(
                buffer.readEnum(HatType.class),
                buffer.readBoolean()
        );
    }
}
