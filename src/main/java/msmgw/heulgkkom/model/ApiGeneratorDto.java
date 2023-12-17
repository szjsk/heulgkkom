package msmgw.heulgkkom.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.AllowStatusEnum;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiGeneratorDto {
  private String pathKey;
  private Long serviceId;
  private String serviceName;
  private Long domainId;
  private String group;
  private String url;
  private String token;
  private Long versionId;
  private String path;
  private HttpMethodEnum method;
  private String data;
}
