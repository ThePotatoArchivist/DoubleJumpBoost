package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(StatusEffects.class)
public class StatusEffectsMixin {
    @ModifyExpressionValue(
            method = "<clinit>",
            slice = @Slice(
                    from = @At(value = "CONSTANT", args = "stringValue=jump_boost")
            ),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;addAttributeModifier(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/Identifier;DLnet/minecraft/entity/attribute/EntityAttributeModifier$Operation;)Lnet/minecraft/entity/effect/StatusEffect;", ordinal = 0)
    )
    private static StatusEffect changeAttribute(StatusEffect original) {
        return original.addAttributeModifier(DoubleJumpBoost.EXTRA_JUMPS, DoubleJumpBoost.id("jump_boost"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE);
    }
}
