package com.adrianwowk.hypixel.client.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class APIRequests {

    public static final String API_KEY = "f9350ddc-60f0-4cd0-bffe-34583c828cf9";

    public static JsonObject getHypixelResponse(UUID uuid) throws Exception{
        if (uuid == null)
            return null;
        URL url = new URL("https://api.hypixel.net/player?key=" + API_KEY + "&uuid=" + uuid.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode = con.getResponseCode();
        // Reading response from input Stream
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String output;
        StringBuilder response = new StringBuilder();

        while ((output = in.readLine()) != null) {
            response.append(output);
        }
        in.close();

        String res = response.toString();

        JsonParser parser =  new JsonParser();
        JsonElement jsonElement = parser.parse(res);
        return jsonElement.getAsJsonObject();
    }

    public static UUID playerNameToUUID(String username) throws Exception{
        URL url = new URL("https://api.mojang.com/profiles/minecraft");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        String json = "[\"" + username + "\",\"nonExistingPlayer\"]";
        byte[] out = json.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        InputStream is;
        try {
            is = http.getInputStream();
        } catch (Exception e){
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
        int i;
        String response = "";
        while((i = is.read())!=-1)
            response += (char)i;
        try {
            return UUID.fromString(response.split("\"")[3].replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));
        } catch (ArrayIndexOutOfBoundsException ignored){

        }
        return null;
    }

    public static int getFriends(UUID uuid) {
        try {
            URL url = new URL("https://api.hypixel.net/friends?key=" + API_KEY + "&uuid=" + uuid.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
//            System.out.println("Sending get request : " + url);
//            System.out.println("Response code : " + responseCode);
            // Reading response from input Stream
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String output;
            StringBuilder response = new StringBuilder();

            while ((output = in.readLine()) != null) {
                response.append(output);
            }
            in.close();

            String res = response.toString();

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(res);
            return jsonElement.getAsJsonObject().getAsJsonArray("records").size();
        } catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
}
