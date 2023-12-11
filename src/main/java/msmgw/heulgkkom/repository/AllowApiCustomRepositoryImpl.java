package msmgw.heulgkkom.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.QAllowApi;
import msmgw.heulgkkom.entity.QApiDomain;
import msmgw.heulgkkom.entity.QApiPath;
import msmgw.heulgkkom.entity.QApiService;
import msmgw.heulgkkom.entity.QApiVersion;
import msmgw.heulgkkom.entity.QDomainVersion;
import msmgw.heulgkkom.model.AllowApiListDto;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class AllowApiCustomRepositoryImpl implements AllowApiCustomRepository {

  private final JPAQueryFactory factory;
  private final QAllowApi qAllowApi = QAllowApi.allowApi;
  private final QApiDomain qApiDomain = QApiDomain.apiDomain;
  private final QApiService qApiService = QApiService.apiService;

  @Override
  public List<AllowApiListDto> findCustomByPathId(Long pathId) {

    List<BooleanExpression> wheres = new ArrayList<>();
    wheres.add(qAllowApi.pathId.eq(pathId));


    return factory.select(
            Projections.bean(AllowApiListDto.class,
                qAllowApi.allowId,
                qAllowApi.pathId,
                qAllowApi.domainId,
                qAllowApi.status,
                qAllowApi.reqReason,
                qAllowApi.resReason,
                qAllowApi.requested,
                qAllowApi.requestedContact,
                qAllowApi.requestedAt,
                qAllowApi.responseAt,
                qApiService.serviceName,
                qApiDomain.group,
                qApiDomain.url
            ))
        .from(qAllowApi)
        .leftJoin(qApiDomain).on(qApiDomain.domainId.eq(qAllowApi.domainId))
        .leftJoin(qApiService).on(qApiService.serviceId.eq(qApiDomain.serviceId))
        .where(wheres.toArray(BooleanExpression[]::new))
        .fetch();
  }


}
