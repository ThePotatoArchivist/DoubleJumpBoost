package archives.tater.doublejumpboost.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin extends Player {
	public ClientPlayerEntityMixin(Level world, GameProfile profile) {
		super(world, profile);
	}

	@ModifyExpressionValue(
			method = "updateAutoJump",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEffect(Lnet/minecraft/core/Holder;)Z")
	)
	private boolean disableJumpBoost(boolean original) {
		return false;
	}
}