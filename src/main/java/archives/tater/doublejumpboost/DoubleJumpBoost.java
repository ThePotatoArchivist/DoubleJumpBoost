package archives.tater.doublejumpboost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.max;
import static java.util.Objects.requireNonNullElse;

@SuppressWarnings("UnstableApiUsage")
public class DoubleJumpBoost implements ModInitializer {
	public static final String MOD_ID = "doublejumpboost";

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryEntry<EntityAttribute> EXTRA_JUMPS = Registry.registerReference(
			Registries.ATTRIBUTE,
			id("extra_jumps"),
			new ClampedEntityAttribute("attribute.name.doublejumpboost.extra_jumps", 0, 0, 10)
					.setCategory(EntityAttribute.Category.POSITIVE)
					.setTracked(true)
	);

	public static final AttachmentType<Integer> REMAINING_JUMPS = AttachmentRegistry.create(
			id("remaining_jumps"),
			builder -> builder
                    .syncWith(PacketCodecs.INTEGER, AttachmentSyncPredicate.targetOnly())
					.initializer(() -> 0)
	);

	public static boolean canJump(PlayerEntity player) {
        return requireNonNullElse(player.getAttached(REMAINING_JUMPS), 0) > 0;
	}

	public static void onJump(PlayerEntity player) {
        player.setAttached(REMAINING_JUMPS, max(0, requireNonNullElse(player.getAttached(REMAINING_JUMPS), 0) - 1));
	}

	public static void resetJump(PlayerEntity player) {
		var maxJumps = (int) player.getAttributeValue(EXTRA_JUMPS);

		if (requireNonNullElse(player.getAttached(REMAINING_JUMPS), 0) < maxJumps)
			player.setAttached(REMAINING_JUMPS, maxJumps);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
	}
}