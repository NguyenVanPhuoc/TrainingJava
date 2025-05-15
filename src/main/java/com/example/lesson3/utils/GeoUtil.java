package com.example.lesson3.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeoUtil {

    // Thay thế JSONArray và JSONObject bằng JsonNode của Jackson
    public static double[] getLatLngFromAddress(String address) throws Exception {
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        String urlStr = "https://nominatim.openstreetmap.org/search?format=json&q=" + encodedAddress;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // Bắt buộc

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder json = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        reader.close();

        // Sử dụng Jackson để parse JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode results = objectMapper.readTree(json.toString());

        if (results.isEmpty()) {
            throw new Exception("Không tìm thấy tọa độ cho địa chỉ: " + address);
        }

        // Đọc lat, lon từ JsonNode
        JsonNode location = results.get(0);
        double lat = location.get("lat").asDouble();
        double lon = location.get("lon").asDouble();

        return new double[]{lat, lon};
    }

    // Tính toán khoảng cách giữa hai điểm
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }
}

