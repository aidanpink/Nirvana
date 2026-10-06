package galena.hats.network;

import galena.hats.ConfigData;
import galena.hats.Constants;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundConfigMessage(Map<UUID, ConfigData> values) implements CustomPacketPayload {

    public static final CustomPacketPayload.TypeAndCodec<FriendlyByteBuf, ClientboundConfigMessage> TYPE = new CustomPacketPayload.TypeAndCodec<>(
            new CustomPacketPayload.Type<>(Constants.createId("clientbound")),
            StreamCodec.of(ClientboundConfigMessage::encode, ClientboundConfigMessage::decode)
    );

    public static void encode(FriendlyByteBuf buffer, ClientboundConfigMessage message) {
        buffer.writeMap(
                message.values(),
                (b, u) -> b.writeUUID(u),
                (b, d) -> d.encode(b)
        );
    }

    public static ClientboundConfigMessage decode(FriendlyByteBuf buffer) {
        var values = buffer.readMap(
                b -> b.readUUID(),
                ConfigData::decode
        );
        return new ClientboundConfigMessage(values);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

}
