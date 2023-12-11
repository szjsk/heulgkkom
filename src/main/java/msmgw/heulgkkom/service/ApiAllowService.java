package msmgw.heulgkkom.service;


import static msmgw.heulgkkom.model.constant.HttpMethodEnum.DELETE;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.GET;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.HEAD;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.OPTION;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.PATCH;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.POST;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.PUT;
import static msmgw.heulgkkom.model.constant.HttpMethodEnum.TRACE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.ApiComponent;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.entity.ApiVersion;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.model.AllowApiListDto;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.repository.AllowApiRepository;
import msmgw.heulgkkom.repository.ApiComponentRepository;
import msmgw.heulgkkom.repository.ApiPathRepository;
import msmgw.heulgkkom.repository.ApiVersionRepository;
import msmgw.heulgkkom.repository.DomainVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApiAllowService {

  final private AllowApiRepository allowApiRepository;

  public List<AllowApiListDto> findAllowApiLists(Long pathId){
    return allowApiRepository.findCustomByPathId(pathId);
  }

}
