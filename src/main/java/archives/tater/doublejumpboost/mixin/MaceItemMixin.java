package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.RecoverJumpPayload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;

@Mixin(MaceItem.class)
public class MaceItemMixin {
    @Inject(
            method = "hurtEnemy",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setIgnoreFallDamageFromCurrentImpulse(Z)V")
    )
    private void recoverJump(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci) {
        if (attacker instanceof ServerPlayer serverPlayerEntity)
            ServerPlayNetworking.send(serverPlayerEntity, new RecoverJumpPayload(1));
    }
}
