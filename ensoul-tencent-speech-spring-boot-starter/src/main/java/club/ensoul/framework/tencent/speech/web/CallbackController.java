package club.ensoul.framework.tencent.speech.web;

import club.ensoul.framework.tencent.speech.cache.TtsTaskManage;
import club.ensoul.framework.tencent.speech.domain.Callback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wy_peng_chen6
 */
@RestController
@RequestMapping(value = "${}")
public class CallbackController {

    private final TtsTaskManage ttsTaskManage;

    public CallbackController(TtsTaskManage ttsTaskManage) {
        this.ttsTaskManage = ttsTaskManage;
    }

    @GetMapping
    public void callback(@RequestBody Callback callback) {
        ttsTaskManage.callback(callback);
    }

}
