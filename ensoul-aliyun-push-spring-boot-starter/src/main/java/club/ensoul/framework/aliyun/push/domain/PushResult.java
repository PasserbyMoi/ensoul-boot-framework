package club.ensoul.framework.aliyun.push.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushResult {

    /**
     * 标志一次推送的消息ID
     */
    private String messageId;

    /**
     * 批量推送时标志一次推送的消息ID
     */
    private List<String> messageIds;

    /**
     * 请求ID
     */
    private String requestId;

}
