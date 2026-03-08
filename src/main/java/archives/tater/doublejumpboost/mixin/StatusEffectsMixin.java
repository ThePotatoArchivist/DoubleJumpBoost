package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MobEffects.class)
public class StatusEffectsMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=jump_boost")
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffect;addAttributeModifier(Lnet/minecraft/core/Holder;Lnet/minecraft/resources/Identifier;DLnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;)Lnet/minecraft/world/effect/MobEffect;", ordinal = 0)
    )
    private static MobEffect changeAttribute(MobEffect original) {
        return original.addAttributeModifier(DoubleJumpBoost.EXTRA_JUMPS, DoubleJumpBoost.id("jump_boost"), 1.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
