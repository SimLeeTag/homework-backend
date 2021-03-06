package com.simleetag.homework.api.utils.mockmvc;

import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.ApplicationContext;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.ContentModifyingOperationPreprocessor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.AbstractMockMvcBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

import capital.scalable.restdocs.AutoDocumentation;

import static capital.scalable.restdocs.SnippetRegistry.*;
import static capital.scalable.restdocs.jackson.JacksonResultHandlers.prepareJackson;
import static capital.scalable.restdocs.response.ResponseModifyingPreprocessors.limitJsonArrayLength;
import static capital.scalable.restdocs.response.ResponseModifyingPreprocessors.replaceBinaryContent;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public class RestDocsMockMvcFactory {

    private static final String PUBLIC_AUTHORIZATION = "Resource is public.";

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final Snippet[] SUCCESS_SNIPPETS = new Snippet[]{
            HttpDocumentation.httpRequest(),
            HttpDocumentation.httpResponse(),
            AutoDocumentation.requestFields(),
            AutoDocumentation.responseFields(),
            AutoDocumentation.pathParameters(),
            AutoDocumentation.requestParameters(),
            AutoDocumentation.description(),
            AutoDocumentation.methodAndPath(),
            AutoDocumentation.modelAttribute(Arrays.asList(
                    new PageableHandlerMethodArgumentResolver(),
                    new ModelAttributeMethodProcessor(false))),
            AutoDocumentation.sectionBuilder().snippetNames(
                    AUTO_AUTHORIZATION,
                    AUTO_PATH_PARAMETERS,
                    AUTO_MODELATTRIBUTE,
                    AUTO_REQUEST_PARAMETERS,
                    AUTO_REQUEST_FIELDS,
                    AUTO_RESPONSE_FIELDS,
                    HTTP_REQUEST,
                    HTTP_RESPONSE
            ).skipEmpty(true).build(),
            AutoDocumentation.authorization(PUBLIC_AUTHORIZATION)};

    private static final Snippet[] FAIL_SNIPPETS = new Snippet[]{HttpDocumentation.httpResponse()};

    public static WebMockMvc successRestDocsMockMvc(RestDocumentationContextProvider provider, ApplicationContext applicationContext) {
        return restDocsMockMvc(provider, SUCCESS_SNIPPETS, applicationContext);
    }

    public static WebMockMvc failRestDocsMockMvc(RestDocumentationContextProvider provider, ApplicationContext applicationContext) {
        return restDocsMockMvc(provider, FAIL_SNIPPETS, applicationContext);
    }

    public static RestDocsMockMvc restDocsMockMvc(RestDocumentationContextProvider provider, Snippet[] snippets, ApplicationContext applicationContext) {
        return new RestDocsMockMvc(createMockMvc(provider, snippets, applicationContext));
    }

    public static WebMockMvc successRestDocsMockMvc(RestDocumentationContextProvider provider, WebApplicationContext wac) {
        return restDocsMockMvc(provider, SUCCESS_SNIPPETS, wac);
    }

    public static WebMockMvc failRestDocsMockMvc(RestDocumentationContextProvider provider, WebApplicationContext wac) {
        return restDocsMockMvc(provider, FAIL_SNIPPETS, wac);
    }

    public static RestDocsMockMvc restDocsMockMvc(RestDocumentationContextProvider provider, Snippet[] snippets, WebApplicationContext wac) {
        return new RestDocsMockMvc(createMockMvc(provider, snippets, wac));
    }

    private static MockMvc createMockMvc(RestDocumentationContextProvider provider, Snippet[] snippets, ApplicationContext applicationContext) {
        return mockMvc(provider, snippets, AbstractWebMockMvc.standaloneMockMvcBuilder(applicationContext));
    }

    private static MockMvc createMockMvc(RestDocumentationContextProvider provider, Snippet[] snippets, WebApplicationContext wac) {
        return mockMvc(provider, snippets, AbstractWebMockMvc.defaultMockMvcBuilder(wac));

    }

    private static MockMvc mockMvc(RestDocumentationContextProvider provider, Snippet[] snippets, AbstractMockMvcBuilder builder) {
        return builder.alwaysDo(prepareJackson(OBJECT_MAPPER))
                      .alwaysDo(restDocumentation())
                      .apply(documentationConfiguration(provider)
                              .uris()
                              .withScheme("http")
                              .withHost("localhost")
                              .withPort(8080)
                              .and()
                              .snippets()
                              .withDefaults(snippets))
                      .build();
    }

    private static RestDocumentationResultHandler restDocumentation() {
        final ContentModifyingOperationPreprocessor contentPreprocessor
                = new ContentModifyingOperationPreprocessor(new PrettyPrintingUtils());

        return MockMvcRestDocumentation
                .document("{class-name}/{method-name}",
                        preprocessRequest(

                                // RestDocs ????????? ?????? ?????? ??? Request ??? Response ??? ???????????? ??????
                                contentPreprocessor,

                                // ?????? ????????? ????????? ???????????? ??????
                                removeHeaders("Host", "Content-Length")
                        ),
                        preprocessResponse(
                                replaceBinaryContent(),
                                limitJsonArrayLength(OBJECT_MAPPER),
                                contentPreprocessor,
                                removeHeaders("Content-Length")
                        )
                );

    }

    private RestDocsMockMvcFactory() {}
}
