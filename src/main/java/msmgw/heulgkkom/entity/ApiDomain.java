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
@Table(name = "api_domain")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ApiDomain implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "domain_id")
  private Long domainId;

  @Column(name = "service_id")
  private Long serviceId;

  @Column(name = "\"group\"")
  private String group;

  @Column(name = "url")
  private String url;

  @Column(name = "token")
  private String token;

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
