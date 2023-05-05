package club.ensoul.framework.azure.speech.domain;

import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import lombok.Data;

@Data
public class SpeechResult {

    private int code;

    private String message;

    private String resultId;

    private long audioLength;

    private long audioDuration;

    private byte[] audioData;

    public static SpeechResult fromSynthesis(SpeechSynthesisResult result) {
        SpeechResult speechResult = new SpeechResult();
        speechResult.setResultId(result.getResultId());

        if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
            speechResult.setCode(1);
            speechResult.setResultId(result.getResultId());
            speechResult.setAudioDuration(result.getAudioDuration());
            speechResult.setAudioData(result.getAudioData());
        } else if (result.getReason() == ResultReason.NoMatch) {
            speechResult.setCode(0);
            speechResult.setMessage("语音合成失败：NoMatch");
        } else if (result.getReason() == ResultReason.Canceled) {
            speechResult.setCode(0);
            SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(result);
            speechResult.setMessage("语音合成失败：Canceled-" + cancellation.getReason().name() + " ErrorCode: " + cancellation.getErrorCode() + " ErrorDetails: " + cancellation.getErrorDetails());
        }
        return speechResult;
    }

}
