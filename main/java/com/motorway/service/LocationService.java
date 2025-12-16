/*package com.motorway.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.motorway.model.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class LocationService {

    public static CompletableFuture<Location> getCurrentLocationAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("http://ip-api.com/json/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                JsonElement parsed = JsonParser.parseString(sb.toString());
                if (!parsed.isJsonObject()) {
                    return new Location(51.5074, -0.1278, "Default Location");
                }
                JsonObject obj = parsed.getAsJsonObject();

                double lat = obj.has("lat") && !obj.get("lat").isJsonNull() ? obj.get("lat").getAsDouble() : 51.5074;
                double lon = obj.has("lon") && !obj.get("lon").isJsonNull() ? obj.get("lon").getAsDouble() : -0.1278;
                String city = obj.has("city") && !obj.get("city").isJsonNull() ? obj.get("city").getAsString() : "Unknown";
                String country = obj.has("country") && !obj.get("country").isJsonNull() ? obj.get("country").getAsString() : "";

                return new Location(lat, lon, city + (country.isEmpty() ? "" : ", " + country));
            } catch (Exception e) {
                System.err.println("Error getting location: " + e.getMessage());
                return new Location(51.5074, -0.1278, "Default Location");
            }
        });
    }
}*/