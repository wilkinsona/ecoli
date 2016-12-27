package ecoli.config;

import ecoli.service.LocationService;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jonatan Ivanov
 */
@Configuration
public class EcoliConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HealthIndicator locationServiceHealthIndicator(LocationService locationService) {
        return () -> {
            try {
                locationService.getLocation("127.0.0.1");
                return Health.up().build();
            }
            catch (Exception exception) {
                return Health.down().withException(exception).build();
            }
        };
    }
}
