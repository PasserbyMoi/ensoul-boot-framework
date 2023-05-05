package club.ensoul.framework.tencent.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

/**
 * @see <a href="https://cloud.tencent.com/document/product/1073/37989">官方文档</a>
 */
@Data
@ConfigurationProperties(prefix = "tencent")
public class TencentProperties {

    @NonNull
    private String appId;

    @NonNull
    private Long projectId = 10000L;

    @NonNull
    private String secretId = "";

    @NonNull
    private String secretKey = "";

    public enum SignMethod {
        SIGN_SHA1("HmacSHA1"),
        SIGN_SHA256("HmacSHA256"),
        SIGN_TC3_256("TC3-HMAC-SHA256");

        public final String signMethod;

        SignMethod(String signMethod) {
            this.signMethod = signMethod;
        }
    }

}