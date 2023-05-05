package club.ensoul.framework.aliyun.sms.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushResult {

    /** 标志一次推送的消息ID */
    private String MessageId;

    /** 请求ID */
    private String RequestId;

}
