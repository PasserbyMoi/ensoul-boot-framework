package club.ensoul.framework.tencent.speech.autoconfigure;

import club.ensoul.framework.tencent.SignMethod;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.tencentcloudapi.common.profile.Language;
import com.tencentcloudapi.common.profile.Region;
import org.springframework.lang.NonNull;

/**
 * @see <a href="https://cloud.tencent.com/document/product/1073/37989">官方文档</a>
 */
@Data
@ConfigurationProperties(prefix = "tencent.tts")
public class TencentTtsProperties {

    @NonNull
    private Boolean enabled = false;

    @NonNull
    private String appId;

    @NonNull
    private Long projectId = 10000L;

    /**
     * 服务地址
     */
    @NonNull
    private String endpoint = "tts.tencentcloudapi.com";

    @NonNull
    private Region region = Region.Shanghai;

    @NonNull
    private Language language = Language.ZH_CN;

    @NonNull
    private boolean debug = false;

    @NonNull
    private boolean unsignedPayload = false;

    @NonNull
    private SignMethod signMethod = SignMethod.SIGN_TC3_256;

}