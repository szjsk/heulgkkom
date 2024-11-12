package msmgw.heulgkkom.model.api;


import lombok.*;
import msmgw.heulgkkom.model.constant.HttpMethodEnum;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RequestApiDTO {
    private Long targetProjectSeq;
    private String targetPath;
    private HttpMethodEnum targetMethod;
    private String requestReason;
    private String requestContact;
}