package club.ensoul.framework.azure.speech;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.util.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wy_peng_chen6
 */
@Slf4j
public abstract class DefaultSpeechSynthesizerListener implements SpeechSynthesizerListener {

    public EventHandler<SpeechSynthesisEventArgs> started() {
        return (o, eventArgs) -> {
            System.out.println("SynthesisStarted event");
        };
    }

    public EventHandler<SpeechSynthesisEventArgs> canceled() {
        return (o, eventArgs) -> {
            log.info("SynthesisCanceled event");
        };
    }

    public EventHandler<SpeechSynthesisEventArgs> completed() {
        return (o, eventArgs) -> {
            SpeechSynthesisResult result = eventArgs.getResult();
            byte[] audioData = result.getAudioData();
            System.out.println("SynthesisCompleted event:");
            System.out.println("\tAudioData: " + audioData.length + " bytes");
            System.out.println("\tAudioDuration: " + result.getAudioDuration());
            result.close();
        };

    }

    public EventHandler<SpeechSynthesisEventArgs> synthesizing() {
        return (o, eventArgs) -> {
            SpeechSynthesisResult result = eventArgs.getResult();
            byte[] audioData = result.getAudioData();
            System.out.println("Synthesizing event:");
            System.out.println("\tAudioData: " + audioData.length + " bytes");
            result.close();
        };

    }

    public EventHandler<SpeechSynthesisBookmarkEventArgs> bookmarkReached() {
        return (o, eventArgs) -> {
            log.info("BookmarkReached event:");
            log.info("\tAudioOffset: " + ((eventArgs.getAudioOffset() + 5000) / 10000) + "ms");
            log.info("\tText: " + eventArgs.getText());
        };

    }

    public EventHandler<SpeechSynthesisVisemeEventArgs> visemeReceived() {
        return (o, eventArgs) -> {
            System.out.println("VisemeReceived event:");
            System.out.println("\tAudioOffset: " + ((eventArgs.getAudioOffset() + 5000) / 10000) + "ms");
            System.out.println("\tVisemeId: " + eventArgs.getVisemeId());
        };

    }


    public EventHandler<SpeechSynthesisWordBoundaryEventArgs> wordBoundary() {
        return (o, eventArgs) -> {
            System.out.println("WordBoundary event:");
            System.out.println("\tBoundaryType: " + eventArgs.getBoundaryType());
            System.out.println("\tAudioOffset: " + ((eventArgs.getAudioOffset() + 5000) / 10000) + "ms");
            System.out.println("\tDuration: " + eventArgs.getDuration());
            System.out.println("\tText: " + eventArgs.getText());
            System.out.println("\tTextOffset: " + eventArgs.getTextOffset());
            System.out.println("\tWordLength: " + eventArgs.getWordLength());
        };

    }

}
