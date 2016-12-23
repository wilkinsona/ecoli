package ecoli.config;

import com.google.common.base.Predicate;
import ecoli.controller.EcoliRestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Paths;

/**
 * @author Jonatan Ivanov
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket ecoliDocket() {
        return new Docket(DocumentationType.SPRING_WEB)
                .groupName("E. coli")
                .apiInfo(ecoliApiInfo())
                .select().apis(inControllerPackage())
                .build();
    }

    private ApiInfo ecoliApiInfo() {
        return new ApiInfoBuilder()
                .title("E. coli REST API")
                .build();
    }

    @Bean
    public Docket actuatorDocket(@Value("${management.context-path}") String managementContextPath) {
        return new Docket(DocumentationType.SPRING_WEB)
                .groupName("Actuator")
                .apiInfo(actuatorApiInfo())
                .select().paths(managementPath(managementContextPath))
                .build();
    }

    private ApiInfo actuatorApiInfo() {
        return new ApiInfoBuilder()
                .title("Actuator REST API")
                .build();
    }

    private Predicate<RequestHandler> inControllerPackage() {
        return RequestHandlerSelectors.basePackage(controllerPackageName());
    }

    private String controllerPackageName() {
        return EcoliRestController.class.getPackage().getName();
    }

    private Predicate<String> managementPath(String managementContextPath) {
        return PathSelectors.regex(managementPathRegex(managementContextPath));
    }

    private String managementPathRegex(String managementContextPath) {
        return Paths.get(managementContextPath, ".*").toString();
    }
}
