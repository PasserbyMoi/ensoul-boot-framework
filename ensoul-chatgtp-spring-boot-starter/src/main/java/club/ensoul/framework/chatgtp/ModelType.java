package club.ensoul.framework.chatgtp;

import club.ensoul.framework.chatgtp.annotation.Beta;
import club.ensoul.framework.chatgtp.annotation.Limited;

public enum ModelType {

    /**
     * 一组改进 GPT-3.5 的模型，可以理解和生成自然语言或代码
     */
    @Limited @Beta
    GPT_4("GTP-4"),

    /**
     * 从2023年3月14日的快照。与之不同的是，这种模式不会得到更新，只支持三个月的时间，到2023年6月14日结束。
     */
    @Limited @Beta
    GPT_4_0314("GPT-4-0314"),

    /**
     * 与基本模式的功能相同，但有4倍的上下文长度。将根据我们最新的模式迭代进行更新。gpt-4
     */
    @Limited @Beta
    GPT_4_32K("GPT-4-32K"),

    /**
     * 从2023年3月14日的快照。与之不同的是，该模型将不会收到更新，只支持三个月的时间，到2023年6月14日为止.GPT-4-32GPT-4-32K
     */
    @Limited @Beta
    GPT_4_32K_0314("GPT-4-32K-0314"),

    /**
     * 	一组改进 GPT-3 的模型，可以理解并生成自然语言或代码
     */
    GPT3_5("GTP-3.5"),
    /** 最有能力的GPT-3.5模型，并以1/10的成本对聊天进行了优化。 将更新我们最新的模型迭代。 text-davinci-003 */
    GPT3_5_Turbo("gpt-3.5-turbo"),
    /** 从2023年3月1日起的快照。与之不同的是，该模型将不会得到更新，只支持三个月的时间，到2023年6月1日结束。GPT-3.5-turbogpt-3.5-turbo */
    GPT3_5_Turbo_0301("gpt-3.5-turbo-0301"),
    /** 可以完成任何语言任务，与居里、巴贝奇或阿达模型相比，质量更好，输出更长，而且指令遵循一致。还支持在文本中插入补语。 */
    GPT3_5_Text_Davinci_003("text-davinci-003"),
    /** 类似的能力，但用监督下的微调而不是强化学习来训练。*/
    GPT3_5_Text_Davinci_002("text-davinci-002"),
    /** 对代码完成任务进行了优化 */
    GPT3_5_Code_Davinci_002("code-davinci-002"),

    /**
     * 一组可以理解和生成自然语言的模型
     */
    GPT_3_Text_Curie_001("text-curie-001"),
    GPT_3_Text_Babbage_001("text-babbage-001"),
    GPT_3_Text_Ada_001("text-ada-001"),
    GPT_3_Davinci("davinci"),
    GPT_3_Curie("curie"),
    GPT_3_Babbage("babbage"),
    GPT_3_Ada("ada"),

    /**
     * 可以在给定自然语言提示的情况下生成和编辑图像的模型
     */
    @Beta
    DALL_E("DALL·E"),

    /**
     * 可以将音频转换为文本的模型
     */
    @Beta
    Whisper("Whisper"),

    /**
     * 一组可以将文本转换为数字形式的模型
     */
    Embeddings("Embeddings"),

    /**
     * 可以检测文本是否敏感或不安全的微调模型
     */
    Moderation("Moderation"),

    /**
     * 最有能力的调控模式。准确度将比稳定模式略高。
     */
    Moderation_Latest("text-moderation-latest"),

    /**
     * 几乎和最新型号一样有能力，但略显老旧。
     */
    Moderation_Stable("text-moderation-stable"),

    /**
     * 一组可以理解和生成代码的模型，包括将自然语言转换为代码
     */
    @Deprecated
    Codex("Codex"),
    Codex_Davinci_002("code-davinci-002"),
    Codex_Davinci_001("code-davinci-001"),
    Codex_Cushman_002("code-cushman-002"),
    Codex_Cushman_001("code-cushman-001"),

    ;

    String modelId;

    ModelType(String modelId) {
        this.modelId = modelId;
    }

}
