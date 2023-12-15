package msmgw.heulgkkom.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import io.swagger.v3.oas.models.Operation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiDto {

  private String path;
  private HttpMethodEnum method;
  private Operation op;
  private String parameter;
  private String requestBody;
  private String response;

  public static ApiDto create(String path, HttpMethodEnum method, Operation op){
    return new ApiDto(path, method, op, EMPTY, EMPTY, EMPTY);
  }

}
