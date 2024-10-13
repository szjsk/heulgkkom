package msmgw.heulgkkom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "permitted_project_api")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PermittedProjectApi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "api_id")
    private Long apiId;

    @Column(name = "status")
    private String status;

    @Column(name = "req_reason")
    private String reqReason;

    @Column(name = "res_reason")
    private String resReason;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "requestedAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime requestedAt;

    @Column(name = "requested_contact")
    private String requestedContact;

    @Column(name = "responseAt", columnDefinition = "TIMESTAMP")
    private LocalDateTime responseAt;
}