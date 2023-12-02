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
public class ServiceManagerDto {
  private Long serviceId;
  private String serviceName;
  private String serviceDesc;
  private ServiceStatusEnum status;
}
