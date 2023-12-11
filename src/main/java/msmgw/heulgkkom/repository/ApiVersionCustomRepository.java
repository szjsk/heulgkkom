package msmgw.heulgkkom.repository;


import java.util.List;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;


public interface ApiVersionCustomRepository {

  List<ApiManagerVersionDto> findCustomByServiceId(Long serviceId);

  List<ApiPathDto> findCustomByDomainId(Long domainId);


  }
