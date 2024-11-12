package msmgw.heulgkkom.model.api;


import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiDTO {

    private Long projectId;
    private String projectName;
    private String envType;
    private String domainUrl;
    private String assignMail;
    private String versionId;
    private Long apiId;
    private String path;
    private HttpMethodEnum method;
    private String parameter;
    private String request;
    private String response;
}