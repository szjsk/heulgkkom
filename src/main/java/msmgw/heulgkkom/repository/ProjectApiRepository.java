package msmgw.heulgkkom.repository;

import msmgw.heulgkkom.entity.ProjectApiEntity;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectApiRepository extends JpaRepository<ProjectApiEntity, Long> {


    @Query("SELECT a FROM ProjectApiEntity a JOIN a.project p " +
            "WHERE (COALESCE(:projectName, '') = '' OR p.projectName LIKE CONCAT('%', :projectName, '%')) " +
            "AND p.envType = :envType " +
            "AND (COALESCE(:apiName, '') = '' OR a.path LIKE CONCAT('%', :apiName, '%')) " +
            "AND a.versionId = p.versionId")
    Page<ProjectApiEntity> findProjectsWithApis(@Param("projectName") String projectName,
                                                @Param("envType") String envType,
                                                @Param("apiName") String apiName,
                                                Pageable pageable);

    Optional<ProjectApiEntity> findByProject_ProjectSeqAndVersionIdAndPathAndMethod(Long projectSeq, String versionId, String path, HttpMethodEnum method);

}