package archives.tater.doublejumpboost;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record RecoverJumpPayload(int jumps) implements CustomPacketPayload {
    public static StreamCodec<ByteBuf, RecoverJumpPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, RecoverJumpPayload::jumps, RecoverJumpPayload::new);
    public static Type<RecoverJumpPayload> ID = new Type<>(DoubleJumpBoost.id("recover_jump"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
