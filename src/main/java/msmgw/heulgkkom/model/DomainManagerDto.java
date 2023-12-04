package msmgw.heulgkkom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class DomainManagerDto {
  private Long domainId;
  private Long serviceId;
  private String group;
  private String url;
  private String token;
}
