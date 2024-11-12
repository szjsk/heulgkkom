package msmgw.heulgkkom.model.api;


import lombok.*;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;
import msmgw.heulgkkom.model.constant.PermittedStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApprovalRequestApiDTO {
    private List<Long> permittedProjectApiSeqs;
    private PermittedStatus status;
    private String approvalReason;
    private String approvalContact;
}