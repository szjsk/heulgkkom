package msmgw.heulgkkom.repository;


import java.util.List;
import java.util.Optional;
import msmgw.heulgkkom.entity.ApiDomain;
import msmgw.heulgkkom.entity.DomainVersion;
import msmgw.heulgkkom.entity.DomainVersionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DomainVersionRepository extends JpaRepository<DomainVersion, DomainVersionId> {

  Optional<DomainVersion> findByDomainId(Long domainId);

}
