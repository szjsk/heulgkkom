package msmgw.heulgkkom.repository;


import java.util.List;
import msmgw.heulgkkom.model.AllowApiListDto;
import msmgw.heulgkkom.model.ApiManagerVersionDto;

public interface AllowApiCustomRepository {

  List<AllowApiListDto> findCustomByPathKey(String pathKey);
  List<AllowApiListDto> findCustomByDomainId(Long domainId);

}
