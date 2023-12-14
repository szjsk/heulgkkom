package msmgw.heulgkkom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiPathDto {

  private Long versionId;
  private String pathKey;
  private String path;
  private HttpMethodEnum method;
  private String data;
  private Long domainId;

}
