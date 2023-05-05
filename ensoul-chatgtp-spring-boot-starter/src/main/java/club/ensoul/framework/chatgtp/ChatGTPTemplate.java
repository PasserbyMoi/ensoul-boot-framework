package club.ensoul.framework.chatgtp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

/**
 * ChatGTP 接口文档
 *
 * @see <a href="https://platform.openai.com/docs/api-reference/introduction">接口文档</a>
 * @author wy_peng_chen6
 */
@Slf4j
public class ChatGTPTemplate {

    /**
     * 列出当前可用的模型，并提供有关每个模型的基本信息，例如所有者和可用性。
     * <p>
     * curl <a href="https://api.openai.com/v1/models">https://api.openai.com/v1/models</a>
     * -H "Authorization: Bearer $OPENAI_API_KEY"
     */
    public String getAllModels() {
        return "";
    }

    /**
     * 检索模型实例，提供有关模型的基本信息，例如所有者和权限。
     * <p>
     * curl <a href="https://api.openai.com/v1/models/text-davinci-003">https://api.openai.com/v1/models/text-davinci-003</a>
     * -H "Authorization: Bearer $OPENAI_API_KEY"
     *
     * @param modelId 用于此请求的模型的 ID
     */
    public String getModel(@NonNull String modelId) {
        return "";
    }


    /**
     * 给定一个提示，该模型将返回一个或多个预测的完成度，并且还可以返回每个位置的备选标记的概率。
     *
     * 为提供的提示和参数创建完成度
     *
     * <p>
     * curl <a href="https://api.openai.com/v1/completions">...</a> \
     *   -H "Content-Type: application/json" \
     *   -H "Authorization: Bearer $OPENAI_API_KEY" \
     *   -d '{
     *     "model": "text-davinci-003",
     *     "prompt": "Say this is a test",
     *     "max_tokens": 7,
     *     "temperature": 0
     *   }'
     *
     *
     *
     *
     * @param modelId 用于此请求的模型的 ID
     */
    public String completions() {
        return "";
    }

}
