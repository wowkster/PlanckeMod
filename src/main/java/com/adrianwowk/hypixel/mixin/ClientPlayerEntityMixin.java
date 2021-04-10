package com.adrianwowk.hypixel.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
//    @Inject(method = "sendMovementPackets()V", at = @At("HEAD"), cancellable = true)
//    private void injectMovementPackets(CallbackInfo info) {
//        if (MinecraftClient.getInstance().options.keyLoadToolbarActivator.isPressed()) {
////            MinecraftClient.getInstance().player.setVelocityClient(0,0,0);
//            info.cancel();
//        }
//    }
//
//    @Inject(method = "tickMovement()V", at = @At("HEAD"), cancellable = true)
//    private void injectMovement(CallbackInfo info) {
//        if (MinecraftClient.getInstance().options.keySaveToolbarActivator.isPressed()){
//            double velX = MinecraftClient.getInstance().player.getVelocity().x;
//            double velY = MinecraftClient.getInstance().player.getVelocity().y;
//            double velZ = MinecraftClient.getInstance().player.getVelocity().z;
//            MinecraftClient.getInstance().player.setVelocityClient(velX,0, velZ);
//        }
//        if (MinecraftClient.getInstance().options.keyLoadToolbarActivator.isPressed()){
//
//            info.cancel();
//        }
//    }
}
