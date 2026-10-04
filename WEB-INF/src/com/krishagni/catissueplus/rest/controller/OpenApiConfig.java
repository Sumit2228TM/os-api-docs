package com.krishagni.catissueplus.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.media.MediaType;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer defaultJsonResponseCustomizer() {
        return openApi -> {
            if (openApi.getPaths() == null) return;
            openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    if (operation.getResponses() != null) {
                        operation.getResponses().values().forEach(response -> {
                            if (response.getContent() != null && response.getContent().containsKey("*/*")) {
                                MediaType mediaType = response.getContent().remove("*/*");
                                response.getContent().put("application/json", mediaType);
                            }
                        });
                    }
                })
            );
        };
    }
}
