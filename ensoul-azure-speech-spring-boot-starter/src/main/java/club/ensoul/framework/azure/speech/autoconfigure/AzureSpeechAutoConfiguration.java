package club.ensoul.framework.azure.speech.autoconfigure;

import club.ensoul.framework.azure.speech.AzureSpeechTemplate;
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
@AutoConfigureAfter(AzureSpeechProperties.class)
@ConditionalOnProperty(value = "enabled", prefix = "azure.tts", havingValue = "true")
@EnableConfigurationProperties({AzureSpeechProperties.class})
public class AzureSpeechAutoConfiguration implements BeanPostProcessor {

    /**
     * 实例化一个 AzureSpeechTemplate 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public AzureSpeechTemplate azureSpeechTemplate() {
        return new AzureSpeechTemplate();
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return bean;
    }

}
