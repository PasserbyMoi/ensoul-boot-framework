package club.ensoul.framework.chatgtp.autoconfigure;

import club.ensoul.framework.chatgtp.ChatGTPTemplate;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Aliyun MQ OpenApi Instance 管理自动配置实现
 *
 * @see ChatGTPAutoConfiguration
 */
@Slf4j
@Configuration
@ConditionalOnProperty(value = "enabled", prefix = "open.api.chatGTP", havingValue = "true")
@EnableConfigurationProperties(ChatGTPProperties.class)
public class ChatGTPAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public ChatGTPTemplate chatGTPTemplate(ChatGTPProperties chatGTPProperties) {
        return null;
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenAiService openAiService(ChatGTPProperties chatGTPProperties) {
        return new OpenAiService(chatGTPProperties.getOpenaiApiKey());
    }

}
