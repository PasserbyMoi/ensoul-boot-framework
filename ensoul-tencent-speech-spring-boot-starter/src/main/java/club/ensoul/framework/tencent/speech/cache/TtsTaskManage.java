package club.ensoul.framework.tencent.speech.cache;

import club.ensoul.framework.tencent.speech.domain.Callback;

/**
 * @author wy_peng_chen6
 */
public interface TtsTaskManage {

    void callback(Callback callback);

    void addTask(String taskId, TtsTaskCallback ttsTaskCallback);

}
