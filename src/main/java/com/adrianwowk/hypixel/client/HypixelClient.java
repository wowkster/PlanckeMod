package com.adrianwowk.hypixel.client;

import com.adrianwowk.hypixel.client.gui.ConfigScreen;
import com.adrianwowk.hypixel.client.gui.PlanckeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;


@Environment(EnvType.CLIENT)
public class HypixelClient implements ClientModInitializer {
//    public static double kb;
    private static KeyBinding openConfig;
    private static KeyBinding openPlankce;

    @Override
    public void onInitializeClient() {
//        HypixelOptions.instance = new HypixelOptions(MinecraftClient.getInstance().runDirectory);

        openConfig = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hypixel.open_config", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_G, // The keycode of the key
                "category.hypixel.keys" // The translation key of the keybinding's category.
        ));
        openPlankce = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hypixel.open_plancke", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_H, // The keycode of the key
                "category.hypixel.keys" // The translation key of the keybinding's category.
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfig.wasPressed()) {
                client.openScreen(new ConfigScreen(null));
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openPlankce.wasPressed()) {
                client.openScreen(new PlanckeScreen(null));
            }
        });

    }
}
