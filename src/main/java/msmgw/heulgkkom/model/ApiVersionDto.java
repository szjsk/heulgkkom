package msmgw.heulgkkom.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.ServiceStatusEnum;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ApiVersionDto {
  private Long versionId;
  private Long serviceId;
  private String versionName;
  private String openapiVersion;
  private String orginalData;
  private Long domainId;
  private String group;
}
