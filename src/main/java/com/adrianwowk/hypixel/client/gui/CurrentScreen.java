package com.adrianwowk.hypixel.client.gui;

import com.adrianwowk.hypixel.client.HypixelUtil;
import com.adrianwowk.hypixel.client.api.APIRequests;
import com.adrianwowk.hypixel.client.formatting.RankFomatter;
import com.adrianwowk.hypixel.client.gui.widget.PlanckeListWidget;
import com.adrianwowk.hypixel.client.commands.MyCommands;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.*;

public class CurrentScreen extends Screen{
    public final Screen parent;
    private List<Text> previousResults;
    private PlanckeListWidget listWidget;
    private String tooltipText;
    public static final HashMap<String, String> messages;
    public HashMap<String, Integer> achievementPoints = new HashMap<>();
    public HashMap<String, Double> playerLevel = new HashMap<>();

    static {
        messages = new HashMap<>();
        messages.put("playerCount", "§c§lToo Many Players");
        messages.put("loading", "§cLoading...");
        messages.put("mojangLimit", "§c§lToo Many Mojang API Requests!");
        messages.put("hypixelLimit", "§e§lToo Many Hypixel API Requests");
    }

    public CurrentScreen(Screen parent) {
        super(new LiteralText("Current Player Lookup"));
        this.parent = parent;
    }

    public int getX(){
        return this.width / 2 - 252 / 2;
    }
    public int getY(){
        return this.height / 2 - ((252 - 25) * 9 / 16);
    }

    protected void init() {
        int x = getX();
        int y = getY();
        this.addButton(new ButtonWidget(x + 15, y + 210, 108, 20,"Back", (buttonWidget) -> {
            this.minecraft.openScreen(this.parent);
        }));
        this.addButton(new ButtonWidget(x + 15 + 112, y + 210, 108, 20,"Refresh", (buttonWidget) -> {
            outputCurrent();
        }));

        this.listWidget = new PlanckeListWidget(this, this.minecraft, this.width, this.height, y + 45, y + 255 - 55, 18);
        this.children.add(listWidget);
        if (previousResults == null)
            outputCurrent();
        else
            this.listWidget.setEntries(previousResults);
    }

    public void render(int mouseX, int mouseY, float delta) {
        this.tooltipText = null;
        int x = getX();
        int y = getY();

        fill(x + 9 -2, y + 18, x + 9 + 234, y + 18 + 225, -0xbbbbbb);
        fill(x + 9 -2 + 8, y + 18, x + 9 + 234 - 8, y + 18 + 225, -0x9a9a9a);

        int y_ = (y > 150 ? y + 25 : y);
        int y__ = 6;
        if (y < 10)
            y__ = 7;
        if (y > 50)
            y__ = 6;
        if (y > 200)
            y__ = -20;

        this.listWidget.render(mouseX, mouseY, delta);

        fill(x + 9 -2, y + 18, x + 9 + 234, y + 45, -0xbbbbbb);
        fill(x + 9 -2, y + 255 - 55, x + 9 + 234, y + 255 , -0xbbbbbb);
        fill(x + 9 -2 + 8, y + 44, x + 9 + 234 - 8, y + 45, -0x555555);
        fill(x + 9 -2 + 8, y + 255 - 55, x + 9 + 234 - 8, y + 255 - 54, -0x555555);
        fill(x + 9 -2 + 8, y + 45, x + 9 -2 + 9, y + 255 - 55, -0x555555);
        fill(x + 9 + 234 - 8 - 1, y + 45 - 1, x + 9 + 234 - 8 , y + 255 - 55 + 1, -0x555555);
//        fill(x + 9 -2 + 8, y + 255 - 55, x + 9 + 234 - 8, y + 255 - 54, -0x555555);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        DiffuseLighting.disable();

        this.minecraft.getTextureManager().bindTexture(new Identifier("hypixel","textures/gui/plancke/window.png"));
        this.blit(x, y, 0, 0, 252, 252);
        this.drawCenteredString(this.font, "§f" + this.title.asFormattedString(), this.width / 2, y_ + y__, 16777215);
        this.drawString(minecraft.textRenderer, "§7Results:",x + 15, y_ + y__ + 20,16777215);

        ArrayList<AbstractButtonWidget> buttons_ = (ArrayList<AbstractButtonWidget>) this.buttons;
        for(int i = 0; i < buttons_.size(); ++i) {
            buttons_.get(i).render(mouseX, mouseY, delta);
        }

        if (this.tooltipText != null) {
            this.renderTooltip(Lists.newArrayList(Splitter.on("\n").split(this.tooltipText)), mouseX, mouseY);
        }
    }

