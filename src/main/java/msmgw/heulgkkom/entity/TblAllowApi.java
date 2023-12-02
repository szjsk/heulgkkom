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
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "tbl_api")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class TblAllowApi implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "api_id")
  private Long api_id;

  @Column(name = "api_id")
  private Long apiId;

  @Column(name = "credential_id")
  private Long credentialId;

  @Column(name = "status")
  private String status;

  @Column(name = "req_reason")
  private String reqReason;

  @Column(name = "res_reason")
  private String resReason;

  @Column(name = "requested")
  private String requested;

  @Column(name = "requested_contact")
  private String requestedContact;

  @CreationTimestamp
  @Column(name = "requested_at")
  private LocalDateTime requestedAt;

  @Column(name = "response_at")
  private LocalDateTime responseAt;

}
