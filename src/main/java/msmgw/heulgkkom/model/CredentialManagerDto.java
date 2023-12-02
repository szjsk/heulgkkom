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
public class CredentialManagerDto {
  private Long credentialId;
  private Long serviceId;
  private String group;
  private String url;
  private String token;
}
