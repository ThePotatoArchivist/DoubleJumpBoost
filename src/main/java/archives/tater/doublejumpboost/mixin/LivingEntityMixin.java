package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
@Debug(export = true)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract void playSound(@Nullable SoundEvent sound);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
            method = "getJumpBoostVelocityModifier",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")
    )
    private boolean disableJumpBoost(boolean original) {
        return original && !((Object) this instanceof PlayerEntity);
    }

    @SuppressWarnings("ConstantValue")
    @ModifyExpressionValue(
            method = "tickMovement",
            at = @At(value = "INVOKE:LAST", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z")
    )
    private boolean allowJump(boolean original, @Share("doubleJumped") LocalBooleanRef doubleJumped) {
        if (original || !((Object) this instanceof PlayerEntity player)) return true;
        if (!DoubleJumpBoost.canJump(player)) return false;
        doubleJumped.set(true);
        return true;
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;jump()V")
    )
    private void decrementJumps(CallbackInfo ci, @Share("doubleJumped") LocalBooleanRef doubleJumped) {
        if (doubleJumped.get() && (Object) this instanceof PlayerEntity player) DoubleJumpBoost.onJump(player);
    }

    @SuppressWarnings("ConstantValue")
    @Inject(
            method = "tickMovement",
            at = @At("TAIL")
    )
    private void resetJumps(CallbackInfo ci) {
        if (isOnGround() && (Object) this instanceof PlayerEntity player)
            DoubleJumpBoost.resetJump(player);
    }
}
