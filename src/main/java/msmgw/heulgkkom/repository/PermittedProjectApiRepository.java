package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.PermittedProjectApiEntity;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.model.constant.PermittedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermittedProjectApiRepository extends JpaRepository<PermittedProjectApiEntity, Long> {

    List<PermittedProjectApiEntity> findAllByReqProject_ProjectSeqAndStatus(Long reqProjectSeq, PermittedStatus status);

    Optional<PermittedProjectApiEntity> findByReqProject_ProjectSeqAndTargetPathAndTargetMethod(Long reqProjectSeq, String path, HttpMethodEnum method);

}