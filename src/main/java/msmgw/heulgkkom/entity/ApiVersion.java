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
@Table(name = "api_version")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ApiVersion implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "version_id")
  private Long versionId;

  @Column(name = "service_id")
  private Long serviceId;

  @Column(name = "version_name")
  private String versionName;

  @Column(name = "openapi_version")
  private String openapiVersion;

  @Column(name = "orginal_data")
  private String orginalData;

  @Column(name = "created", updatable = false)
  private String created;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "TIMESTAMP", nullable = false, updatable = false)
  private LocalDateTime createdAt;

}
