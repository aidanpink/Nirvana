package galena.hats.network;

import galena.hats.ConfigData;
import galena.hats.Constants;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ServerboundConfigMessage(ConfigData data) implements CustomPacketPayload {

    public static final TypeAndCodec<FriendlyByteBuf, ServerboundConfigMessage> TYPE = new TypeAndCodec<>(
            new Type<>(Constants.createId("serverbound")),
            StreamCodec.of(ServerboundConfigMessage::encode, ServerboundConfigMessage::decode)
    );

    public static void encode(FriendlyByteBuf buffer, ServerboundConfigMessage message) {
        message.data().encode(buffer);
    }

    public static ServerboundConfigMessage decode(FriendlyByteBuf buffer) {
        var data = ConfigData.decode(buffer);
        return new ServerboundConfigMessage(data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE.type();
    }

}
