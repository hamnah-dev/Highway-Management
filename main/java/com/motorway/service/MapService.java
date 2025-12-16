package com.motorway.service;

import javafx.scene.web.WebEngine;
import netscape.javascript.JSObject;

public class MapService {

    public static String getMapHtml() {
        return """
        <!doctype html>
        <html>
        <head>
            <meta charset="utf-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                html, body, #map { height: 100%; margin:0; padding:0; }
            </style>
        </head>
        <body>
        <div id="map"></div>
        <script>
            var map = L.map('map').setView([30.3753, 69.3451], 5);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 }).addTo(map);

            var selectedMarker = null;
            var selectedLat = null;
            var selectedLng = null;

            map.on('click', function(e) {
                var lat = e.latlng.lat;
                var lng = e.latlng.lng;

                if (selectedMarker) map.removeLayer(selectedMarker);
                selectedMarker = L.marker([lat, lng]).addTo(map)
                    .bindPopup("Selected Location").openPopup();

                selectedLat = lat;
                selectedLng = lng;
            });
        </script>
        </body>
        </html>
        """;
    }


    public static double[] getSelectedCoordinates(WebEngine engine) {
        Object latObj = engine.executeScript("selectedLat");
        Object lngObj = engine.executeScript("selectedLng");

        if (latObj instanceof Number && lngObj instanceof Number) {
            double lat = ((Number) latObj).doubleValue();
            double lng = ((Number) lngObj).doubleValue();
            return new double[]{lat, lng};
        }
        return null;
    }
}
