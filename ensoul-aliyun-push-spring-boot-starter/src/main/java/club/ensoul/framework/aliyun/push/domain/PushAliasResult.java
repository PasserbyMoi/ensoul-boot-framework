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
public class PushAliasResult {

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * aliasName 列表
     */
    private List<String> aliasNames;

}
