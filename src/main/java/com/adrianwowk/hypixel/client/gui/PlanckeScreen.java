package com.adrianwowk.hypixel.client.gui;

import com.adrianwowk.hypixel.client.HypixelUtil;
import com.adrianwowk.hypixel.client.api.APIRequests;
import com.adrianwowk.hypixel.client.formatting.RankFomatter;
import com.adrianwowk.hypixel.client.commands.MyCommands;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PlanckeScreen extends Screen {
    private final Screen parent;
    private static List<String> results;
    private static List<String> results_;
    private static JsonObject lastPlayer;
    private TextFieldWidget tfw;
    private static boolean changeFocus;
    private static ButtonType btn1 = ButtonType.BEDWARS;
    private static ButtonType btn2 = ButtonType.SKYWARS;
    private static ButtonType btn3 = ButtonType.CVC;
    private ArrayList<String> cachedNames;
    private static CurrentScreen currScreen;
    private static List<String> previousRes;
    private static JsonObject previousPlayer;
    private boolean prevBtnClicked;

    public PlanckeScreen(Screen parent) {
        super(new LiteralText("Hypixel Plancke Lookup"));
        this.parent = parent;
            results = new ArrayList<>();
            results_ = new ArrayList<>();
        changeFocus = false;
        this.cachedNames = new ArrayList<>();
        this.prevBtnClicked = false;
        if (this.currScreen == null)
            this.currScreen = new CurrentScreen(this);
    }

    protected void init() {
        int x = this.width / 2 - 252 / 2;
        int y = this.height / 2 - ((252 - 25) * 9 / 16);
        addTextField(x, y);
        System.out.printf("Prev Player is null: %b - Prev Res is null: %b - Prev Btn Clicked: %b\n", previousPlayer == null, previousRes == null, prevBtnClicked);
        if (previousPlayer == null || previousRes == null || prevBtnClicked) {
            this.addButton(new ButtonWidget(x + 15, y + 210, 220, 20,"Lookup Current", (buttonWidget) -> {
                this.minecraft.openScreen(this.currScreen);
            }));
        } else {
            this.addButton(new ButtonWidget(x + 15, y + 210, 108, 20,"Last Player", (buttonWidget) -> {
                this.lastPlayer = previousPlayer;
                this.results_ = previousRes;
                this.prevBtnClicked = true;
                changeFocus = true;
                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
                rerenderButtons();
            }));
            this.addButton(new ButtonWidget(x + 15 + 112, y + 210, 108, 20,"Lookup Current", (buttonWidget) -> {
                this.minecraft.openScreen(this.currScreen);
            }));
        }

        System.out.printf("Last Player is null: %b - Results is empty: %b\n", lastPlayer == null, results.isEmpty());
        if (lastPlayer == null || results_.isEmpty())
            return;

        this.addButton(new ButtonWidget(x + 15 + (70 * 0) + (5 * 0), y + 180, 70, 20,btn1.getName(), btn1.getOnPress()));
        this.addButton(new ButtonWidget(x + 15 + (70 * 1) + (5 * 1), y + 180, 70, 20,btn2.getName(), btn2.getOnPress()));
        this.addButton(new ButtonWidget(x + 15 + (70 * 2) + (5 * 2), y + 180, 70, 20,btn3.getName(), btn3.getOnPress()));
        this.setInitialFocus(this.tfw);
    }

    @Override
    public void onClose() {
        if (lastPlayer != null)
            previousPlayer = lastPlayer;
//        previousRes = results_;
        if (prevBtnClicked)
            lastPlayer = null;
        super.onClose();
    }

    public void addTextField(int x, int y){
        tfw = new TextFieldWidget(minecraft.textRenderer, x + 15, y + 40, 165, 19, "Username Input");
        tfw.setEditable(true);
        tfw.setMaxLength(100);
        tfw.setSuggestion("");
        tfw.setVisible(true);
        tfw.setChangedListener(this::onTextChange);
        this.addButton(tfw);

        this.addButton(new ButtonWidget(x + 185 , y + 40, 50, 20,"Search", (buttonWidget) -> {
            outputFromUsername(tfw.getText());
        }));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onTextChange(String text) {
        String string = this.tfw.getText();
        tfw.setVisible(true);
        tfw.setEditable(true);
    }

    public void rerenderButtons(){
        this.buttons.clear();
        init();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height){
        rerenderButtons();
        super.resize(client, width, height);
    }

    public void outputFromUsername(String name){
        results_.clear();
        results_.add("§cLoading...");

        this.prevBtnClicked = true;

        MyCommands.MOJANG_EXECUTOR.execute(() -> {
            UUID uuid;
            if (name.length() < 20) {
                try {
                    uuid = APIRequests.playerNameToUUID(name);
                    if (name.equals("")) {
                        results_.clear();
                        results_.add("§7That name was not found :(");
                        lastPlayer = null;
//                    rerenderButtons();
                        return;
                    }
                    if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                        results_.clear();
                        results_.add(CurrentScreen.messages.get("mojangLimit"));
                        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                        return;
                    }
                    outputFromUUID(uuid);
                } catch(Exception e){
                    results_.clear();
                    results_.add("§7That name was not found :(");
                    lastPlayer = null;
                    e.printStackTrace();
                }
            } else{
                try {
                    uuid = UUID.fromString(name.replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                    ));
                    outputFromUUID(uuid);
                } catch (Exception e) {
                    results_.clear();
                    results_.add("§7That UUID was not found :(");
                    lastPlayer = null;
                }
            }
        });
    }

    private static void outputCVCStats(UUID uuid){
        results_.clear();
        results_.add("§cLoading...");

        final ArrayList<String>[] result = new ArrayList[]{new ArrayList<>()};

        MyCommands.HTTP_EXECUTOR.execute(() -> {
            try {
                JsonObject player = lastPlayer;
                JsonObject stats;
                try {
                    stats = (player.get("stats").getAsJsonObject());
                } catch (NullPointerException e){
                    outputNoStats(player, ButtonType.CVC);
                    return;
                }

                String rankPrefix = RankFomatter.getFormattedRank(player);
                String playerName = rankPrefix + (rankPrefix.equals("§7") ? "" : " ") + player.get("displayname").getAsString();

                JsonObject mcgo = stats.get("MCGO").getAsJsonObject();

                if (!hasStats(mcgo)){
                    outputNoStats(player, ButtonType.CVC);
                    return;
                }

                int game_wins = (mcgo.get("game_wins") != null ? mcgo.get("game_wins").getAsInt() : 0);
                int round_wins = (mcgo.get("round_wins") != null ? mcgo.get("round_wins").getAsInt() : 0);
                int game_wins_deathmatch = (mcgo.get("game_wins_deathmatch") != null ? mcgo.get("game_wins_deathmatch").getAsInt() : 0);
                int game_plays = (mcgo.get("game_plays") != null ? mcgo.get("game_plays").getAsInt() : 0);
                int game_plays_deathmatch = (mcgo.get("game_plays_deathmatch") != null ? mcgo.get("game_plays_deathmatch").getAsInt() : 0);
                int kills = (mcgo.get("kills") != null ? mcgo.get("kills").getAsInt() : 0);
                int kills_deathmatch = (mcgo.get("kills_deathmatch") != null ? mcgo.get("kills_deathmatch").getAsInt() : 0);

                result[0] = new ArrayList<>(Arrays.asList(new StringBuilder()
                        .append("§6§lCops & Crims Stats: " )
                        .append(playerName)
                        .append("\n\n"
                                + "§o§aGame Wins:§7§o " + String.format("%,d", game_wins) + "\n"
                                + "§o§aRound Wins:§7§o " + String.format("%,d", round_wins) + "\n"
                                + "§o§aGame Wins Deathmatch:§7§o " + String.format("%,d", game_wins_deathmatch) + "\n"
                                + "§o§eGame Plays:§7§o " + String.format("%,d", game_plays) + "\n"
                                + "§o§eGame Plays Deathmatch:§7§o " + String.format("%,d", game_plays_deathmatch) + "\n"
                                + "§o§dKills:§7§o " + String.format("%,d", kills) + "\n"
                                + "§o§dKills Deathmatch:§7§o " + String.format("%,d", kills_deathmatch) + "\n").toString().split("\n")
                        ));

                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
                results_.clear();
                results_.addAll(result[0]);
                changeFocus = true;
                setButtons(ButtonType.CVC);
            } catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    private static void outputSkyWarsStats(UUID uuid){
        results_.clear();
        results_.add("§cLoading...");

        final ArrayList<String>[] result = new ArrayList[]{new ArrayList<>()};

        MyCommands.HTTP_EXECUTOR.execute(() -> {
            try {
                JsonObject player = lastPlayer;
                JsonObject stats;
                try {
                    stats = (player.get("stats").getAsJsonObject());
                } catch (NullPointerException e){
                    outputNoStats(player, ButtonType.SKYWARS);
                    return;
                }

                String rankPrefix = RankFomatter.getFormattedRank(player);
                String playerName = rankPrefix + (rankPrefix.equals("§7") ? "" : " ") + player.get("displayname").getAsString();

                JsonObject sw = stats.get("SkyWars").getAsJsonObject();

                if (!hasStats(sw)){
                    outputNoStats(player, ButtonType.SKYWARS);
                    return;
                }

                int games_played_skywars = (sw.get("games_played_skywars") != null ? sw.get("games_played_skywars").getAsInt() : 0);
                int wins = (sw.get("wins") != null ? sw.get("wins").getAsInt() : 0);
                int deaths = (sw.get("deaths") != null ? sw.get("deaths").getAsInt() : 0);
                int kills = (sw.get("kills") != null ? sw.get("kills").getAsInt() : 0);

                result[0] = new ArrayList<>(Arrays.asList(new StringBuilder()
                .append("§6§lSky Wars Stats: " )
                        .append(playerName)
                        .append("\n\n"
                                + "§o§cLevel:§7§o " + sw.get("levelFormatted").getAsString() + "\n"
                                + "§o§eGames Played:§7§o " + String.format("%,d", games_played_skywars) + "\n"
                                + "§o§aWins:§7§o " +String.format("%,d", wins)+ "\n"
                                + "§o§dDeaths:§7§o " +String.format("%,d",  deaths) + "\n"
                                + "§o§dKills:§7§o " + String.format("%,d", kills)).toString().split("\n")
                ));
                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
                results_.clear();
                results_.addAll(result[0]);
                changeFocus = true;
                setButtons(ButtonType.SKYWARS);
            } catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    private static void outputBedwarsStats(UUID uuid){
        results_.clear();
        results_.add("§cLoading...");

        final ArrayList<String>[] result = new ArrayList[]{new ArrayList<>()};

        MyCommands.HTTP_EXECUTOR.execute(() -> {
            try {
                JsonObject player = lastPlayer;
                JsonObject stats;
                try {
                    stats = (player.get("stats").getAsJsonObject());
                } catch (NullPointerException e){
                    outputNoStats(player, ButtonType.BEDWARS);
                    return;
                }

                String rankPrefix = RankFomatter.getFormattedRank(player);
                String playerName = rankPrefix + (rankPrefix.equals("§7") ? "" : " ") + player.get("displayname").getAsString();

                JsonObject bw = stats.get("Bedwars").getAsJsonObject();

                if (!hasStats(bw)){
                    outputNoStats(player, ButtonType.BEDWARS);
                    return;
                }

                int games_played_bedwars_1 = (bw.get("games_played_bedwars_1") != null ? bw.get("games_played_bedwars_1").getAsInt() : 0);
                int wins_bedwars = (bw.get("games_played_bedwars_1") != null ? bw.get("games_played_bedwars_1").getAsInt() : 0);
                int beds_lost_bedwars = (bw.get("games_played_bedwars_1") != null ? bw.get("games_played_bedwars_1").getAsInt() : 0);
                int deaths_bedwars = (bw.get("games_played_bedwars_1") != null ? bw.get("games_played_bedwars_1").getAsInt() : 0);
                int kills_bedwars = (bw.get("games_played_bedwars_1") != null ? bw.get("games_played_bedwars_1").getAsInt() : 0);
                int final_kills_bedwars = (bw.get("games_played_bedwars_1") != null ? bw.get("games_played_bedwars_1").getAsInt() : 0);
                int level;
                try {
                    level = (player.get("achievements").getAsJsonObject().get("bedwars_level").getAsInt());
                } catch (NullPointerException e){
                    level = 0;
                }

                result[0] = new ArrayList<>(Arrays.asList(new StringBuilder()
                        .append("§6§lBed Wars Stats: ")
                        .append(playerName)
                        .append("\n\n"
                                + "§o§cLevel:§7§o " + String.format("%,d", level) + "\n"
                                + "§o§eGames Played:§7§o " + String.format("%,d",games_played_bedwars_1) + "\n"
                                + "§o§aWins:§7§o " + String.format("%,d", wins_bedwars) + "\n"
                                + "§o§dBeds Lost:§7§o " + String.format("%,d", beds_lost_bedwars) + "\n"
                                + "§o§dDeaths:§7§o " + String.format("%,d", deaths_bedwars) + "\n"
                                + "§o§dKills:§7§o " + String.format("%,d", kills_bedwars) + "\n"
                                + "§o§dFinal Kills:§7§o " + String.format("%,d", final_kills_bedwars) + "\n").toString().split("\n")
                ));
                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
                results_.clear();
                results_.addAll(result[0]);
                changeFocus = true;
                setButtons(ButtonType.BEDWARS);
            } catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    private static boolean hasStats(JsonObject game){
        return !game.isJsonNull();
    }

    private static void outputNoStats(JsonObject player, ButtonType game){
        String rankPrefix = RankFomatter.getFormattedRank(player);
        String playerName = rankPrefix + (rankPrefix.equals("§7") ? "" : " ") + player.get("displayname").getAsString();
        results_.clear();
        results_.addAll(new ArrayList<>(Arrays.asList(new StringBuilder()
                .append("§6§l" + game.getName() + " Stats: ")
                .append(playerName)
                .append("\n\n"
                        + "§7Player has no Stats : (").toString().split("\n")
        )));
        changeFocus = true;
        setButtons(game);
        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
    }

    public static void setButtons(ButtonType btnType){
        switch (btnType){
            case BEDWARS:
                btn1 = ButtonType.MAIN;
                btn2 = ButtonType.SKYWARS;
                btn3 = ButtonType.CVC;
                break;
            case SKYWARS:
                btn1 = ButtonType.BEDWARS;
                btn2 = ButtonType.MAIN;
                btn3 = ButtonType.CVC;
                break;
            case CVC:
                btn1 = ButtonType.BEDWARS;
                btn2 = ButtonType.SKYWARS;
                btn3 = ButtonType.MAIN;
                break;
            default:
                btn1 = ButtonType.BEDWARS;
                btn2 = ButtonType.SKYWARS;
                btn3 = ButtonType.CVC;
                break;
        }
    }

    private static void outputFromUUID(UUID uuid){
        results_.clear();
        AtomicReference<JsonObject> player = new AtomicReference<>(lastPlayer);
        if (getUUID() == null || !getUUID().equals(uuid) || lastPlayer == null) {
            results_.add("§cLoading...");
            lastPlayer = null;
        }
        final ArrayList<String>[] result = new ArrayList[]{new ArrayList<>()};

        MyCommands.HTTP_EXECUTOR.execute(() -> {
            try {
                if (getUUID() == null || !getUUID().equals(uuid) || player.get() == null) {
                    JsonObject jsonObj;
                    try {
                        jsonObj = APIRequests.getHypixelResponse(uuid);
                        player.set(jsonObj.getAsJsonObject("player"));
                    } catch (IOException e) {
                        results_.clear();
                        results_.add("§c§lToo Many Hypixel API Requests");
                        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                        return;
                    } catch (ClassCastException e){
                        results_.clear();
                        results_.add("§e§lPlayer Is Not In Hypixel's Database!");
                        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                        return;
                    }
                    if (jsonObj.get("player").isJsonNull()) {
                        results_.clear();
                        results_.add("§e§lPlayer Is Not In Hypixel's Database!");
                        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                        return;
                    }
                }

                String rankPrefix = RankFomatter.getFormattedRank(player.get());
                rankPrefix += (rankPrefix.equals("§7") ? "" : " ");
                String name = player.get().get("displayname").getAsString();
                String playerName = rankPrefix + name;

                JsonElement _playerLevel = player.get().get("networkExp");
                JsonElement _firstLogin = player.get().get("firstLogin");
                JsonElement _achievementPoints = player.get().get("achievementPoints");
                JsonElement _karma = player.get().get("karma");

                double playerLevel = (_playerLevel != null ? HypixelUtil.getExactLevel(_playerLevel.getAsDouble()) : 0);
                long firstLogin = (_playerLevel != null ? _firstLogin.getAsLong() : 0);
                int achievementPoints = (_playerLevel != null ? _achievementPoints.getAsInt() : 0);
                int karma = (_playerLevel != null ? _karma.getAsInt() : 0);

                int friends = APIRequests.getFriends(uuid);

                Date date = new Date(firstLogin);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String strDate = (date.getTime() == 0L ? "Never!" : dateFormat.format(date)) ;

                 result[0] = new ArrayList<>(Arrays.asList(new StringBuilder()
                         .append("§7§o(" + uuid.toString() + ")\n")
                         .append(playerName)
                         .append("\n\n"
                                 + "§eLevel:§7§o " + String.format("%.02f", playerLevel) + "\n"
                                 + "§eFirst Login:§7§o " + strDate + "\n"
                                 + "§dAchievement Points:§7§o " + String.format("%,d", achievementPoints) + "\n"
                                 + "§dKarma:§7§o " +  String.format("%,d",karma) + "\n"
                                 + "§aFriends:§7§o " + friends + "\n"
                         ).toString().split("\n")));
                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);
                results_.clear();
                for (String res : result[0])
                    results_.add(res);
                previousRes = results_;
                lastPlayer = player.get();
                previousPlayer = lastPlayer;
                changeFocus = true;
                setButtons(ButtonType.MAIN);
            } catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    public void render(int mouseX, int mouseY, float delta) {
        tfw.setVisible(true);
        tfw.setEditable(true);
        if (changeFocus){
            resize(MinecraftClient.getInstance(), this.width, this.height);
            changeFocus = false;
        }
        int x = this.width / 2 - 252 / 2;
        int y = this.height / 2 - ((252 - 25) * 9 / 16);
        this.renderBackground(x, y);
        int y_ = (y > 150 ? y + 25 : y);
        int y__ = 6;
        if (y < 10)
            y__ = 7;
        if (y > 50)
            y__ = 6;
        if (y > 200)
            y__ = -20;

        this.drawCenteredString(this.font, "§f" + this.title.asFormattedString(), this.width / 2, y_ + y__, 16777215);
        this.drawString(minecraft.textRenderer, "§7Enter a Username or UUID:",x + 15, y_ + y__ + 20,16777215);
        int c = 0;
        results = results_;
        for (String result : results){
            this.drawString(minecraft.textRenderer, result, x + 15,y + c + 70,16777215);
            c += 12;
        }
        ArrayList<AbstractButtonWidget> buttons_ = (ArrayList<AbstractButtonWidget>) this.buttons;
        for(int i = 0; i < buttons_.size(); ++i) {
            buttons_.get(i).render(mouseX, mouseY, delta);
        }
        if (lastPlayer != null) {
            GlStateManager.enableDepthTest();
            ItemStack item = new ItemStack(Items.PLAYER_HEAD, 1);
            CompoundTag tag = new CompoundTag();
            tag.putString("SkullOwner",lastPlayer.get("displayname").getAsString());
            item.setTag(tag);
            double scale = 3;
            GlStateManager.scaled(scale,scale,scale);
            GlStateManager.enableLighting();
            DiffuseLighting.enableForItems();
            if (!results.isEmpty() && lastPlayer != null)
                this.itemRenderer.renderGuiItem(this.minecraft.player, item, (int)((x + 180)/scale), (int)((y + 100)/scale));
            GlStateManager.scaled(1/scale, 1/scale, 1/scale);
        }

    }

    public void renderBackground(int x, int y){
        fill(x + 9 -2, y + 18, x + 9 + 234, y + 18 + 225, -0xbbbbbb);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableRescaleNormal();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        DiffuseLighting.disable();
        this.minecraft.getTextureManager().bindTexture(new Identifier("hypixel","textures/gui/plancke/window.png"));
        this.blit(x, y, 0, 0, 252, 252);
    }

    private static UUID getUUID(){
        if (lastPlayer == null)
            return null;
        return UUID.fromString(lastPlayer.get("uuid").getAsString().replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
        ));
    }

    enum ButtonType {
        MAIN("Main", (buttonWidget) -> {
            outputFromUUID(getUUID());
        }),
        BEDWARS("Bed Wars", (buttonWidget) -> {
            outputBedwarsStats(getUUID());
        }),
        SKYWARS("Sky Wars", (buttonWidget) -> {
            outputSkyWarsStats(getUUID());
        }),
        CVC("Cops & Crims", (buttonWidget) -> {
            outputCVCStats(getUUID());
        });

        private String name;
        private  ButtonWidget.PressAction onPress;

        ButtonType(String _name, ButtonWidget.PressAction _onPress){
            this.name = _name;
            this.onPress = _onPress;
        }

        public String getName() {
            return name;
        }

        public ButtonWidget.PressAction getOnPress() {
            return onPress;
        }
    }
}
