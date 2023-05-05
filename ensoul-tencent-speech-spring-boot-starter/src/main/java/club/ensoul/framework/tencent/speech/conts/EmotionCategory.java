package club.ensoul.framework.tencent.speech.conts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wy_peng_chen6
 */
public enum EmotionCategory {

    neutral("中性"),
    sad("悲伤"),
    happy("高兴"),
    angry("生气"),
    fear("恐惧"),
    news("新闻"),
    story("故事"),
    radio("广播"),
    poetry("诗歌"),
    call("客服");

    public final String displayName;

    EmotionCategory(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>(EmotionCategory.values().length);
        for (EmotionCategory emotionCategory : EmotionCategory.values()) {
            map.put(emotionCategory.name(), emotionCategory.displayName);
        }
        return map;
    }

}
