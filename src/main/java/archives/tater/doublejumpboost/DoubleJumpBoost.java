package archives.tater.doublejumpboost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.max;

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

	public static final AttachmentType<Integer> USED_JUMPS = AttachmentRegistry.create(
			id("remaining_jumps")
	);

	public static boolean canJump(PlayerEntity player) {
        return !player.getWorld().isClient || player.getAttachedOrSet(USED_JUMPS, 0) < (int) player.getAttributeValue(EXTRA_JUMPS);
	}

	public static void onJump(PlayerEntity player) {
        player.setAttached(USED_JUMPS, player.getAttachedOrElse(USED_JUMPS, 0) + 1);
	}

	public static void resetJump(PlayerEntity player) {
		if (player.getAttachedOrSet(USED_JUMPS, 0) > 0)
			player.setAttached(USED_JUMPS, 0);
	}

	public static void recoverJump(PlayerEntity player) {
		player.setAttached(USED_JUMPS, max(0, player.getAttachedOrElse(USED_JUMPS, 0) - 1));
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		PayloadTypeRegistry.playS2C().register(RecoverJumpPayload.ID, RecoverJumpPayload.CODEC);
	}
}