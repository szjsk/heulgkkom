package msmgw.heulgkkom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.ServiceStatusEnum;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiManagerVersionDto {

  private Long versionId;
  private String versionName;
  private String openapiVersion;
  private String orginalData;
  private Long domainId;
  private String group;

}
