package club.ensoul.framework.tencent.speech;

import club.ensoul.framework.tencent.speech.cache.TtsTaskManage;
import club.ensoul.framework.tencent.speech.conts.CodecType;
import club.ensoul.framework.tencent.speech.conts.EmotionCategory;
import club.ensoul.framework.tencent.speech.conts.SampleRate;
import club.ensoul.framework.tencent.speech.conts.VoiceType;
import club.ensoul.framework.tencent.speech.exception.TencentException;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.tencent.SpeechClient;
import com.tencent.tts.model.SpeechSynthesisRequest;
import com.tencent.tts.model.SpeechSynthesisResponse;
import com.tencent.tts.service.SpeechSynthesisListener;
import com.tencent.tts.service.SpeechSynthesizer;
import com.tencent.tts.utils.Ttsutils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.tts.v20190823.TtsClient;
import com.tencentcloudapi.tts.v20190823.models.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MQ简易发送类
 *
 * @author wy_peng_chen6
 */
@Slf4j
public class TencentTtsTemplate {

    /**
     * 项目id，用户自定义，默认为0
     */
    private final Long projectId;
    private final TtsClient ttsClient;
    private final SpeechClient speechClient;
    private final TtsTaskManage ttsTaskManage;

    /**
     * 模型类型，1-默认模型。
     */
    private final long modelType = -1;
    /**
     * 断句敏感阈值，默认值为：0，取值范围：[0,1,2]。该值越大越不容易断句，模型会更倾向于仅按照标点符号断句。此参数建议不要随意调整，可能会影响合成效果。
     */
    private final long segmentRate = 0L;
    /**
     * 是否开启时间戳功能，默认为false。
     */
    private final boolean enableSubtitle = false;

    public TencentTtsTemplate(Long projectId, TtsClient ttsClient, SpeechClient speechClient, TtsTaskManage ttsTaskManage) {
        this.projectId = projectId == null ? 0L : projectId;
        this.ttsClient = ttsClient;
        this.speechClient = speechClient;
        this.ttsTaskManage = ttsTaskManage;
    }

    public TextToVoiceResponse textToVoice(String text) {
        return textToVoice(text, VoiceType.BZ_TY, CodecType.mp3, 0, 0, EmotionCategory.neutral, 100L, 1, SampleRate.$16000);
    }

    /**
     * <pre>
     * 基础语音合成
     *
     * 接口请求域名： tts.tencentcloudapi.com 。<br/>
     *
     * 腾讯云语音合成技术（TTS）可以将任意文本转化为语音，实现让机器和应用张口说话。
     *
     * 腾讯TTS技术可以应用到很多场景，比如，
     * - 移动APP语音播报新闻；
     * - 智能设备语音提醒；
     * - 依靠网上现有节目或少量录音，快速合成明星语音，降低邀约成本；
     * - 支持车载导航语音合成的个性化语音播报。
     * 内测期间免费使用。 基础合成支持 SSML，语法详见 SSML 标记语言。
     *
     * 默认接口请求频率限制：20次/秒。
     * </pre>
     *
     * @param text             合成语音的源文本，按UTF-8编码统一计算。 中文最大支持150个汉字（全角标点符号算一个汉字）；英文最大支持500个字母（半角标点符号算一个字母）。
     * @param codec            返回音频格式，可取值：wav（默认），mp3，pcm
     * @param volume           音量大小，范围：[0，10]，分别对应11个等级的音量，默认为0，代表正常音量。没有静音选项。
     * @param speed            语速，范围：[-2，6]，分别对应不同语速：
     *                         -2 代表 0.6 倍
     *                         -1 代表 0.8 倍
     *                         0 代表 1.0 倍（默认）
     *                         1 代表 1.2 倍
     *                         2 代表 1.5 倍
     *                         6 代表 2.5 倍
     *                         如果需要更细化的语速，可以保留小数点后一位，例如0.5 1.1 1.8等。
     * @param emotionCategory  控制合成音频的情感，仅支持多情感音色使用。取值: neutral(中性)、sad(悲伤)、happy(高兴)、angry(生气)、fear(恐惧)、news(新闻)、story(故事)、radio(广播)、poetry(诗歌)、call(客服)
     * @param emotionIntensity 控制合成音频情感程度，取值范围为[50,200],默认为100，不填写为默认值；只有EmotionCategory不为空时生效；
     * @param primaryLanguage  主语言类型：1-中文（默认）2-英文
     * @param sampleRate       音频采样率：16000：16k（默认） 8000：8k
     * @return TextToVoiceResponse
     */
    public TextToVoiceResponse textToVoice(String text, VoiceType voiceType, CodecType codec, float volume, float speed, EmotionCategory emotionCategory, Long emotionIntensity, long primaryLanguage, SampleRate sampleRate) {
        TextToVoiceRequest req = new TextToVoiceRequest();
        req.setProjectId(projectId);
        req.setSessionId(createdSessionId()); // 一次请求对应一个SessionId，会原样返回，建议传入类似于uuid的字符串防止重复。
        req.setText(text);
        req.setCodec(codec.name());
        req.setSpeed(shrinkSpeed(speed));
        req.setVolume(shrinkVolume(volume));
        req.setVoiceType(voiceType.code);
        req.setEmotionCategory(emotionCategory.name());
        req.setEmotionIntensity(shrinkEmotionIntensity(emotionIntensity));
        req.setPrimaryLanguage(primaryLanguage);
        req.setSampleRate(sampleRate.sampleRate);
        req.setEnableSubtitle(enableSubtitle);
        req.setModelType(modelType);
        req.setSegmentRate(segmentRate);

        TextToVoiceResponse resp = null;
        try {
            resp = ttsClient.TextToVoice(req);
        } catch (TencentCloudSDKException e) {
            throw new TencentException(e);
        }
        log.debug(TextToVoiceResponse.toJsonString(resp));
        return resp;
    }

