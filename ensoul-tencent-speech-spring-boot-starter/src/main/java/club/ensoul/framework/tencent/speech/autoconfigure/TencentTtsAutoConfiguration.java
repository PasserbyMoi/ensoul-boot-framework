package club.ensoul.framework.tencent.speech.autoconfigure;

import club.ensoul.framework.tencent.autoconfigure.TencentAutoConfiguration;
import club.ensoul.framework.tencent.autoconfigure.TencentProperties;
import club.ensoul.framework.tencent.speech.TencentTtsTemplate;
import club.ensoul.framework.tencent.speech.cache.DefaultTtsTaskManage;
import club.ensoul.framework.tencent.speech.cache.TtsTaskManage;
import com.tencent.SpeechClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tts.v20190823.TtsClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

/**
 * Aliyun MQ 自动生产者及消费者自动配置实现
 *
 * @see BeanPostProcessor
 */
@Slf4j
@Configuration
@AutoConfigureAfter(TencentAutoConfiguration.class)
@ConditionalOnProperty(value = "enabled", prefix = "tencent.tts", havingValue = "true")
@EnableConfigurationProperties({TencentProperties.class, TencentTtsProperties.class})
public class TencentTtsAutoConfiguration implements BeanPostProcessor {

    /**
     * 实例化一个http选项，可选的，没有特殊需求可以跳过
     *
     * @param tencentProperties 配置信息 {@link TencentProperties}
     * @return TtsClient
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpProfile httpProfile(TencentTtsProperties tencentProperties) {
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint(tencentProperties.getEndpoint());
        return httpProfile;
    }

    /**
     * 实例化一个client选项，可选的，没有特殊需求可以跳过
     *
     * @param httpProfile 配置信息 {@link HttpProfile}
     * @return TtsClient
     */
    @Bean
    @ConditionalOnMissingBean
    public ClientProfile clientProfile(HttpProfile httpProfile, TencentTtsProperties tencentProperties) {
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        clientProfile.setLanguage(tencentProperties.getLanguage());
        clientProfile.setDebug(tencentProperties.isDebug());
        clientProfile.setSignMethod(tencentProperties.getSignMethod().signMethod);
        clientProfile.setUnsignedPayload(tencentProperties.isUnsignedPayload());
        return clientProfile;
    }


    /**
     * 实例化要请求产品的TtsClient对象
     *
     * @param credential        认证信息 {@link Credential}
     * @param clientProfile     可选的 {@link ClientProfile}
     * @param tencentProperties 配置信息 {@link TencentTtsProperties}
     * @return TtsClient
     */
    @Bean
    @ConditionalOnMissingBean
    public TtsClient ttsClient(Credential credential, ClientProfile clientProfile, TencentTtsProperties tencentProperties) {
        return new TtsClient(credential, tencentProperties.getRegion().getValue(), clientProfile);
    }

    /**
     * 实例化要请求产品的TtsClient对象
     *
     * @param tencentProperties 配置信息 {@link TencentTtsProperties}
     * @return TtsClient
     * @see <a href="https://cloud.tencent.com/document/product/1073/37933">TTS SDK 说明</a>
     */
    @Bean
    @ConditionalOnMissingBean
    public SpeechClient speechClient(TencentProperties tencentProperties) {
        return SpeechClient.newInstance(tencentProperties.getAppId(), tencentProperties.getSecretId(), tencentProperties.getSecretKey());
    }

    /**
     * 实例化要请求产品的TtsClient对象
     *
     * @param ttsClient         认证信息 {@link TtsClient}
     * @param speechClient      可选的 {@link SpeechClient}
     * @param tencentProperties 配置信息 {@link TencentTtsProperties}
     * @return TtsClient
     */
    @Bean
    @ConditionalOnMissingBean
    public TencentTtsTemplate tencentTtsTemplate(TtsClient ttsClient, SpeechClient speechClient, TtsTaskManage ttsTaskManage, TencentTtsProperties tencentProperties) {
        return new TencentTtsTemplate(tencentProperties.getProjectId(), ttsClient, speechClient, ttsTaskManage);
    }

    @Bean
    @ConditionalOnMissingBean
    public TtsTaskManage ttsTaskManage() {
        return new DefaultTtsTaskManage();
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }

}
