package archives.tater.doublejumpboost;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.max;

@SuppressWarnings("UnstableApiUsage")
public class DoubleJumpBoost implements ModInitializer {
	public static final String MOD_ID = "doublejumpboost";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Holder<Attribute> EXTRA_JUMPS = Registry.registerForHolder(
			BuiltInRegistries.ATTRIBUTE,
			id("extra_jumps"),
			new RangedAttribute("attribute.name.doublejumpboost.extra_jumps", 0, 0, 10)
					.setSentiment(Attribute.Sentiment.POSITIVE)
					.setSyncable(true)
	);

	public static final AttachmentType<Integer> USED_JUMPS = AttachmentRegistry.create(
			id("remaining_jumps")
	);

	public static boolean canJump(Player player) {
        return !player.level().isClientSide() || player.getAttachedOrSet(USED_JUMPS, 0) < (int) player.getAttributeValue(EXTRA_JUMPS);
	}

	public static void onJump(Player player) {
        player.setAttached(USED_JUMPS, player.getAttachedOrElse(USED_JUMPS, 0) + 1);
	}

	public static void resetJump(Player player) {
		if (player.getAttachedOrSet(USED_JUMPS, 0) > 0)
			player.setAttached(USED_JUMPS, 0);
	}

	public static void recoverJump(Player player) {
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