package weather.com.bot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GreetingController {

    @GetMapping("/hello")
    public String greet() {
        // This string will be sent back as the HTTP response body.
        return "Hello, Don Spring Boot Weather Bot Challenge";
    }

    // NOTE: The @GetMapping("/") method was intentionally removed from this controller
    // to prevent a conflict with the @GetMapping("/") route already defined in WeatherController.
}


