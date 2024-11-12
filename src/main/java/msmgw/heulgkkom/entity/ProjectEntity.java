package msmgw.heulgkkom.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "project")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ProjectEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_seq")
    private Long projectSeq;

    @Column(name = "project_name", length=500, nullable = false)
    private String projectName;

    @Column(name = "version_id", length = 500)
    private String versionId;

    @Column(name = "env_type", length=50, nullable = false)
    private String envType;

    @Column(name = "domain_url", length=2000, nullable = false)
    private String domainUrl;

    @Column(name = "assign_mail", length=2000)
    private String assignMail;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "project")
    private List<ProjectApiEntity> projectApi;

    @OneToMany(mappedBy = "project")
    private List<ProjectSpecEntity> projectSpec;

}