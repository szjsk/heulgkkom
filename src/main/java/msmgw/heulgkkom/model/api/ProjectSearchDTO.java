package msmgw.heulgkkom.model.api;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectSearchDTO {
    private Long projectSeq;
    private String projectName;
    private String versionId;
    private String envType;
    private String domainUrl;
    private String assignMail;
}