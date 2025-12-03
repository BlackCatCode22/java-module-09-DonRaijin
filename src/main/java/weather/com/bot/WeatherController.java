package weather.com.bot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * WeatherController is the main controller that handles requests related to the weather dashboard.
 * It uses the @Controller annotation because it returns a Thymeleaf template (weather.html).
 */
@Controller
public class WeatherController {

    // 1. Handles the request for the main dashboard (mapped to /weather)
    @GetMapping("/weather")
    public String getWeather(
            // Fetches the 'city' parameter from the URL (e.g., /weather?city=Fresno)
            @RequestParam(name = "city", required = false) String selectedCity,
            Model model
    ) {
        // Step 1: Add city names to the model for the dropdown menu
        List<String> cityNames = CityData.getAllCityNames();
        model.addAttribute("cityNames", cityNames);

        // If no city is selected yet, default to the first city in the list
        if (selectedCity == null || selectedCity.isEmpty()) {
            if (!cityNames.isEmpty()) {
                selectedCity = cityNames.get(0);
            }
        }

        if (selectedCity != null && !selectedCity.isEmpty()) {
            model.addAttribute("selectedCity", selectedCity);

            // Step 2: Get coordinates using the CityData helper class
            City city = CityData.getCityByName(selectedCity);

            if (city != null) {
                try {
                    // Step 3: Construct the Open-Meteo API URL
                    // We are now building the URL directly using simple string concatenation.
                    String apiUrl = "https://api.open-meteo.com/v1/forecast" +
                            "?latitude=" + city.getLatitude() +
                            "&longitude=" + city.getLongitude() +
                            "&current=temperature_2m,wind_speed_10m";

                    // Step 4: Call the API and get the JSON response
                    RestTemplate restTemplate = new RestTemplate();
                    String response = restTemplate.getForObject(apiUrl, String.class);

                    // Step 5: Parse the JSON response
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response);

                    // Extract the required values (in metric units)
                    double temp_c = root.path("current").path("temperature_2m").asDouble();
                    double wind_mps = root.path("current").path("wind_speed_10m").asDouble();

                    // --- Temperature Conversions ---
                    double temp_f = (temp_c * 1.8) + 32; // Celsius to Fahrenheit

                    // --- Wind Speed Conversions ---
                    double wind_kph = wind_mps * 3.6;    // m/s to km/h
                    double wind_mph = wind_mps * 2.237;  // m/s to mph

                    // Step 6: Add Results to the Model for use in weather.html
                    model.addAttribute("temperatureC", temp_c);
                    model.addAttribute("temperatureF", temp_f);
                    model.addAttribute("windKph", wind_kph);
                    model.addAttribute("windMph", wind_mph);

                } catch (Exception e) {
                    model.addAttribute("error", "API Error: Could not retrieve or parse weather data.");
                    System.err.println("API Call or JSON Parsing Failed:");
                    e.printStackTrace();
                }
            } else {
                model.addAttribute("error", "Error: Selected city not recognized.");
            }
        }

        // Step 7: Return the view (weather.html)
        return "weather";
    }
    }