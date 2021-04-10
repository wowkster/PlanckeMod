package com.adrianwowk.hypixel.client.gui;

import com.adrianwowk.hypixel.client.options.HypixelOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.options.Option;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private static Option[] OPTIONS;
    private HypixelOptions settings;

    private void initOptions(){
//        DoubleOption key = new DoubleOption("options.api.key", 0D, 2D, 0.05F, (gameOptions) -> {
//            return settings.API_KEY;
//        }, (gameOptions, double_) -> {
//            settings.kb = double_;
//        }, (gameOptions, doubleOption) -> {
//            double d = settings.kb;
//            String string = "Knockback: ";
//            if (d == 0D)
//                return string + "None";
//            return string + String.format("%.2fx", d);
//        });
//        OPTIONS = new Option[] {};
    }

    public ConfigScreen(Screen parent) {
        super(new LiteralText("Hypixel Client Options"));
        this.parent = parent;
//        this.settings = MinecraftClient.getInstance().options;
        initOptions();
    }

    protected void init() {
        int i = 0;
        Option[] var2 = OPTIONS;
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Option option = var2[var4];
            int j = this.width / 2 - 155 + i % 2 * 160;
            int k = this.height / 6 - 12 + 24 * (i >> 1);
            this.addButton(option.createButton(this.minecraft.options, j, k, 150));
            ++i;
        }

        this.addButton(new TextFieldWidget(minecraft.textRenderer, this.width / 2 - 155, this.height / 6 + 96 - 6, 150, 20, "Test"));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 96 - 6, 150, 20,"Get Text", (buttonWidget) -> {
            for (AbstractButtonWidget widget : buttons) {
                if (widget instanceof TextFieldWidget) {
                    TextFieldWidget tfw = (TextFieldWidget) widget;
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText(tfw.getText()));
                }
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 155, this.height / 6 + 120 - 6, 150, 20,"Button 1", (buttonWidget) -> {
//            this.minecraft.openScreen(null);
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 5, this.height / 6 + 120 - 6, 150, 20, "Button 2", (buttonWidget) -> {
//            this.minecraft.openScreen(null);
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height / 6 + 168, 200, 20, I18n.translate("gui.done"), (buttonWidget) -> {
            this.minecraft.openScreen(this.parent);
        }));
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.renderBackground();
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 15, 16777215);
        super.render(mouseX, mouseY, delta);
    }
}
