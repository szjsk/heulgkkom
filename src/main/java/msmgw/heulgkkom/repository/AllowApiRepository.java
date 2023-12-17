package msmgw.heulgkkom.repository;


import java.util.List;
import msmgw.heulgkkom.entity.AllowApi;
import msmgw.heulgkkom.entity.ApiPath;
import msmgw.heulgkkom.model.constant.AllowStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AllowApiRepository extends JpaRepository<AllowApi, Long>, AllowApiCustomRepository {

  List<AllowApi> findAllByReqDomainIdAndStatus(Long domainId, AllowStatusEnum statusEnum);

}
