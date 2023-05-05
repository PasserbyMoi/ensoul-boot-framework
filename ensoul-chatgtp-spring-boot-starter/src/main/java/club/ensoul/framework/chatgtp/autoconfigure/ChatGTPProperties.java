package club.ensoul.framework.chatgtp.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "open.api.chat-gtp")
public class ChatGTPProperties {

    private Boolean enabled = false;

    /** API 密钥 */
    private String openaiApiKey = "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";

    /** 组织 id 对于属于多个组织的用户，您可以传递标头以指定用于 API 请求的组织。这些 API 请求的使用量将计入指定组织的订阅配额。 */
    private String orgId;

}