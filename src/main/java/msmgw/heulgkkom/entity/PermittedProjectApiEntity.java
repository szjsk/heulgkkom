package msmgw.heulgkkom.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.model.constant.PermittedStatus;

@Entity
@Table(name = "permitted_project_api")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PermittedProjectApiEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permittedProjectApiSeq;

    @ManyToOne
    @JoinColumn(name = "req_project_seq", referencedColumnName = "project_seq")
    private ProjectEntity reqProject;

    @ManyToOne
    @JoinColumn(name = "target_project_seq", referencedColumnName = "project_seq")
    private ProjectEntity targetProject;

    @Column(name = "target_path", length = 2000, nullable = false)
    private String targetPath;

    @Column(name = "target_method", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethodEnum targetMethod;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PermittedStatus status;

    //request
    @Column(name = "request_reason", length=2000, nullable = false)
    private String requestReason;

    @Column(name = "request_by", nullable = false)
    private String requestedBy;

    @Column(name = "request_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime requestAt;

    @Column(name = "request_contact", length=500, nullable = false)
    private String requestContact;

    //approval
    @Column(name = "approval_reason", length=2000)
    private String approvalReason;

    @Column(name = "approval_by")
    private String approvalBy;

    @Column(name = "approval_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime approvalAt;

    @Column(name = "approval_contact", length=500)
    private String approvalContact;

}