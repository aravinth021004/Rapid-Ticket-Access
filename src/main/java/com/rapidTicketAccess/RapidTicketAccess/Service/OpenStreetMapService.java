package com.rapidTicketAccess.RapidTicketAccess.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenStreetMapService {
    // GEOCODE_URL is used to get the latitude and longitude from location
    private static final String GEOCODE_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json";

    //REVERSE_GEOCODE_URL is used to get the location from latitude and longitude
    private static final String REVERSE_GEOCODE_URL = "https://nominatim.openstreetmap.org/reverse?lat=%s&lon=%s&format=json";

    //Used to make HTTP requests
    private final RestTemplate restTemplate;

    //Map the json with the java object
    private final ObjectMapper objectMapper;

    public OpenStreetMapService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    // Get latitude and longitude by location
    public String getLatLong(String place) {
        String url = String.format(GEOCODE_URL, place);
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonArray = objectMapper.readTree(response);
            if (jsonArray.isArray() && jsonArray.size() > 0) {
                JsonNode location = jsonArray.get(0);
                return location.get("lat").asText() + "," + location.get("lon").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Location not found!";
    }

    // Get the location by the latitude and longitude
    public String getPlace(double lat, double lon) {
        String url = String.format(REVERSE_GEOCODE_URL, lat, lon);
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonObject = objectMapper.readTree(response);
            return jsonObject.has("display_name") ? jsonObject.get("display_name").asText() : "Location not found!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Location not found!";
        }
    }


}
