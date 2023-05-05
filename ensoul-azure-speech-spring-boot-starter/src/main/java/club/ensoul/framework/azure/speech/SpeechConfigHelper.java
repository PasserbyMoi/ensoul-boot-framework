package club.ensoul.framework.azure.speech;

import club.ensoul.framework.azure.speech.conts.SpeechSynthesisVoice;
import com.microsoft.cognitiveservices.speech.ProfanityOption;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisOutputFormat;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wy_peng_chen6
 */
@AllArgsConstructor
public class SpeechConfigHelper {

    private static final String speechKey = System.getenv("SPEECH_KEY");
    private static final String speechRegion = System.getenv("SPEECH_REGION");

    @Getter
    private SpeechConfig speechConfig;

    @Getter
    private SpeechSynthesizerListener synthesizerListener;

    public static Builder builder() {
        return new Builder();
    }

    public void listenerBind(SpeechSynthesizer speechSynthesizer) {
        speechSynthesizer.BookmarkReached.addEventListener(synthesizerListener.bookmarkReached());
        speechSynthesizer.SynthesisStarted.addEventListener(synthesizerListener.started());
        speechSynthesizer.SynthesisCompleted.addEventListener(synthesizerListener.completed());
        speechSynthesizer.SynthesisCanceled.addEventListener(synthesizerListener.canceled());
        speechSynthesizer.Synthesizing.addEventListener(synthesizerListener.synthesizing());
        speechSynthesizer.VisemeReceived.addEventListener(synthesizerListener.visemeReceived());
        speechSynthesizer.WordBoundary.addEventListener(synthesizerListener.wordBoundary());
    }

    public static class Builder {

        private final SpeechConfig speechConfig;
        private SpeechSynthesizerListener synthesizerListener;

        public Builder() {
            speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
            speechConfig.enableAudioLogging();  // 在服务中启用音频日志记录。
            speechConfig.setProfanity(ProfanityOption.Masked);  // 设置不当言论选项。
        }

        /**
         * 设置语音合成语言和声音名称
         */
        public Builder voice(SpeechSynthesisVoice synthesisVoice) {
            if (synthesisVoice != null) {
                speechConfig.setSpeechSynthesisLanguage(synthesisVoice.lang);   //
                speechConfig.setSpeechSynthesisVoiceName(synthesisVoice.voic);  // 设置语音合成的声音名称。
            }
            return this;
        }

        public Builder listener(SpeechSynthesizerListener synthesizerListener) {
            if (synthesizerListener != null) {
                this.synthesizerListener = synthesizerListener;
            }
            return this;
        }

        public Builder outputFormat(SpeechSynthesisOutputFormat outputFormat) {
            if (outputFormat != null) {
                speechConfig.setSpeechSynthesisOutputFormat(outputFormat);
            }
            return this;
        }

        public SpeechConfigHelper build() {
            return new SpeechConfigHelper(speechConfig, synthesizerListener);
        }

    }

}
