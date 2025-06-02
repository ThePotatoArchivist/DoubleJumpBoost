package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Shadow public ServerPlayerEntity player;

	@ModifyExpressionValue(
			method = "onPlayerMove",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;isOnGround()Z", ordinal = 0)
	)
	private boolean allowJump(boolean original, @Share("doubleJumped") LocalBooleanRef doubleJumped) {
		if (original) return true;
		if (!DoubleJumpBoost.canJump(player)) return false;
		doubleJumped.set(true);
		return true;
	}

	@Inject(
			method = "onPlayerMove",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;jump()V")
	)
	private void decrementJumps(PlayerMoveC2SPacket packet, CallbackInfo ci, @Share("doubleJumped") LocalBooleanRef doubleJumped) {
		if (doubleJumped.get()) DoubleJumpBoost.onJump(player);
	}
}