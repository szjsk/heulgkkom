package msmgw.heulgkkom.model.api;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestApiDTO {
    private List<Long> apiId;
    private String requestReason;
    private String responseReason;
    private String status;
    private String requestedBy;
    private String requestedContact;
}