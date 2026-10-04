package com.krishagni.catissueplus.rest.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	// operationId = <controller><Method>, e.g. workflowsUpdateTask, so ids are unique without _1/_2 suffixes
	@Bean
	public OperationCustomizer operationIdCustomizer() {
		return (operation, handlerMethod) -> {
			String ctrl = handlerMethod.getBeanType().getSimpleName().replaceAll("Controller$", "");
			String method = handlerMethod.getMethod().getName();
			if (ctrl.isEmpty()) {
				return operation;
			}

			operation.setOperationId(
				Character.toLowerCase(ctrl.charAt(0)) + ctrl.substring(1) +
				Character.toUpperCase(method.charAt(0)) + method.substring(1));
			return operation;
		};
	}
}
