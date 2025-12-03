package weather.com.bot;

public class City {
    private String name;
    private double latitude;
    private double longitude;

    // Constructor: This lets us create a city like new City("Fresno", 36.7, -119.7)
    public City(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters: These allow other parts of our code to read these values
    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

