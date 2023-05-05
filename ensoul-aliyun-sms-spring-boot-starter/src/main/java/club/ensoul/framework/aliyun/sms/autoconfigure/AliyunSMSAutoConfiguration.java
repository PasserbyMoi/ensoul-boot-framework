package club.ensoul.framework.aliyun.sms.autoconfigure;

import club.ensoul.framework.aliyun.sms.*;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "enabled", prefix = "aliyun.sms", havingValue = "true")
@EnableConfigurationProperties(SMSProperties.class)
public class AliyunSMSAutoConfiguration {

    @Bean("ossClient")
    @ConditionalOnMissingBean
    public Client ossClient(SMSProperties smsProperties) throws Exception {
        Config config = new Config();
        config.setAccessKeyId(smsProperties.getAccessKeyId());
        config.setAccessKeySecret(smsProperties.getAccessKeySecret());
        config.setEndpoint(smsProperties.getSendEndpoint());
        config.setProtocol(smsProperties.getProtocol().name());
        config.setRegionId(smsProperties.getRegionId());
        config.setConnectTimeout(smsProperties.getConnectTimeout());
        config.setReadTimeout(smsProperties.getReadTimeout());
        return new Client(config);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunSmsSendTemplate aliyunSmsTemplate(Client ossClient) {
        return new AliyunSmsSendTemplate(ossClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunShortUrlTemplate aliyunShortUrlTemplate(Client ossClient) {
        return new AliyunShortUrlTemplate(ossClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunSmsTemplateTemplate aliyunSmsTemplateTemplate(Client ossClient) {
        return new AliyunSmsTemplateTemplate(ossClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunSmsSignTemplate aliyunSmsSignTemplate(Client ossClient) {
        return new AliyunSmsSignTemplate(ossClient);
    }

    @Bean
    @ConditionalOnMissingBean
    public AliyunTagResourcesTemplate aliyunTagResourcesTemplate(Client ossClient) {
        return new AliyunTagResourcesTemplate(ossClient);
    }

}
