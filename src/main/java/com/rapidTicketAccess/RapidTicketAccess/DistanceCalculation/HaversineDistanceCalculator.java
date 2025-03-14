package com.rapidTicketAccess.RapidTicketAccess.DistanceCalculation;

import org.springframework.stereotype.Service;

@Service
public class HaversineDistanceCalculator {

    private static final double EARTH_RADIUS_KM = 6371; // Radius of the Earth in km

    // Calculating the distance by taking the latitude and longitude of source and destination
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // Distance in kilometers
    }
}
