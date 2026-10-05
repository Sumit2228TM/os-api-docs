package com.krishagni.catissueplus.rest.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;

@Configuration
public class OpenApiConfig {

	// operationId = <controller><Method>, e.g. workflowsUpdateTask
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

	// springdoc adds _1, _2 when the same id is used by several routes (e.g. one method mapped
	// to PUT and PATCH). Rename every member of such a group to <id><HttpMethod>.
	@Bean
	public OpenApiCustomizer uniqueOperationIdCustomizer() {
		return openApi -> {
			if (openApi.getPaths() == null) {
				return;
			}

			List<Op> ops = new ArrayList<>();
			Set<String> ids = new HashSet<>();
			openApi.getPaths().forEach((path, item) ->
				item.readOperationsMap().forEach((method, op) -> {
					ops.add(new Op(path, method.name(), op));
					if (op.getOperationId() != null) {
						ids.add(op.getOperationId());
					}
				}));

			Map<String, List<Op>> groups = new LinkedHashMap<>();
			for (Op o : ops) {
				String id = o.op.getOperationId();
				if (id == null) {
					continue;
				}

				String base = id.replaceAll("_\\d+$", "");
				if (!base.equals(id) && !ids.contains(base)) {
					base = id;
				}

				groups.computeIfAbsent(base, k -> new ArrayList<>()).add(o);
			}

			Set<String> taken = new HashSet<>();
			for (List<Op> g : groups.values()) {
				if (g.size() == 1) {
					taken.add(g.get(0).op.getOperationId());
				}
			}

			for (Map.Entry<String, List<Op>> e : groups.entrySet()) {
				if (e.getValue().size() == 1) {
					continue;
				}

				for (Op o : e.getValue()) {
					String candidate = e.getKey() + cap(o.method.toLowerCase());
					if (taken.contains(candidate)) {
						candidate += pathPart(o.path);
					}

					taken.add(candidate);
					o.op.setOperationId(candidate);
				}
			}
		};
	}

	// Documentation only: describes responses/requests that are generated as empty "{}" schemas.
	// Controller return types and the JSON sent at runtime are not touched.
	@Bean
	public OpenApiCustomizer schemaDocsCustomizer() {
		return openApi -> {
			// GET /specimens is declared List<?>; its elements are SpecimenInfo (SpecimenDetail for cprId queries)
			if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null &&
				openApi.getComponents().getSchemas().containsKey("SpecimenInfo")) {
				setResponse(find(openApi, "/specimens", PathItem.HttpMethod.GET),
					new ArraySchema().items(new Schema<>().$ref("#/components/schemas/SpecimenInfo")));
			}

			ObjectSchema uiStateOut = freeForm("User interface state: free-form key/value pairs saved by the UI.");
			uiStateOut.addProperty("authToken", new ObjectSchema().description("Auth token of the current session (added to the response)."));
			setResponse(find(openApi, "/users/current-user-ui-state", PathItem.HttpMethod.GET), uiStateOut);

			ObjectSchema uiStateIn = freeForm("User interface state: free-form key/value pairs saved by the UI.");
			Operation saveUiState = find(openApi, "/users/current-user-ui-state", PathItem.HttpMethod.PUT);
			setRequest(saveUiState, uiStateIn);
			setResponse(saveUiState, uiStateIn);

			ObjectSchema formId = new ObjectSchema();
			IntegerSchema id = new IntegerSchema();
			id.setFormat("int64");
			id.setDescription("Id of the saved form");
			formId.addProperty("id", id);
			Operation saveForm = find(openApi, "/forms/{id}", PathItem.HttpMethod.PUT);
			setRequest(saveForm, freeForm("Form definition properties."));
			setResponse(saveForm, formId);

			ObjectSchema formData = freeForm("Form data: field names (as defined by the form) mapped to their values.");
			Operation saveFormData = find(openApi, "/forms/{id}/data", PathItem.HttpMethod.PUT);
			setRequest(saveFormData, formData);
			setResponse(saveFormData, formData);
		};
	}

	private static Operation find(OpenAPI api, String path, PathItem.HttpMethod method) {
		if (api.getPaths() == null) {
			return null;
		}

		PathItem item = api.getPaths().get(path);
		return item == null ? null : item.readOperationsMap().get(method);
	}

	private static ObjectSchema freeForm(String description) {
		ObjectSchema s = new ObjectSchema();
		s.setDescription(description);
		s.setAdditionalProperties(true);
		return s;
	}

	private static void setResponse(Operation op, Schema<?> schema) {
		if (op == null || op.getResponses() == null) {
			return;
		}

		ApiResponse resp = op.getResponses().get("200");
		if (resp == null) {
			return;
		}

		if (resp.getContent() == null) {
			Content content = new Content();
			content.addMediaType("application/json", new MediaType().schema(schema));
			resp.setContent(content);
			return;
		}

		resp.getContent().values().forEach(mt -> mt.setSchema(schema));
	}

	private static void setRequest(Operation op, Schema<?> schema) {
		if (op == null || op.getRequestBody() == null || op.getRequestBody().getContent() == null) {
			return;
		}

		op.getRequestBody().getContent().values().forEach(mt -> mt.setSchema(schema));
	}

	// Documentation only: schemas generated as additionalProperties {} / items {} are rewritten as
	// explicit free-form schemas with a description.
	@Bean
	@SuppressWarnings({"rawtypes", "unchecked"})
	public OpenApiCustomizer freeFormSchemaCustomizer() {
		return openApi -> {
			if (openApi.getPaths() == null) {
				return;
			}

			openApi.getPaths().values().forEach(item -> item.readOperations().forEach(op -> {
				if (op.getRequestBody() != null && op.getRequestBody().getContent() != null) {
					op.getRequestBody().getContent().values().forEach(mt -> describeOpen(mt.getSchema()));
				}

				if (op.getResponses() != null) {
					op.getResponses().values().forEach(r -> {
						if (r.getContent() != null) {
							r.getContent().values().forEach(mt -> describeOpen(mt.getSchema()));
						}
					});
				}
			}));
		};
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void describeOpen(Schema s) {
		if (s == null || s.get$ref() != null) {
			return;
		}

		Object ap = s.getAdditionalProperties();
		if (ap instanceof Schema && isBlank((Schema) ap)) {
			s.setAdditionalProperties(Boolean.TRUE);
			if (s.getDescription() == null) {
				s.setDescription("Free-form JSON object: keys and values vary.");
			}
		}

		Schema items = s.getItems();
		if (items != null && items.get$ref() == null) {
			if (isBlank(items)) {
				items.setDescription("Any JSON value.");
			} else {
				describeOpen(items);
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static boolean isBlank(Schema a) {
		return a.get$ref() == null && a.getType() == null &&
			(a.getTypes() == null || a.getTypes().isEmpty()) &&
			a.getProperties() == null && a.getItems() == null && a.getEnum() == null &&
			a.getAllOf() == null && a.getAnyOf() == null && a.getOneOf() == null;
	}

	private static String cap(String s) {
		return s.isEmpty() ? s : Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}

	private static String pathPart(String path) {
		StringBuilder sb = new StringBuilder();
		for (String t : path.split("[^A-Za-z0-9]+")) {
			sb.append(cap(t));
		}

		return sb.toString();
	}

	private static class Op {
		final String path;
		final String method;
		final Operation op;

		Op(String path, String method, Operation op) {
			this.path = path;
			this.method = method;
			this.op = op;
		}
	}
}
