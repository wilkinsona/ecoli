package ecoli.service;

import ecoli.model.LocationResponse;

/**
 * @author Jonatan Ivanov
 */
public interface LocationService {
    LocationResponse getLocation(String ip);
}
