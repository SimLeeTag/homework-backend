package com.simleetag.homework.api.config;

import java.util.Arrays;
import java.util.List;

import com.simleetag.homework.api.common.IdentifierHeader;
import com.simleetag.homework.api.common.Invitation;
import com.simleetag.homework.api.common.Login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OperationCustomizer;

@Configuration
public class SwaggerConfig {

    private static void hideParameter(Operation operation, String parameterName) {
        final List<Parameter> parameters = operation.getParameters();
        final Parameter homeIdParameter = parameters.stream()
                                                    .filter(parameter -> parameterName.equals(parameter.getName()))
                                                    .findAny()
                                                    .get();

        parameters.remove(homeIdParameter);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                             .group("API")
                             .packagesToScan("com.simleetag.homework.api.domain")
                             .pathsToMatch("/**")
                             .pathsToExclude(
                                     "/maintenance/**"
                             )
                             .addOperationCustomizer(userIdOperationCustomizer())
                             .addOperationCustomizer(homeIdOperationCustomizer())
                             .build();
    }

    private OperationCustomizer userIdOperationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            /** @Login 어노테이션을 파라미터로 가진 메소드들은 필수적으로 x-user-id 헤더를 작성하도록 한다. */
            final boolean loginRequired = Arrays.stream(handlerMethod.getMethodParameters())
                                                .anyMatch(method -> method.hasParameterAnnotation(Login.class));

            if (!loginRequired) {
                return operation;
            }

            Parameter userIdHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name(IdentifierHeader.USER.getKey())
                    .description("사용자 ID를 담고 있는 JWT")
                    .required(true);

            operation.addParametersItem(userIdHeader);

            /**
             * 사용자가 UserID 를 전달할 수 없도록 한다.
             * UserID는 JWT를 디코딩하여 얻어야 한다.
             */
            hideParameter(operation, "userId");
            return operation;
        };
    }

    private OperationCustomizer homeIdOperationCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {

            /** @Invitation 어노테이션을 파라미터로 가진 메소드들은 필수적으로 x-home-id 헤더를 작성하도록 한다. */
            final boolean loginRequired = Arrays.stream(handlerMethod.getMethodParameters())
                                                .anyMatch(method -> method.hasParameterAnnotation(Invitation.class));

            if (!loginRequired) {
                return operation;
            }

            Parameter homeIdHeader = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema())
                    .name(IdentifierHeader.HOME.getKey())
                    .description("집(HOME) ID를 담고 있는 JWT")
                    .required(true);

            operation.addParametersItem(homeIdHeader);

            /**
             * 사용자가 HomeID 를 매개변수로 전달할 수 없도록 한다.
             * HomeID는 JWT를 디코딩하여 얻어야 한다.
             */
            hideParameter(operation, "homeId");
            return operation;
        };
    }

    @Bean
    public GroupedOpenApi maintenance() {
        return GroupedOpenApi.builder()
                             .group("Maintenance")
                             .packagesToScan("com.simleetag.homework.api.domain")
                             .pathsToMatch("/maintenance/**")
                             .build();
    }
}
