package archives.tater.doublejumpboost.mixin;

import archives.tater.doublejumpboost.DoubleJumpBoost;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Player.class)
public class PlayerEntityMixin {
    @ModifyReturnValue(
            method = "createAttributes",
            at = @At("RETURN")
    )
    private static AttributeSupplier.Builder addAttribute(AttributeSupplier.Builder original) {
        return original.add(DoubleJumpBoost.EXTRA_JUMPS);
    }
}
