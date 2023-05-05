package club.ensoul.framework.aliyun.push.autoconfigure;

import club.ensoul.framework.aliyun.push.AliyunPushTagTemplate;
import club.ensoul.framework.aliyun.push.AliyunPushTemplate;
import com.aliyun.push20160801.Client;
import com.aliyun.teaopenapi.models.*;
import club.ensoul.framework.aliyun.push.AliyunPushAliasTemplate;
import club.ensoul.framework.aliyun.push.AliyunPushDeviceTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "enabled", prefix = "aliyun.push", havingValue = "true")
@EnableConfigurationProperties(PushProperties.class)
public class AliyunPushAutoConfiguration {

    @Bean("pushClient")
    @ConditionalOnMissingBean
    public Client pushClient(PushProperties pushProperties) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(pushProperties.getAccessKeyId());
        config.setAccessKeySecret(pushProperties.getAccessKeySecret());
        config.setProtocol(pushProperties.getProtocol().name());
        config.setRegionId(pushProperties.getRegionId());
        config.setEndpoint(pushProperties.getEndpoint());
        config.setConnectTimeout(pushProperties.getConnectTimeout());
        config.setReadTimeout(pushProperties.getReadTimeout());
        return new Client(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunPushTemplate aliyunPushTemplate(Client pushClient) {
        return new AliyunPushTemplate(pushClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunPushTagTemplate aliyunPushTagTemplate(Client pushClient) {
        return new AliyunPushTagTemplate(pushClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunPushAliasTemplate aliyunPushAliasTemplate(Client pushClient) {
        return new AliyunPushAliasTemplate(pushClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunPushDeviceTemplate aliyunPushDeviceTemplate(Client pushClient) {
        return new AliyunPushDeviceTemplate(pushClient);
    }

}
