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
public class PushDeviceResult {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * deviceId 列表
     */
    private List<String> deviceIds;

}
