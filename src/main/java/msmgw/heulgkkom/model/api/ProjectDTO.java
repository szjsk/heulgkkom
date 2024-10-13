package msmgw.heulgkkom.model.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDTO {
    private String projectName;
    private String envType;
    private String domainUrl;
    private String assignMail;
}