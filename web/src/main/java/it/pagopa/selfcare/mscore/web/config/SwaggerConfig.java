package it.pagopa.selfcare.mscore.web.config;

import com.fasterxml.classmate.TypeResolver;
import it.pagopa.selfcare.commons.web.model.Problem;
import it.pagopa.selfcare.commons.web.swagger.EmailAnnotationSwaggerPluginConfig;
import it.pagopa.selfcare.commons.web.swagger.ServerSwaggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * The Class SwaggerConfig.
 */
@Configuration
class SwaggerConfig {

    private static final String AUTH_SCHEMA_NAME = "bearerAuth";
    private static final Response INTERNAL_SERVER_ERROR_RESPONSE = new ResponseBuilder()
            .code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
            .description(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .representation(MediaType.APPLICATION_PROBLEM_JSON).apply(repBuilder ->
                    repBuilder.model(modelSpecBuilder ->
                            modelSpecBuilder.referenceModel(refModelSpecBuilder ->
                                    refModelSpecBuilder.key(modelKeyBuilder ->
                                            modelKeyBuilder.qualifiedModelName(qualifiedModelNameBuilder ->
                                                    qualifiedModelNameBuilder.namespace(Problem.class.getPackageName())
                                                            .name(Problem.class.getSimpleName()))))))
            .build();
    private static final Response BAD_REQUEST_RESPONSE = new ResponseBuilder()
            .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
            .description(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .representation(MediaType.APPLICATION_PROBLEM_JSON).apply(repBuilder ->
                    repBuilder.model(modelSpecBuilder ->
                            modelSpecBuilder.referenceModel(refModelSpecBuilder ->
                                    refModelSpecBuilder.key(modelKeyBuilder ->
                                            modelKeyBuilder.qualifiedModelName(qualifiedModelNameBuilder ->
                                                    qualifiedModelNameBuilder.namespace(Problem.class.getPackageName())
                                                            .name(Problem.class.getSimpleName()))))))
            .build();
    private static final Response UNAUTHORIZED_RESPONSE = new ResponseBuilder()
            .code(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
            .description(HttpStatus.UNAUTHORIZED.getReasonPhrase())
            .representation(MediaType.APPLICATION_PROBLEM_JSON).apply(repBuilder ->
                    repBuilder.model(modelSpecBuilder ->
                            modelSpecBuilder.referenceModel(refModelSpecBuilder ->
                                    refModelSpecBuilder.key(modelKeyBuilder ->
                                            modelKeyBuilder.qualifiedModelName(qualifiedModelNameBuilder ->
                                                    qualifiedModelNameBuilder.namespace(Problem.class.getPackageName())
                                                            .name(Problem.class.getSimpleName()))))))
            .build();
    private static final Response NOT_FOUND_RESPONSE = new ResponseBuilder()
            .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
            .description(HttpStatus.NOT_FOUND.getReasonPhrase())
            .representation(MediaType.APPLICATION_PROBLEM_JSON).apply(repBuilder ->
                    repBuilder.model(modelSpecBuilder ->
                            modelSpecBuilder.referenceModel(refModelSpecBuilder ->
                                    refModelSpecBuilder.key(modelKeyBuilder ->
                                            modelKeyBuilder.qualifiedModelName(qualifiedModelNameBuilder ->
                                                    qualifiedModelNameBuilder.namespace(Problem.class.getPackageName())
                                                            .name(Problem.class.getSimpleName()))))))
            .build();

    @Configuration
    @Profile("swaggerIT")
    @PropertySource("classpath:/swagger/swagger_it.properties")
    public static class itConfig {
    }

    @Configuration
    @Profile("swaggerEN")
    @PropertySource("classpath:/swagger/swagger_en.properties")
    public static class enConfig {
    }

    private final Environment environment;


    @Autowired
    SwaggerConfig(Environment environment) {
        this.environment = environment;
    }


    @Bean
    public Docket swaggerSpringPlugin(@Autowired TypeResolver typeResolver) {
        return (new Docket(DocumentationType.OAS_30))
                .apiInfo(new ApiInfoBuilder()
                        .title(environment.getProperty("swagger.title", environment.getProperty("spring.application.name")))
                        .description(environment.getProperty("swagger.description", "Api and Models"))
                        .version(environment.getProperty("swagger.version", environment.getProperty("spring.application.version")))
                        .build())
                .select().apis(RequestHandlerSelectors.basePackage("it.pagopa.selfcare.mscore.web.controller")).build()
                .tags(new Tag("name", environment.getProperty("swagger.name.api.description")))//TODO change Name
                .directModelSubstitute(LocalTime.class, String.class)
                .forCodeGeneration(true)
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, List.of(INTERNAL_SERVER_ERROR_RESPONSE, UNAUTHORIZED_RESPONSE, BAD_REQUEST_RESPONSE, NOT_FOUND_RESPONSE))
                .globalResponses(HttpMethod.DELETE, List.of(INTERNAL_SERVER_ERROR_RESPONSE, UNAUTHORIZED_RESPONSE, BAD_REQUEST_RESPONSE))
                .globalResponses(HttpMethod.POST, List.of(INTERNAL_SERVER_ERROR_RESPONSE, UNAUTHORIZED_RESPONSE, BAD_REQUEST_RESPONSE))
                .globalResponses(HttpMethod.PUT, List.of(INTERNAL_SERVER_ERROR_RESPONSE, UNAUTHORIZED_RESPONSE, BAD_REQUEST_RESPONSE))
                .globalResponses(HttpMethod.PATCH, List.of(INTERNAL_SERVER_ERROR_RESPONSE, UNAUTHORIZED_RESPONSE, BAD_REQUEST_RESPONSE))
                .additionalModels(typeResolver.resolve(Problem.class))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .build()))
                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme.JWT_BEARER_BUILDER
                        .name(AUTH_SCHEMA_NAME)
                        .description(environment.getProperty("swagger.security.schema.bearer.description"))
                        .build()));
    }


    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(AUTH_SCHEMA_NAME, authorizationScopes));
    }


    @Bean
    public EmailAnnotationSwaggerPluginConfig emailAnnotationPlugin() {
        return new EmailAnnotationSwaggerPluginConfig();
    }


    @Bean
    public ServerSwaggerConfig serverSwaggerConfiguration() {
        return new ServerSwaggerConfig();
    }

}
