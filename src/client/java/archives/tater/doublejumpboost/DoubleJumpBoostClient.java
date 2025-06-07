package archives.tater.doublejumpboost;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class DoubleJumpBoostClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(RecoverJumpPayload.ID, (payload, context) -> {
            DoubleJumpBoost.recoverJump(context.player());
        });
    }
}
