package com.motorway.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.motorway.config.ApiConfig;
import com.motorway.enums.VisibilityLevel;
import com.motorway.model.VisibilityInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static WeatherData getWeatherForLocation(double lat, double lon) {
        try {
            String url = String.format("%s?lat=%f&lon=%f&appid=%s&units=metric",
                    ApiConfig.WEATHER_API_URL, lat, lon, ApiConfig.WEATHER_API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseWeatherResponse(response.body());
            } else {
                System.err.println("Weather API error: " + response.statusCode());
                return null;
            }

        } catch (Exception e) {
            System.err.println("Failed to fetch weather: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public static AirQualityData getAirQuality(double lat, double lon) {
        try {
            String url = String.format("%s?lat=%f&lon=%f&appid=%s",
                    ApiConfig.WEATHER_AIR_POLLUTION_URL, lat, lon, ApiConfig.WEATHER_API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseAirQualityResponse(response.body());
            }

        } catch (Exception e) {
            System.err.println("Failed to fetch air quality: " + e.getMessage());
        }
        return null;
    }


    public static VisibilityInfo getVisibilityInfo(double lat, double lon) {
        WeatherData weather = getWeatherForLocation(lat, lon);
        AirQualityData airQuality = getAirQuality(lat, lon);

        if (weather == null) {
            return new VisibilityInfo(1000, 0, 0);
        }


        int visibilityMeters = weather.visibility;
        VisibilityLevel level = calculateVisibilityLevel(visibilityMeters, weather.weatherCondition);

        double pm25 = airQuality != null ? airQuality.pm25 : 0;
        double pm10 = airQuality != null ? airQuality.pm10 : 0;

        return new VisibilityInfo(visibilityMeters, pm25, pm10);
    }

    private static VisibilityLevel calculateVisibilityLevel(int visibilityMeters, String condition) {

        if (condition.toLowerCase().contains("fog") ||
                condition.toLowerCase().contains("mist") ||
                condition.toLowerCase().contains("smoke")) {
            if (visibilityMeters < 100) return VisibilityLevel.HAZARDOUS;
            if (visibilityMeters < 500) return VisibilityLevel.POOR;
            return VisibilityLevel.MODERATE;
        }


        if (condition.toLowerCase().contains("rain") ||
                condition.toLowerCase().contains("snow")) {
            if (visibilityMeters < 200) return VisibilityLevel.POOR;
            if (visibilityMeters < 1000) return VisibilityLevel.MODERATE;
        }


        if (visibilityMeters < 100) return VisibilityLevel.HAZARDOUS;
        if (visibilityMeters < 500) return VisibilityLevel.POOR;
        if (visibilityMeters < 2000) return VisibilityLevel.MODERATE;
        if (visibilityMeters < 5000) return VisibilityLevel.GOOD;
        return VisibilityLevel.EXCELLENT;
    }

    private static WeatherData parseWeatherResponse(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        WeatherData data = new WeatherData();
        data.temperature = obj.getAsJsonObject("main").get("temp").getAsDouble();
        data.humidity = obj.getAsJsonObject("main").get("humidity").getAsInt();
        data.visibility = obj.has("visibility") ? obj.get("visibility").getAsInt() : 10000;

        if (obj.has("weather") && obj.getAsJsonArray("weather").size() > 0) {
            JsonObject weather = obj.getAsJsonArray("weather").get(0).getAsJsonObject();
            data.weatherCondition = weather.get("main").getAsString();
            data.weatherDescription = weather.get("description").getAsString();
        }

        if (obj.has("wind")) {
            data.windSpeed = obj.getAsJsonObject("wind").get("speed").getAsDouble();
        }

        return data;
    }

    private static AirQualityData parseAirQualityResponse(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        if (obj.has("list") && obj.getAsJsonArray("list").size() > 0) {
            JsonObject components = obj.getAsJsonArray("list").get(0)
                    .getAsJsonObject().getAsJsonObject("components");

            AirQualityData data = new AirQualityData();
            data.pm25 = components.get("pm2_5").getAsDouble();
            data.pm10 = components.get("pm10").getAsDouble();
            return data;
        }

        return null;
    }

    // Data classes
    public static class WeatherData {
        public double temperature;
        public int humidity;
        public int visibility;
        public String weatherCondition;
        public String weatherDescription;
        public double windSpeed;
    }

    public static class AirQualityData {
        public double pm25;
        public double pm10;
    }
}