    public CreateTtsTaskResponse createTtsTask(String text, String callbackUrl) {
        return createTtsTask(text, VoiceType.BZ_TY, CodecType.mp3, 0, 0, 1, SampleRate.$16000, callbackUrl, false);
    }


    /**
     * 长文本语音合成
     * <p>
     * 接口请求域名： tts.tencentcloudapi.com 。<p/>
     * <p>
     * 本接口服务对10万字符以内的文本进行语音合成，异步返回音频结果。满足一次性合成较长文本的客户需求，如阅读播报、新闻媒体等场景。 <p/>
     * - 支持音频格式：mp3,wav,pcm <br/>
     * - 支持音频采样率：16000 Hz, 8000 Hz <br/>
     * - 支持中文普通话、英文、中英文混读、粤语合成 <br/>
     * - 支持语速、音量设置 <br/>
     * - 支持回调或轮询的方式获取结果，结果获取请参考 长文本语音合成结果查询。 <br/>
     * - 提交长文本语音合成请求后，合成结果在3小时内完成，音频文件在服务端可保存24小时 <p/>
     * <p>
     * 长文本合成支持 SSML，语法详见 SSML 标记语言，使用时需满足如下使用规范： <br/>
     * - 使用 SSML 标签，需置于 speak 闭合标签内部； <br/>
     * - 合成文本可包含多组 speak 闭合标签，且无数量限制； <br/>
     * - 每个 speak 闭合标签内部，字符数不超过 150 字（标签字符本身不计算在内）； <br/>
     * - 每个 speak 闭合标签内部，使用 break 标签数目最大为 10 个。如需要使用更多，可拆解到多个 speak 标签中； <br/>
     * - 默认接口请求频率限制：20次/秒。
     *
     * @param callbackUrl            回调 URL，用户自行搭建的用于接收识别结果的服务URL。如果用户使用轮询方式获取识别结果，则无需提交该参数。回调说明
     * @param voiceoverDialogueSplit 旁白与对白文本解析，分别合成相应风格（仅适用于旁对白音色），默认 false
     * @return CreateTtsTaskResponse
     */
    public CreateTtsTaskResponse createTtsTask(String text, VoiceType voiceType, CodecType codec, float volume, float speed, long primaryLanguage, SampleRate sampleRate, String callbackUrl, boolean voiceoverDialogueSplit) {
        CreateTtsTaskRequest req = new CreateTtsTaskRequest();
        req.setProjectId(projectId);
        req.setText(text);
        req.setCodec(codec.name());
        req.setSpeed(shrinkSpeed(speed));
        req.setVolume(shrinkVolume(volume));
        req.setVoiceType(voiceType.code);
        req.setPrimaryLanguage(primaryLanguage);
        req.setSampleRate(sampleRate.sampleRate);
        req.setModelType(modelType);
        req.setCallbackUrl(callbackUrl);
        req.setVoiceoverDialogueSplit(voiceoverDialogueSplit);

        CreateTtsTaskResponse resp = null;
        try {
            resp = ttsClient.CreateTtsTask(req);
        } catch (TencentCloudSDKException e) {
            throw new TencentException(e);
        }
        log.debug(CreateTtsTaskResponse.toJsonString(resp));
        return resp;
    }

