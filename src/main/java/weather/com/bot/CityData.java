// CityData.java (in package weather.com.bot)
package weather.com.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CityData {

    // Uses a Map for fast lookup
    private static final Map<String, City> CITIES_MAP = createCitiesMap();

    private static Map<String, City> createCitiesMap() {
        List<City> cities = new ArrayList<>();
        cities.add(new City("Fresno", 36.7378, -119.7871));
        cities.add(new City("Seoul", 37.566, 126.9784));
        cities.add(new City("Tokyo", 35.6895, 139.6917));
        cities.add(new City("Zakynthos", 37.7802, 20.8956));

        return cities.stream()
                .collect(Collectors.toMap(City::getName, Function.identity()));
    }

    public static City getCityByName(String name) {
        return CITIES_MAP.get(name);
    }

    public static List<String> getAllCityNames() {
        return new ArrayList<>(CITIES_MAP.keySet());
    }
}