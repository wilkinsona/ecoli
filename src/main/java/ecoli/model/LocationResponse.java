package ecoli.model;

import lombok.Getter;

/**
 * @author Jonatan Ivanov
 */
@Getter
public class LocationResponse {
    private String ip;
    private String hostname;
    private String city;
    private String region;
    private String country;
    private String loc;
    private String org;
    private String postal;
}
