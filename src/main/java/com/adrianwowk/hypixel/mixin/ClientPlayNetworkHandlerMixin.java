package com.adrianwowk.hypixel.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

//    @ModifyArgs(method = "onVelocityUpdate(Lnet/minecraft/network/packet/s2c/play/EntityVelocityUpdateS2CPacket;)V",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocityClient(DDD)V"))
//    private void injected(Args args) {
//
//        double scale =  HypixelClient.kb;
//
//        double a0 = args.get(0);
//        double a1 = args.get(1);
//        double a2 = args.get(2);
//
//        args.set(0, a0 * scale);
//        if (scale < 1.0)
//            args.set(1, a1 * scale * 0.9);
//        else
//            args.set(1, a1 * scale);
//        args.set(2, a2 * scale);
//    }
//
//    @Inject(method = "onVelocityUpdate(Lnet/minecraft/network/packet/s2c/play/EntityVelocityUpdateS2CPacket;)V", at = @At("HEAD"), cancellable = true)
//    private void inject(CallbackInfo info){
////        info.cancel();
//    }
//
//    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
//    private void injectMethod(Packet<?> packet, CallbackInfo info) {
//        if (MinecraftClient.getInstance().options.keyLoadToolbarActivator.isPressed()
//                && !(packet instanceof UpdateSelectedSlotC2SPacket)
//                && !(packet instanceof PlayerInteractItemC2SPacket)
//                && !(packet instanceof PlayerInteractEntityC2SPacket)
//                && !(packet instanceof HandSwingC2SPacket))
//            info.cancel();
//    }
}
