package msmgw.heulgkkom.service.parse;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import msmgw.heulgkkom.model.ApiDto;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import static msmgw.heulgkkom.model.constant.HttpMethodEnum.*;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.TRACE;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Component
public class OpenApiSpecParser {


    public List<ApiDto> getPathAndParameter(OpenAPI openAPI) {
        Paths paths = openAPI.getPaths();

        Set<Map.Entry<String, PathItem>> entries = paths.entrySet();

        return entries.stream()
                .flatMap(e -> {
                    String k = e.getKey();
                    PathItem v = e.getValue();
                    List<ApiDto> group = new ArrayList<>();

                    group.add(printOperation(openAPI, v.getHead(), HEAD, k));
                    group.add(printOperation(openAPI, v.getGet(), GET, k));
                    group.add(printOperation(openAPI, v.getPost(), POST, k));
                    group.add(printOperation(openAPI, v.getDelete(), DELETE, k));
                    group.add(printOperation(openAPI, v.getPut(), PUT, k));
                    group.add(printOperation(openAPI, v.getOptions(), OPTION, k));
                    group.add(printOperation(openAPI, v.getPatch(), PATCH, k));
                    group.add(printOperation(openAPI, v.getTrace(), TRACE, k));

                    return group.stream();
                })
                .filter(Objects::nonNull)
                .toList();

    }

    private static final String COLON = " : ";
    private static final String SPACE = " ";
    private static final String CHILD_PREFIX = "\tL";
    private static final String CRLF = "\n";

    public ApiDto printOperation(OpenAPI openAPI, Operation op, HttpMethodEnum type, String path) {
        if (op == null) {
            return null;
        }

        ApiDto apiDto = ApiDto.create(path, type, op);

        if (op.getParameters() != null) {
            StringBuilder parameter = new StringBuilder();
            op.getParameters().forEach(o -> {
                if (Objects.isNull(o.getSchema().getType())) {
                    printReference(openAPI, o.getSchema(), parameter, EMPTY);
                } else {
                    parameter.append(CRLF).append(o.getName()).append(COLON).append(o.getSchema().getType());
                }
            });
            apiDto.setParameter(parameter.toString());
        }
        if (op.getRequestBody() != null) {
            apiDto.setRequestBody(printRequestBody(openAPI, op.getRequestBody()));
        }
        apiDto.setResponse(printResponses(openAPI, op.getResponses()));

        return apiDto;
    }


    private String printRequestBody(OpenAPI openAPI, RequestBody body) {
        StringBuilder reqBody = new StringBuilder();
        body.getContent().forEach((k, v) -> {
            reqBody.append(CRLF).append("content Type : ").append(k);
            printReference(openAPI, v.getSchema(), reqBody, EMPTY);
        });
        return reqBody.toString();
    }

    private String printResponses(OpenAPI openAPI, ApiResponses res) {
        StringBuilder responses = new StringBuilder();
        res.forEach((k, v) -> {
            responses.append(CRLF).append(k).append(SPACE).append(v.getDescription());
            if (v.getContent() != null) {
                v.getContent().forEach((name, media) -> {
                    responses.append(CRLF).append(name);
                    printReference(openAPI, media.getSchema(), responses, EMPTY);
                });
            }
        });

        return responses.toString();
    }


    private void printReference(OpenAPI openAPI, Schema<?> schema, StringBuilder str, String childPrefix) {
        if (schema instanceof ArraySchema arraySchema) {
            printReference(openAPI, arraySchema.getItems(), str, childPrefix + CHILD_PREFIX);
        }
        String componentName = getComponentName(schema.get$ref());
        str.append(CRLF).append(childPrefix).append(" [DTO] ").append(componentName);

        if (componentName != null) {
            Schema objSchema = openAPI.getComponents().getSchemas().get(componentName);

            if (objSchema instanceof ArraySchema arraySchema) {
                printReference(openAPI, arraySchema.getItems(), str, childPrefix + CHILD_PREFIX);
                return;
            } else if (objSchema != null) {
                objSchema.getProperties()
                        .forEach((k, v) -> {
                            if (v instanceof Schema<?>) {
                                str.append(CRLF).append(childPrefix).append(SPACE).append(k);
                                str.append(COLON).append(((Schema<?>) v).getType());

                                if (!Objects.isNull(((Schema<?>) v).getFormat())) {
                                    str.append(SPACE).append(((Schema<?>) v).getFormat());
                                }
                            } else {
                                str.append(CRLF).append(childPrefix).append(SPACE).append(k).append(COLON).append(v.getClass().getSimpleName());
                            }
                        });
            }

        }
    }

    private String getComponentName(String s) {
        if (StringUtils.isBlank(s)) {
            return EMPTY;
        }
        String prefix = "#/components/schemas/";
        if (s.startsWith(prefix)) {
            return s.substring(prefix.length());
        }
        return EMPTY;
    }


}
