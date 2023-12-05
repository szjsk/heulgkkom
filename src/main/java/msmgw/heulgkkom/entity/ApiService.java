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
import msmgw.heulgkkom.model.constant.ServiceStatusEnum;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "api_service")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ApiService implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "service_id")
  private Long serviceId;

  @Column(name = "service_name")
  private String serviceName;

  @Column(name = "service_desc")
  private String serviceDesc;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private ServiceStatusEnum status;

  @Column(name = "created", updatable = false)
  private String created;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "modifier")
  private String modifier;

  @CreationTimestamp
  @Column(name = "modifed_at", columnDefinition = "TIMESTAMP")
  private LocalDateTime modifiedAt;
}
