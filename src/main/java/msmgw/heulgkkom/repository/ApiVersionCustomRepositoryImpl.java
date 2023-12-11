package msmgw.heulgkkom.repository;


import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import msmgw.heulgkkom.entity.QApiDomain;
import msmgw.heulgkkom.entity.QApiPath;
import msmgw.heulgkkom.entity.QApiVersion;
import msmgw.heulgkkom.entity.QDomainVersion;
import msmgw.heulgkkom.model.ApiManagerVersionDto;
import msmgw.heulgkkom.model.ApiPathDto;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ApiVersionCustomRepositoryImpl implements ApiVersionCustomRepository {

  private final JPAQueryFactory factory;
  private final QApiVersion qApiVersion = QApiVersion.apiVersion;
  private final QDomainVersion qDomainVersion = QDomainVersion.domainVersion;
  private final QApiDomain qApiDomain = QApiDomain.apiDomain;
  private final QApiPath qApiPath = QApiPath.apiPath;

  @Override
  public List<ApiManagerVersionDto> findCustomByServiceId(Long serviceId) {

    List<BooleanExpression> wheres = new ArrayList<>();
    wheres.add(qApiVersion.serviceId.eq(serviceId));


    return factory.select(
            Projections.bean(ApiManagerVersionDto.class,
                qApiVersion.versionId,
                qApiVersion.versionName,
                qApiVersion.openapiVersion,
                qApiVersion.orginalData,
                qDomainVersion.domainId,
                qApiDomain.group
            ))
        .from(qApiVersion)
        .leftJoin(qDomainVersion).on(qApiVersion.versionId.eq(qDomainVersion.versionId))
        .leftJoin(qApiDomain).on(qDomainVersion.domainId.eq(qApiDomain.domainId))
        .where(wheres.toArray(BooleanExpression[]::new))
        .fetch();
  }



  @Override
  public List<ApiPathDto> findCustomByDomainId(Long domainId) {

    List<BooleanExpression> wheres = new ArrayList<>();
    wheres.add(qDomainVersion.domainId.eq(domainId));

    return factory.select(
            Projections.bean(ApiPathDto.class,
                qApiPath.versionId,
                qApiPath.pathId,
                qApiPath.path,
                qApiPath.method,
                qApiPath.data,
                qDomainVersion.domainId
            ))
        .from(qApiPath)
        .leftJoin(qDomainVersion).on(qApiPath.versionId.eq(qDomainVersion.versionId))
        .where(wheres.toArray(BooleanExpression[]::new))
        .fetch();
  }

}
