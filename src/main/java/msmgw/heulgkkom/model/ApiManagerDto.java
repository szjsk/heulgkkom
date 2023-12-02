package msmgw.heulgkkom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.ServiceStatusEnum;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ApiManagerDto {
  private Long apiId;
  private Long credentialId;
  private String version;

  private String serviceDesc;
  private ServiceStatusEnum status;
}
