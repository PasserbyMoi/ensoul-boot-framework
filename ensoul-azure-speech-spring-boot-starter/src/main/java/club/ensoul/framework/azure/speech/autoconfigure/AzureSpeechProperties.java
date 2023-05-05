package club.ensoul.framework.azure.speech.autoconfigure;

import club.ensoul.framework.azure.speech.conts.SpeechRegion;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;

/**
 * @see <a href="https://cloud.tencent.com/document/product/1073/37989">官方文档</a>
 */
@Data
@ConfigurationProperties(prefix = "azure.speech")
public class AzureSpeechProperties {

    @NonNull
    private Boolean enabled = false;

    @NonNull
    private String key = "8c6e9c26e0bd4a648b0e9fe90f4c56d7";

    /**
     * 服务地址
     */
    @NonNull
    private String endpoint = "https://eastasia.api.cognitive.microsoft.com/sts/v1.0/issuetoken";

    @NonNull
    private SpeechRegion region = SpeechRegion.eastasia;

}