    public void outputCurrent(){
        this.listWidget.setEntries(new ArrayList<>(Arrays.asList(new LiteralText(messages.get("loading")))));
        ArrayList<String> nicks = new ArrayList<>();
        ArrayList<Text> entries = new ArrayList<>();

        MyCommands.MOJANG_EXECUTOR.execute(() -> {
            try {
                List<UUID> uuids = new ArrayList<>();
                ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();

                assert clientPlayNetworkHandler != null;
                List<PlayerListEntry> list = Ordering.from((Comparator)(new MyCommands.EntryOrderComparator())).sortedCopy(clientPlayNetworkHandler.getPlayerList());
                if (!playerListToUuids(nicks, uuids, list)){
                    this.listWidget.setEntries(new ArrayList<>(Arrays.asList(new LiteralText(messages.get("playerCount")))));
                    MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                    return;
                }
                MyCommands.HTTP_EXECUTOR.execute(() -> {

                    for (UUID uuid : uuids){
                        if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                            this.listWidget.setEntries(new ArrayList<>(Arrays.asList(new LiteralText(messages.get("mojangLimit")))));
                            MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                            return;
                        }

                        JsonObject jsonObj = null;
                        try {
                            try {
                                jsonObj = APIRequests.getHypixelResponse(uuid);
                            } catch (IOException e){
                                this.listWidget.setEntries(new ArrayList<>(Arrays.asList(new LiteralText(messages.get("hypixelLimit")))));
                                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                                return;
                            }

                            if (jsonObj.get("player").isJsonNull())
                                continue;

                            JsonObject player = jsonObj.getAsJsonObject("player");

                            String rankPrefix = RankFomatter.getFormattedRank(player);
                            String playerBaseName = player.get("displayname").getAsString();
                            String playerName = rankPrefix + (rankPrefix.equals("§7") ? "" : " ") + playerBaseName;

                            JsonElement _playerLevel = player.get("networkExp");
                            double playerLevel = (_playerLevel != null ? HypixelUtil.getExactLevel(_playerLevel.getAsDouble()) : 0);

                            JsonElement _achievementPoints = player.get("achievementPoints");
                            int achievementPoints = (_playerLevel != null ? _achievementPoints.getAsInt() : 0);

                            Text text = new LiteralText("");
                            text.append((uuids.indexOf(uuid) + 1) + ". " );
                            text.append(playerName);
                            text.append(" §e§o(" + String.format("%.02f", playerLevel) + ")");
                            this.achievementPoints.put(playerBaseName, achievementPoints);
                            this.playerLevel.put(playerBaseName, playerLevel);
                            entries.add(text);

                        } catch (Exception e) {
                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                            for (StackTraceElement ste : e.getStackTrace()){
                                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                            }
                            e.printStackTrace();
                        }
                    }
//                text.append("\n§6§lNicks:\n");
//                for (String name : nicks){
//                    text.append("§8 - ");
//                    text.append(getCommandText("§7" + name,
//                            "This player might be nicked.",
//                            ""));
//                    text.append("\n");
//                }

                    this.listWidget.setEntries(entries);
                    previousResults = entries;
                    MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
                });
            }
            catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    public boolean playerListToUuids(ArrayList<String> nicks, List<UUID> uuids, List<PlayerListEntry> list) throws Exception {
        if (list.size() > 50)
            return false;
        for (PlayerListEntry p : list) {
            String name = MinecraftClient.getInstance().inGameHud.getPlayerListWidget().getPlayerName(p).getString();
            String[] parts = name.split(" ");
            if (parts.length == 0)
                name = "";
            else if (parts.length == 1)
                name = parts[0];
            else if (parts.length == 2)
                if (parts[0].charAt(0) == '[')
                    name = parts[1];
                else
                    name = parts[0];
            else if (parts.length == 3)
                name = parts[1];

            UUID uuid = APIRequests.playerNameToUUID(name);
            if (uuid == null){
                nicks.add(name);
            }
            else
                uuids.add(uuid);
        }
        return true;
    }

    public void setTooltip(String text) {
        this.tooltipText = text;
    }

    public MinecraftClient getMinecraft(){
        return this.minecraft;
    }
}
