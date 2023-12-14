package msmgw.heulgkkom.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@Table(name = "domain_version")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@IdClass(DomainVersionId.class)
public class DomainVersion implements Serializable {

  //todo service id추가
  @Id
  @Column(name = "domain_id")
  private Long domainId;

  @Id
  @Column(name = "version_id")
  private Long versionId;

  @Column(name = "requested")
  private String requested;

  @CreationTimestamp
  @Column(name = "requestAt", columnDefinition = "TIMESTAMP", nullable = false)
  private LocalDateTime request_at;

}
