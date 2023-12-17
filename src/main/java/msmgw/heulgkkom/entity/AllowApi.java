package msmgw.heulgkkom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import msmgw.heulgkkom.model.constant.AllowStatusEnum;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "allow_api")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AllowApi implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "allow_id")
  private Long allowId;

  @Column(name = "path_key")
  private String pathKey;

  @Column(name = "req_domain_id")
  private Long reqDomainId;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private AllowStatusEnum status;

  @Column(name = "req_reason")
  private String reqReason;

  @Column(name = "res_reason")
  private String resReason;

  @Column(name = "requested")
  private String requested;

  @Column(name = "requested_contact")
  private String requestedContact;

  @Column(name = "requested_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime requestedAt;

  @Column(name = "response_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime responseAt;

}