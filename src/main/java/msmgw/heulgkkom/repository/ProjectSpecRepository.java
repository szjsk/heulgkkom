package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.ProjectApiEntity;
import msmgw.heulgkkom.entity.ProjectSpecEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectSpecRepository extends JpaRepository<ProjectSpecEntity, String> {

    Optional<ProjectSpecEntity> findByProject_ProjectSeqAndVersionId(Long projectSeq, String versionId);

    List<ProjectSpecEntity> findAllByProject_ProjectSeq(Long projectSeq);

}