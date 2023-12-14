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
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "api_path")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ApiPath implements Serializable {
  @Id
  @Column(name = "path_key")
  private String pathKey;

  @Column(name = "version_id")
  private Long versionId;

  @Column(name = "path")
  private String path;

  @Column(name = "method")
  @Enumerated(EnumType.STRING)
  private HttpMethodEnum method;

  @Column(name = "data")
  private String data;

}