    /**
     * <pre>
     * 接口请求域名： tts.tencentcloudapi.com 。
     *
     * 在调用长文本语音合成请求接口后，有回调和轮询两种方式获取识别结果。
     * - 当采用回调方式时，合成完毕后会将结果通过 POST 请求的形式通知到用户在请求时填写的回调 URL，具体请参见 长文本语音合成结果查询 。
     * - 当采用轮询方式时，需要主动提交任务ID来轮询识别结果，共有任务成功、等待、执行中和失败四种结果，具体信息请参见下文说明。
     *
     * 默认接口请求频率限制：20次/秒。
     * </pre>
     *
     * @param taskId 任务id
     * @return DescribeTtsTaskStatusResponse
     */
    public DescribeTtsTaskStatusResponse describeTtsTaskStatus(String taskId) {
        DescribeTtsTaskStatusRequest req = new DescribeTtsTaskStatusRequest();
        req.setTaskId(taskId);
        DescribeTtsTaskStatusResponse resp = null;
        try {
            resp = ttsClient.DescribeTtsTaskStatus(req);
        } catch (TencentCloudSDKException e) {
            throw new TencentException(e);
        }
        log.debug(DescribeTtsTaskStatusResponse.toJsonString(resp));
        return resp;
    }

    public DescribeTtsTaskStatusResponse createTtsTaskAsync(String text) throws ExecutionException, InterruptedException {
        return createTtsTaskAsync(text, VoiceType.BZ_TY, CodecType.mp3, 0, 0, 1, SampleRate.$16000, false);
    }

    public DescribeTtsTaskStatusResponse createTtsTaskAsync(String text, VoiceType voiceType, CodecType codec, float volume, float speed, long primaryLanguage, SampleRate sampleRate, boolean voiceoverDialogueSplit) throws ExecutionException, InterruptedException {
        CreateTtsTaskResponse ttsTask = createTtsTask(text, voiceType, codec, volume, speed, primaryLanguage, sampleRate, null, voiceoverDialogueSplit);
        String taskId = ttsTask.getData().getTaskId();
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        final ListenableFuture<DescribeTtsTaskStatusResponse> listenableFuture = executorService.submit(() -> {
            Long status = 0L;
            DescribeTtsTaskStatusResponse response;
            do {
                TimeUnit.SECONDS.sleep(1);
                response = describeTtsTaskStatus(taskId);
                status = response.getData().getStatus();
            } while (status == 0L || status == 1L);
            return response;
        });
        return listenableFuture.get();
    }

    public void newSpeechSynthesizer(String text, VoiceType voiceType, CodecType codec, float volume, float speed, long primaryLanguage, SampleRate sampleRate) {
        SpeechSynthesisRequest req = SpeechSynthesisRequest.initialize();
        req.setProjectId(projectId.intValue());
        req.setSessionId(createdSessionId());
        req.setCodec(codec.name());
        req.setSpeed(shrinkSpeed(speed));
        req.setVolume((int) shrinkVolume(volume));
        req.setVoiceType((int) voiceType.code);
        req.setPrimaryLanguage((int) primaryLanguage);
        req.setSampleRate((int) sampleRate.sampleRate);
        req.setModelType((int) modelType);
        // req.setExpired();
        // req.setExtendsParam();
        // req.setTimestamp();
        // req.setStrToBeEncoded();

        //使用客户端client创建语音合成实例
        SpeechSynthesizer speechSynthesizer = speechClient.newSpeechSynthesizer(req, new MySpeechSynthesizerListener());
        speechSynthesizer.synthesis(text);
    }

    public static class MySpeechSynthesizerListener extends SpeechSynthesisListener {

        private static String codec = "pcm";
        private static int sampleRate = 16000;

        private AtomicInteger sessionId = new AtomicInteger(0);

        @Override
        public void onComplete(SpeechSynthesisResponse response) {
            System.out.println("onComplete");
            if (response.getSuccess()) {
                //根据具体的业务选择逻辑处理
                //Ttsutils.saveResponseToFile(response.getAudio(),"./111.mp3");
                if ("pcm".equals(codec)) {//pcm 转 wav
                    Ttsutils.responsePcm2Wav(sampleRate, response.getAudio(), response.getSessionId());
                }
                if ("opus".equals(codec)) {//opus
                    System.out.println("OPUS:" + response.getSessionId() + " length:" + response.getAudio().length);
                }
            }
            System.out.println("结束：" + response.getSuccess() + " " + response.getCode() + " " + response.getMessage() + " " + response.getEnd());
        }

        //语音合成的语音二进制数据
        @Override
        public void onMessage(byte[] data) {
            //System.out.println("onMessage:" + data.length); Your own logic.
            System.out.println("onMessage length:" + data.length);
            sessionId.incrementAndGet();
        }

        @Override
        public void onFail(SpeechSynthesisResponse response) {
            System.out.println("onFail");
        }
    }

    private float shrinkSpeed(float speed) {
        return speed < -2 ? -2f : (speed > 6 ? 6F : speed);
    }

    private float shrinkVolume(float volume) {
        return volume < 0 ? 0F : (volume > 10 ? 10F : volume);
    }

    private long shrinkEmotionIntensity(long emotionIntensity) {
        return emotionIntensity < 50 ? 50L : (emotionIntensity > 200 ? 200L : emotionIntensity);
    }

    private String createdSessionId() {
        return UUID.randomUUID().toString();
    }

}
