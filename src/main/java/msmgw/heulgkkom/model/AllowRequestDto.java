package msmgw.heulgkkom.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msmgw.heulgkkom.model.constant.AllowStatusEnum;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllowRequestDto {

  private Long allowId;
  private Long pathId;
  private Long domainId;
  private AllowStatusEnum status;
  private String reqReason;
  private String resReason;
  private String requested;
  private String requestedContact;
  private LocalDateTime requestedAt;
  private LocalDateTime responseAt;
}
