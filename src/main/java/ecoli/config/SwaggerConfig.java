package ecoli.config;

import com.google.common.base.Predicate;
import ecoli.controller.EcoliController;
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

import static com.google.common.base.Predicates.not;

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
                .select()
                    .apis(inControllerPackage())
                    .build();
    }

    private ApiInfo ecoliApiInfo() {
        return new ApiInfoBuilder()
                .title("E. coli REST API")
                .build();
    }

    @Bean
    public Docket actuatorDocket() {
        return new Docket(DocumentationType.SPRING_WEB)
                .groupName("Actuator")
                .apiInfo(actuatorApiInfo())
                .select()
                    .apis(not(inControllerPackage()))
                    .paths(not(errorController()))
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
        return EcoliController.class.getPackage().getName();
    }

    private Predicate<String> errorController() {
        return PathSelectors.regex("/error");
    }
}
