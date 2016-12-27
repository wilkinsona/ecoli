package ecoli.service;

import ecoli.model.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jonatan Ivanov
 */
@Service
public class SimpleLocationService implements LocationService {
    private static String BASE_URL = "http://ipinfo.io";

    private RestTemplate restTemplate;

    @Autowired
    public SimpleLocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public LocationResponse getLocation(String ip) {
        LocationResponse response = restTemplate.getForObject(BASE_URL + "/{ip}", LocationResponse.class, ip);

        if (response.getCity() == null) {
            response = restTemplate.getForObject(BASE_URL, LocationResponse.class);
        }

        return response;
    }
}
