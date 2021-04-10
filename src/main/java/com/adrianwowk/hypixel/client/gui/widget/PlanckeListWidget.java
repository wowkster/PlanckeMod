package com.adrianwowk.hypixel.client.gui.widget;

import com.adrianwowk.hypixel.client.gui.CurrentScreen;
import com.adrianwowk.hypixel.client.gui.PlanckeScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class PlanckeListWidget<E extends EntryListWidget.Entry<E>> extends EntryListWidget<E> {

    private final CurrentScreen screen;
    private List<PlanckeListWidget.ResultEntry> results;

    public PlanckeListWidget(CurrentScreen screen, MinecraftClient client, int width, int height, int top, int bottom, int entryHeight) {
        super(client, width, height, top, bottom, entryHeight);
        this.screen = screen;
        this.results = new ArrayList<>();
    }

    private void updateEntries() {
        this.clearEntries();
        for (ResultEntry result : this.results) {
            addEntry((E) result);
        }
    }

    public void setEntries(List<Text> textList) {
        this.results.clear();

        for(Text text : textList) {
            this.results.add(new PlanckeListWidget.ResultEntry(this.screen, text, this));
        }

        this.updateEntries();
    }

    public int getEntryHeight(){
        return this.itemHeight;
    }

    public void render(int mouseX, int mouseY, float delta) {
        int i = this.getScrollbarPosition();
        int j = i + 6;
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        this.minecraft.getTextureManager().bindTexture(DrawableHelper.BACKGROUND_LOCATION);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.getRowLeft();
        int l = this.top + 4 - (int)this.getScrollAmount();
        if (this.renderHeader) {
            this.renderHeader(k, l, tessellator);
        }

        this.renderList(k, l, mouseX, mouseY, delta);
        GlStateManager.disableDepthTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlphaTest();
        GlStateManager.shadeModel(7425);
        GlStateManager.disableTexture();
        int n = getMaxScroll();
        if (n > 0) {
            int o = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / (float)this.getMaxPosition());
            o = MathHelper.clamp(o, 32, this.bottom - this.top - 8);
            int p = (int)this.getScrollAmount() * (this.bottom - this.top - o) / n + this.top;
            if (p < this.top) {
                p = this.top;
            }

            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex((double)i, (double)this.bottom, 0.0D).texture(0.0D, 1.0D).color(0, 0, 0, 200).next();
            bufferBuilder.vertex((double)j, (double)this.bottom, 0.0D).texture(1.0D, 1.0D).color(0, 0, 0, 200).next();
            bufferBuilder.vertex((double)j, (double)this.top, 0.0D).texture(1.0D, 0.0D).color(0, 0, 0, 200).next();
            bufferBuilder.vertex((double)i, (double)this.top, 0.0D).texture(0.0D, 0.0D).color(0, 0, 0, 200).next();
            tessellator.draw();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex((double)i, (double)(p + o), 0.0D).texture(0.0D, 1.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)j, (double)(p + o), 0.0D).texture(1.0D, 1.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)j, (double)p, 0.0D).texture(1.0D, 0.0D).color(128, 128, 128, 255).next();
            bufferBuilder.vertex((double)i, (double)p, 0.0D).texture(0.0D, 0.0D).color(128, 128, 128, 255).next();
            tessellator.draw();
            bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
            bufferBuilder.vertex((double)i, (double)(p + o - 1), 0.0D).texture(0.0D, 1.0D).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((double)(j - 1), (double)(p + o - 1), 0.0D).texture(1.0D, 1.0D).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((double)(j - 1), (double)p, 0.0D).texture(1.0D, 0.0D).color(192, 192, 192, 255).next();
            bufferBuilder.vertex((double)i, (double)p, 0.0D).texture(0.0D, 0.0D).color(192, 192, 192, 255).next();
            tessellator.draw();
        }

        GlStateManager.enableTexture();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlphaTest();
        GlStateManager.disableBlend();
    }

    private int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4));
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() - 22;
    }

    public int getRowWidth() {
        return super.getRowWidth() + 25;
    }

    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }

    public static String rankedNameFromText(Text txt){
        String raw = txt.getString();
        String[] split = raw.split(" ");
        if (split.length == 3)
            return split[1];
        if (split.length == 4)
            return split[1] + " " + split[2];
        return "";
    }

    public static String nameFromText(Text txt){
        String raw = txt.getString();
        raw = raw.replaceAll("§.", "");
        String[] split = raw.split(" ");
        if (split.length == 3)
            return split[1];
        if (split.length == 4)
            return split[2];
        return "";
    }

    @Environment(EnvType.CLIENT)
    public class ResultEntry extends PlanckeListWidget.Entry {
        private final CurrentScreen screen;
        private final MinecraftClient client;
        private final Text text;
        private final PlanckeListWidget list;
        private int x = 0;
        private int y = 0;
        private final Integer achievementPoints;
        private final Double playerLevel;

        protected ResultEntry(CurrentScreen screen, Text text, PlanckeListWidget list) {
            this.screen = screen;
            this.client = MinecraftClient.getInstance();
            this.text = text;
            this.list = list;

            this.achievementPoints = (Integer) screen.achievementPoints.get(nameFromText(text));
            this.playerLevel = (Double) screen.playerLevel.get(nameFromText(text));
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (CurrentScreen.messages.containsValue(this.text.getString())) {
                return false;
            }

            if (mouseY >= y && mouseY <= y + list.getEntryHeight() && mouseY >= screen.getY() + 46 && mouseY <= screen.getY() + 255 - 56 && mouseX >= x + 10 && mouseX <= list.getScrollbarPosition()){
                ((PlanckeScreen)this.screen.parent).outputFromUsername(nameFromText(this.text));
                this.screen.getMinecraft().openScreen(this.screen.parent);
                return true;
            }
            return false;
        }

        @Override
        public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
            int x1 = x + 10;
            int y1 = y;
            int x2 = list.getScrollbarPosition();
            int y2 = y + list.getEntryHeight();

            if (mouseY >= y1 && mouseY <= y2 && mouseY >= screen.getY() + 46 && mouseY <= screen.getY() + 255 - 56 && mouseX >= x1 && mouseX <= x2) {
                if (!CurrentScreen.messages.containsValue(this.text.getString())) {
                    fill(x1, y1, x2, y2, -0xafafaf);
                    screen.setTooltip(String.format("%s" + "\n§7Hypixel Level: §6" + String.format("%.02f", playerLevel) +
                            "\n§7Achievement Points: §e" + achievementPoints + "\n\n§eClick to view §b%s§e's stats", rankedNameFromText(text), nameFromText(text)));
                }
            }
            drawString(minecraft.textRenderer, this.text.getString(), x + 18, y + 5, 16777215);
            this.x = x;
            this.y = y;
        }
    }
    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends EntryListWidget.Entry<PlanckeListWidget.Entry> {
    }
}
