package club.ensoul.framework.tencent.autoconfigure;

import com.tencentcloudapi.common.Credential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Slf4j
@Configuration
@EnableConfigurationProperties(TencentProperties.class)
public class TencentAutoConfiguration implements BeanPostProcessor {

    /**
     * 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
     * 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，
     * <a href="https://cloud.tencent.com/document/product/1278/85305">请参见</a>
     * 密钥可前往<a href="https://console.cloud.tencent.com/cam/capi">官网控制台</a> 进行获取
     *
     * @param tencentProperties 配置信息 {@link TencentProperties}
     * @return TtsClient
     */
    @Bean
    @ConditionalOnMissingBean
    public Credential credential(TencentProperties tencentProperties) {
        Credential credential = new Credential();
        credential.setSecretId(tencentProperties.getSecretId());
        credential.setSecretKey(tencentProperties.getSecretKey());
        return credential;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }

}
