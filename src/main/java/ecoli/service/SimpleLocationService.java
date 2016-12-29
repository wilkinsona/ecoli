package ecoli.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import ecoli.model.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jonatan Ivanov
 */
@Service
public class SimpleLocationService implements LocationService {
    private final RestTemplate restTemplate;
    private final String locationServiceUrl;

    @Autowired
    public SimpleLocationService(
            RestTemplate restTemplate,
            @Value("${locationService.url}") String locationServiceUrl) {
        this.restTemplate = restTemplate;
        this.locationServiceUrl = locationServiceUrl;
    }

    @HystrixCommand
    @Override
    public LocationResponse getLocation(String ip) {
        LocationResponse response = restTemplate.getForObject(
                locationServiceUrl + "/{ip}",
                LocationResponse.class,
                ip
        );
        if (response.getCity() == null) {
            response = restTemplate.getForObject(locationServiceUrl, LocationResponse.class);
        }

        return response;
    }
}
