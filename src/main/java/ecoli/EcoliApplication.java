package ecoli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author Jonatan Ivanov
 */
@EnableEurekaClient
@EnableCircuitBreaker
@SpringBootApplication
public class EcoliApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcoliApplication.class, args);
    }
}
