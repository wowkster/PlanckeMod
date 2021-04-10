package com.adrianwowk.hypixel.mixin;

import com.adrianwowk.hypixel.client.gui.ConfigScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets()V", at = @At("TAIL"), cancellable = true)
    private void inject(CallbackInfo info) {
        this.addButton(new ButtonWidget(this.width / 2 - 102, this.height / 4 + 72 + -16, /*204*/ 98, 20, "§3Mod Options", (buttonWidgetx) -> {
            this.minecraft.openScreen(new ConfigScreen(this));
        }));
        this.buttons.remove(3);
        this.children.remove(3);
    }
}
