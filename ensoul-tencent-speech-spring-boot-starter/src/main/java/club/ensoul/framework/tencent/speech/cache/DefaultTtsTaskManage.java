package club.ensoul.framework.tencent.speech.cache;

import club.ensoul.framework.tencent.speech.domain.Callback;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wy_peng_chen6
 */
public class DefaultTtsTaskManage implements TtsTaskManage {

    private static final ConcurrentHashMap<String, TtsTaskCallback> ttsTaskCallbacks = new ConcurrentHashMap<>();

    public void callback(Callback callback) {
        TtsTaskCallback ttsTaskCallback = ttsTaskCallbacks.get(callback.getTaskId());
        ttsTaskCallback.callback(callback);
    }

    public void addTask(String taskId, TtsTaskCallback ttsTaskCallback) {
        ttsTaskCallbacks.put(taskId, ttsTaskCallback);
    }

}
