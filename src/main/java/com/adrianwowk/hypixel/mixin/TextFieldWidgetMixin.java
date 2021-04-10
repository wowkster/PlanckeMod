package com.adrianwowk.hypixel.mixin;

import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin extends AbstractButtonWidget {
    protected TextFieldWidgetMixin(int x, int y, String text) {
        super(x, y, text);
    }

    @Inject(method = "keyPressed(III)Z", at = @At( "HEAD"), cancellable = true)
    public void inject(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir){
//        System.out.println("Mixin Called!");
    }
}
