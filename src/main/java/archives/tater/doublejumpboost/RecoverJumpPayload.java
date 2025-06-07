package archives.tater.doublejumpboost;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record RecoverJumpPayload(int jumps) implements CustomPayload {
    public static PacketCodec<ByteBuf, RecoverJumpPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, RecoverJumpPayload::jumps, RecoverJumpPayload::new);
    public static Id<RecoverJumpPayload> ID = new Id<>(DoubleJumpBoost.id("recover_jump"));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
