package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.RecoverJumpPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MaceItem;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MaceItem.class)
public class MaceItemMixin {
    @Inject(
            method = "postHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setIgnoreFallDamageFromCurrentExplosion(Z)V")
    )
    private void recoverJump(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (attacker instanceof ServerPlayerEntity serverPlayerEntity)
            ServerPlayNetworking.send(serverPlayerEntity, new RecoverJumpPayload(1));
    }
}
