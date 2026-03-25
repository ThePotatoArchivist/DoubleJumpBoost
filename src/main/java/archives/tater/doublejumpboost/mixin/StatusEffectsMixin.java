package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

@Mixin(MobEffects.class)
public class StatusEffectsMixin {
    @Definition(id = "JUMP_BOOST", field = "Lnet/minecraft/world/effect/MobEffects;JUMP_BOOST:Lnet/minecraft/core/Holder;")
    @Definition(id = "register", method = "Lnet/minecraft/world/effect/MobEffects;register(Ljava/lang/String;Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/core/Holder;")
    @Expression("JUMP_BOOST = register(?, @(?))")
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private static MobEffect changeAttribute(MobEffect original) {
        return original.addAttributeModifier(DoubleJumpBoost.EXTRA_JUMPS, DoubleJumpBoost.id("jump_boost"), 1.0, AttributeModifier.Operation.ADD_VALUE);
    }
}
