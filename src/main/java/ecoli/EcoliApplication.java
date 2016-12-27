package ecoli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * @author Jonatan Ivanov
 */
@EnableCircuitBreaker
@SpringBootApplication
public class EcoliApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcoliApplication.class, args);
    }
}
