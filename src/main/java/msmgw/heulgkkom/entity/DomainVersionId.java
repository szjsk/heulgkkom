package msmgw.heulgkkom.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class DomainVersionId implements Serializable {

  private Long domainId;
  private Long versionId;
}
