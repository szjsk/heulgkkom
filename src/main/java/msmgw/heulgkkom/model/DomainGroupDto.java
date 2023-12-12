package msmgw.heulgkkom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DomainGroupDto {
  private Long serviceId;
  private Long domainId;
  private String serviceName;
  private String group;
}
