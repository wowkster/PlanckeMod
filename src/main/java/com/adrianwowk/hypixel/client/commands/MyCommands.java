package com.adrianwowk.hypixel.client.commands;

import com.adrianwowk.hypixel.client.HypixelUtil;
import com.adrianwowk.hypixel.client.api.APIRequests;
import com.adrianwowk.hypixel.client.formatting.RankFomatter;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.cottonmc.clientcommands.ArgumentBuilders;
import io.github.cottonmc.clientcommands.ClientCommandPlugin;
import io.github.cottonmc.clientcommands.CottonClientCommandSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MyCommands implements ClientCommandPlugin {

    public static final Executor HTTP_EXECUTOR = Executors.newSingleThreadExecutor();
    public static final Executor MOJANG_EXECUTOR = Executors.newSingleThreadExecutor();

    @Override
    public void registerCommands(CommandDispatcher<CottonClientCommandSource> dispatcher) {
        dispatcher.register(ArgumentBuilders.literal("plancke")
                .executes(
                        source -> {
                            sendHelpMessage();
                            return 1;
                        }
                )
                .then(ArgumentBuilders.literal("help")
                        .executes(
                                source -> {
                                    sendHelpMessage();
                                    return 1;
                                }
                        )
                )
                .then(ArgumentBuilders.literal("player")
                        .executes(
                                source -> {
                                    sendHelpMessage();
                                    return 1;
                                }
                        )
                        .then(ArgumentBuilders.argument("name", StringArgumentType.string())
                                .executes(
                                        source -> {
                                            MOJANG_EXECUTOR.execute(() -> {
                                                UUID uuid = null;
                                                try {
                                                    uuid = APIRequests.playerNameToUUID(StringArgumentType.getString(source, "name"));
                                                    if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c§lYou Are Sending Too Many Requests!"));
                                                        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                                                        return;
                                                    }
                                                    outputFromUUID(uuid);
                                                } catch (Exception e) {
                                                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That name was not found or an error occurred :("));
                                                }
                                            });
                                            return 1;
                                        }
                                )
                                .then(ArgumentBuilders.literal("cvc")
                                        .executes(
                                                source -> {
                                                    MOJANG_EXECUTOR.execute(() -> {
                                                        UUID uuid = null;
                                                        try {
                                                            uuid = APIRequests.playerNameToUUID(StringArgumentType.getString(source, "name"));
                                                            if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                                                                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c§lYou Are Sending Too Many Requests!"));
                                                                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                                                                return;
                                                            }
                                                            outputFromUUID(uuid, GameType.CVC);
                                                        } catch (Exception e) {
                                                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That name was not found or an error occurred :("));
                                                        }
                                                    });
                                                    return 1;
                                                }
                                        )
                                )
                                .then(ArgumentBuilders.literal("bw")
                                        .executes(
                                                source -> {
                                                    MOJANG_EXECUTOR.execute(() -> {
                                                        UUID uuid = null;
                                                        try {
                                                            uuid = APIRequests.playerNameToUUID(StringArgumentType.getString(source, "name"));
                                                            if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                                                                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c§lYou Are Sending Too Many Requests!"));
                                                                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                                                                return;
                                                            }
                                                            outputFromUUID(uuid, GameType.BEDWARS);
                                                        } catch (Exception e) {
                                                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That name was not found or an error occurred :("));
                                                        }
                                                    });
                                                    return 1;
                                                }
                                        )
                                )
                                .then(ArgumentBuilders.literal("sw")
                                        .executes(
                                                source -> {
                                                    MOJANG_EXECUTOR.execute(() -> {
                                                        UUID uuid = null;
                                                        try {
                                                            uuid = APIRequests.playerNameToUUID(StringArgumentType.getString(source, "name"));
                                                            if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                                                                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c§lYou Are Sending Too Many Requests!"));
                                                                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                                                                return;
                                                            }
                                                            outputFromUUID(uuid, GameType.SKYWARS);
                                                        } catch (Exception e) {
                                                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That name was not found or an error occurred :("));
                                                        }
                                                    });
                                                    return 1;
                                                }
                                        )
                                )
                        )
                )
                .then(ArgumentBuilders.literal("uuid")
                        .executes(
                                source -> {
                                    sendHelpMessage();
                                    return 1;
                                }
                        )
                        .then(ArgumentBuilders.argument("uuid", StringArgumentType.string())
                                .executes(
                                        source -> {
                                            UUID uuid = null;
                                            try {
                                                uuid = UUID.fromString(StringArgumentType.getString(source, "uuid").replaceFirst(
                                                        "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                                ));
                                                outputFromUUID(uuid);
                                            } catch (Exception e) {
                                                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That UUID was invalid or an error occurred :("));
                                            }
                                            return 1;
                                        }
                                )
                                .then(ArgumentBuilders.literal("cvc")
                                        .executes(
                                                source -> {
                                                    UUID uuid;
                                                    try {
                                                        uuid = UUID.fromString(StringArgumentType.getString(source, "uuid").replaceFirst(
                                                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                                        ));
                                                        outputFromUUID(uuid, GameType.CVC);
                                                    } catch (Exception e) {
                                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That UUID was invalid or an error occurred :("));
                                                    }
                                                    return 1;
                                                }
                                        )
                                )
                                .then(ArgumentBuilders.literal("bw")
                                        .executes(
                                                source -> {
                                                    UUID uuid;
                                                    try {
                                                        uuid = UUID.fromString(StringArgumentType.getString(source, "uuid").replaceFirst(
                                                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                                        ));
                                                        outputFromUUID(uuid, GameType.BEDWARS);
                                                    } catch (Exception e) {
                                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§7That UUID was invalid or an error occurred :("));
                                                    }
                                                    return 1;
                                                }
                                        )
                                )
                                .then(ArgumentBuilders.literal("sw")
                                        .executes(
                                                source -> {
                                                    UUID uuid;
                                                    try {
                                                        uuid = UUID.fromString(StringArgumentType.getString(source, "uuid").replaceFirst(
                                                                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
                                                        ));
                                                        outputFromUUID(uuid, GameType.SKYWARS);
                                                    } catch (Exception e) {
                                                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("That UUID was invalid or an error occurred :("));
                                                    }
                                                    return 1;
                                                }
                                        )
                                )
                        )
                )
                .then(ArgumentBuilders.literal("current")
                        .executes(
                                source -> {
                                    MOJANG_EXECUTOR.execute(this::outputCurrent);
                                    return 1;
                                }
                        )
                )
                );
    }

    public void outputCurrent(){
        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§cLoading..."));
        ArrayList<String> nicks = new ArrayList<>();
        try {
            List<UUID> uuids = new ArrayList<>();
            ClientPlayNetworkHandler clientPlayNetworkHandler = MinecraftClient.getInstance().getNetworkHandler();

            assert clientPlayNetworkHandler != null;
            List<PlayerListEntry> list = Ordering.from((Comparator)(new EntryOrderComparator())).sortedCopy(clientPlayNetworkHandler.getPlayerList());
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
            HTTP_EXECUTOR.execute(() -> {

                Text text = new LiteralText("§f============================================================\n\n"
                        + "§6§lPlayers in your lobby:\n\n");

                for (UUID uuid : uuids){

                    if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c§lYou Are Sending Too Many Requests!"));
                        MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                        return;
                    }

                    JsonObject jsonObj = null;
                    try {
                        try {
                            jsonObj = APIRequests.getHypixelResponse(uuid);
                        } catch (IOException e){
                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§lToo Many Hypixel API Requests"));
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

                        text.append("§8 " + (uuids.indexOf(uuid) + 1) + ". " );
                        text.append(getCommandText(playerName,
                                playerName + "\n§7Hypixel Level: §6" + String.format("%.02f", playerLevel) +
                                        "\n§7Achievement Points: §e" + achievementPoints + "\n\n§eClick to view §b" + playerBaseName + "§e's stats",
                                "/whois uuid " + uuid.toString()));

                        text.append("  §8§o(" + String.format("%.02f", playerLevel) + ")");
                        text.append("\n");

                    } catch (Exception e) {
                        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                        for (StackTraceElement ste : e.getStackTrace()){
                            MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                        }
                        e.printStackTrace();
                    }
                }
                text.append("\n§6§lNicks:\n");
                for (String name : nicks){
                    text.append("§8 - ");
                    text.append(getCommandText("§7" + name,
                            "This player might be nicked.",
                            ""));
                    text.append("\n");
                }
                text.append("\n§f============================================================");

                MinecraftClient.getInstance().player.sendMessage(text);
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
    }

    public void outputFromUUID(UUID uuid, GameType gt) {
        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§cLoading..."));
        HTTP_EXECUTOR.execute(() -> {
            try {
                JsonObject jsonObj;
                try {
                    jsonObj = APIRequests.getHypixelResponse(uuid);
                } catch (IOException e){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§lToo Many Hypixel API Requests"));
                    MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                    return;
                }
                JsonObject player = jsonObj.getAsJsonObject("player");
                JsonObject stats = player.get("stats").getAsJsonObject();


                String rankPrefix = RankFomatter.getFormattedRank(player);
                String playerName = rankPrefix + (rankPrefix.equals("§7") ? "" : " ") + player.get("displayname").getAsString();

                if (gt == GameType.CVC) {
                    JsonObject mcgo = stats.get("MCGO").getAsJsonObject();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText(
                                    "§f==========================================================\n\n"
                                    + "§6§lCops and Crims Stats: " )
                            .append(getCommandText(playerName, "Click to view player stats", "/whois uuid " + uuid.toString()))
                            .append(new LiteralText( "\n\n"
                                    + "§o§aGame Wins:§7§o " + mcgo.get("game_wins") + "\n"
                                    + "§o§aRound Wins:§7§o " + mcgo.get("round_wins") + "\n"
                                    + "§o§aGame Wins Deathmatch:§7§o " + mcgo.get("game_wins_deathmatch") + "\n"
                                    + "§o§eGame Plays:§7§o " + mcgo.get("game_plays") + "\n"
                                    + "§o§eGame Plays Deathmatch:§7§o " + mcgo.get("game_plays_deathmatch") + "\n"
                                    + "§o§dKills:§7§o " + mcgo.get("kills") + "\n"
                                    + "§o§dKills Deathmatch:§7§o " + mcgo.get("kills_deathmatch") + "\n"
                                    + "\n§f==========================================================")
                    ));
                } else if (gt == GameType.BEDWARS) {
                    JsonObject bw = stats.get("Bedwars").getAsJsonObject();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText(
                            "§f==========================================================\n\n"
                                    + "§6§lBed Wars Stats: ")
                            .append(getCommandText(playerName, "Click to view player stats", "/whois uuid " + uuid.toString()))
                            .append(new LiteralText( "\n\n"
                                    + "§o§eGames Played:§7§o " + bw.get("games_played_bedwars_1") + "\n"
                                    + "§o§aWins:§7§o " + bw.get("wins_bedwars") + "\n"
                                    + "§o§dBeds Lost:§7§o " + bw.get("beds_lost_bedwars") + "\n"
                                    + "§o§dDeaths:§7§o " + bw.get("deaths_bedwars") + "\n"
                                    + "§o§dKills:§7§o " + bw.get("kills_bedwars") + "\n"
                                    + "§o§dFinal Kills:§7§o " + bw.get("final_kills_bedwars") + "\n"
                                    + "\n§f==========================================================")
                    ));
                } else if (gt == GameType.SKYWARS) {
                    JsonObject sw = stats.get("SkyWars").getAsJsonObject();
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText(
                           "§f==========================================================\n\n"
                                    + "§6§lSky Wars Stats: " )
                            .append(getCommandText(playerName, "Click to view player stats", "/whois uuid " + uuid.toString()))
                            .append(new LiteralText("\n\n"
                                    + "§o§eGames Played:§7§o " + sw.get("games_played_skywars") + "\n"
                                    + "§o§aWins:§7§o " + sw.get("wins") + "\n"
                                    + "§o§dLevel:§7§o " + sw.get("levelFormatted").getAsString() + "\n"
                                    + "§o§dDeaths:§7§o " + sw.get("deaths") + "\n"
                                    + "§o§dKills:§7§o " + sw.get("kills") + "\n"
                                    + "\n§f==========================================================")

                    ));
                }
                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);

            } catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    public void outputFromUUID(UUID uuid) {
        MinecraftClient.getInstance().player.sendMessage(new LiteralText("§cLoading..."));
        HTTP_EXECUTOR.execute(() -> {
            try {
                JsonObject jsonObj;
                try {
                    jsonObj = APIRequests.getHypixelResponse(uuid);
                } catch (IOException e){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§lToo Many Hypixel API Requests"));
                    MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                    return;
                }
                if (jsonObj.get("player").isJsonNull()) {
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§9§lAn Error Occurred"));
                    MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:block.anvil.use")), 0.8f, 1f);
                    return;
                }
                JsonObject player = jsonObj.getAsJsonObject("player");


                String rankPrefix = RankFomatter.getFormattedRank(player);
                rankPrefix += (rankPrefix.equals("§7") ? "" : " ");
                String name = player.get("displayname").getAsString();
                Text playerName = getClickableText(rankPrefix + name, name);

                JsonElement _playerLevel = player.get("networkExp");
                JsonElement _firstLogin = player.get("firstLogin");
                JsonElement _achievementPoints = player.get("achievementPoints");
                JsonElement _karma = player.get("karma");

                double playerLevel = (_playerLevel != null ? HypixelUtil.getExactLevel(_playerLevel.getAsDouble()) : 0);
                long firstLogin = (_playerLevel != null ? _firstLogin.getAsLong() : 0);
                int achievementPoints = (_playerLevel != null ? _achievementPoints.getAsInt() : 0);
                int karma = (_playerLevel != null ? _karma.getAsInt() : 0);

                int friends = APIRequests.getFriends(uuid);

                Date date = new Date(firstLogin);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                String strDate = dateFormat.format(date);

                MinecraftClient.getInstance().player.sendMessage(new LiteralText(
                    "§f============================================================\n\n"
                            + "§6§lWho Is ")
                    .append(playerName)
                    .append(new LiteralText(" "))
                    .append(getClickableText("§7§o(" + uuid.toString() + ")", uuid.toString()))
                    .append(new LiteralText(
                "\n\n"
                        + "§eLevel:§7§o " + String.format("%.02f", playerLevel) + "\n"
                        + "§eFirst Login:§7§o " + strDate + "\n"
                        + "§dAchievement Points:§7§o " + achievementPoints + "\n"
                        + "§dKarma:§7§o " + karma + "\n"
                        + "§aFriends:§7§o " + friends + "\n"
                    )
                    .append(new LiteralText("\n§b§lStats: "))
                    .append(getCommandText("§f[Bedwars]", "View Bedwars Stats", "/whois uuid " + uuid.toString() + " bw"))
                            .append(new LiteralText(" "))
                    .append(getCommandText("§f[SkyWars]", "View SkyWars Stats", "/whois uuid " + uuid.toString() + " sw"))
                            .append(new LiteralText(" "))
                    .append(getCommandText("§f[CvC]", "View Cops and Crims Stats", "/whois uuid " + uuid.toString() + " cvc"))
                            .append(new LiteralText(" "))
                    .append(new LiteralText("\n§b§lQuick Links: "))
                    .append(getClickableText("§6[Friend]", "/f " + name))
                    .append(new LiteralText(" "))
                    .append(getClickableText("§a[Party]", "/p invite " + name))
                    .append(new LiteralText(" "))
                    .append(getClickableText("§9[Message]", "/msg " + name + " "))
                    .append(new LiteralText(" "))
                    .append(getClickableText("§d[Stats]", "/stats " + name))
                    .append(new LiteralText(" "))
                    .append(getClickableText("§c[Report]", "/wdr " + name + " "))
                    .append(new LiteralText("\n§f============================================================"))
                ));
                MinecraftClient.getInstance().player.playSound(new SoundEvent(Identifier.tryParse("minecraft:entity.arrow.hit_player")), 0.8f, 1f);

            } catch (Exception e) {
                MinecraftClient.getInstance().player.sendMessage(new LiteralText("§e§o" + e.toString()));
                for (StackTraceElement ste : e.getStackTrace()){
                    MinecraftClient.getInstance().player.sendMessage(new LiteralText("§c" + ste.toString()));
                }
                e.printStackTrace();
            }
        });
    }

    public Text getClickableText(String str, String suggestion){
        return new LiteralText(str)
                .styled(s ->
                        s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText("Click to copy into chat")))
                                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestion)));
    }

    public Text getCommandText(String str, String hover, String command){
        return new LiteralText(str)
                .styled(s ->
                        s.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new LiteralText(hover)))
                                .setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }

    public void sendHelpMessage() {
        MinecraftClient.getInstance().player.sendMessage(new LiteralText(
                new StringBuilder()
                        .append("§7=====================================================\n\n")
                        .append("§b§lPlancke Command\n\n")
                        .append("§o§6/plancke help§7§o - Show all subcommands\n")
                        .append("§o§6/plancke uuid <uuid>§7§o - Show hypixel stats for UUID\n")
                        .append("§o§6/plancke uuid <uuid> <game>§7§o - Show game stats for UUID\n")
                        .append("§o§6/plancke player <username>§7§o - Show hypixel stats for Player\n")
                        .append("§o§6/plancke player <username> <game>§7§o - Show game stats for Player\n")
                        .append("§o§6/plancke current§7§o - Shows players in your current game or lobby\n")
                        .append("\n§7=====================================================")
                        .toString()
        ));
    }

    enum GameType {
        CVC, BEDWARS, SKYWARS
    }

    @Environment(EnvType.CLIENT)
    public static class EntryOrderComparator implements Comparator<PlayerListEntry> {
        public EntryOrderComparator() {
        }

        public int compare(PlayerListEntry playerListEntry, PlayerListEntry playerListEntry2) {
            Team team = playerListEntry.getScoreboardTeam();
            Team team2 = playerListEntry2.getScoreboardTeam();
            return ComparisonChain.start().compareTrueFirst(playerListEntry.getGameMode() != GameMode.SPECTATOR, playerListEntry2.getGameMode() != GameMode.SPECTATOR).compare((Comparable)(team != null ? team.getName() : ""), (Comparable)(team2 != null ? team2.getName() : "")).compare(playerListEntry.getProfile().getName(), playerListEntry2.getProfile().getName(), String::compareToIgnoreCase).result();
        }
    }

}
