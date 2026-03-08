package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Debug(export = true)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
            method = "getJumpBoostPower",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hasEffect(Lnet/minecraft/core/Holder;)Z")
    )
    private boolean disableJumpBoost(boolean original) {
        return original && !((Object) this instanceof Player);
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
            method = "aiStep",
            at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/world/entity/LivingEntity;onGround()Z")
    )
    private boolean allowJump(boolean original, @Share("doubleJumped") LocalBooleanRef doubleJumped) {
        if (original || !((Object) this instanceof Player player)) return original;
        if (!DoubleJumpBoost.canJump(player)) return false;
        doubleJumped.set(true);
        return true;
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;jumpFromGround()V")
    )
    private void decrementJumps(CallbackInfo ci, @Share("doubleJumped") LocalBooleanRef doubleJumped) {
        if (doubleJumped.get() && (Object) this instanceof Player player) DoubleJumpBoost.onJump(player);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "aiStep",
            at = @At("TAIL")
    )
    private void resetJumps(CallbackInfo ci) {
        if (onGround() && (Object) this instanceof Player player)
            DoubleJumpBoost.resetJump(player);
    }
}
