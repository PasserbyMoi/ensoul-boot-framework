package club.ensoul.framework.azure.speech;

import club.ensoul.framework.azure.speech.conts.SpeechSynthesisVoice;
import club.ensoul.framework.azure.speech.domain.SpeechResult;
import club.ensoul.framework.azure.speech.exception.AzureSpeechException;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * MQ简易发送类
 *
 * @author wy_peng_chen6
 */
@Slf4j
public class AzureSpeechTemplate {

    /**
     * 将语音合成到文件中
     *
     * @param content        文本内容
     * @param outFile        输出文件对象
     * @param synthesisVoice 合成语言和声音名称
     */
    public SpeechResult speechToFile(@NonNull String content, boolean ssml, @NonNull File outFile, @NonNull SpeechSynthesisVoice synthesisVoice) {
        return speechToFile(content, ssml, outFile.getPath(), synthesisVoice, null);
    }

    /**
     * 将语音合成到文件中-异步
     *
     * @param content        文本内容
     * @param outFile        输出文件对象
     * @param synthesisVoice 合成语言和声音名称
     */
    public Future<SpeechSynthesisResult> speechToFileAsync(@NonNull String content, boolean ssml, @NonNull File outFile, @NonNull SpeechSynthesisVoice synthesisVoice) {
        return speechToFileAsync(content, ssml, outFile.getPath(), synthesisVoice, null);
    }

    /**
     * 将语音合成到文件中
     *
     * @param content        文本内容
     * @param outFile        输出文件对象
     * @param synthesisVoice 合成语言和声音名称
     * @param outputFormat   输出文件格式
     */
    public SpeechResult speechToFile(@NonNull String content, boolean ssml, @NonNull File outFile,
                                     @NonNull SpeechSynthesisVoice synthesisVoice,
                                     @Nullable SpeechSynthesisOutputFormat outputFormat) {
        return speechToFile(content, ssml, outFile.getPath(), synthesisVoice, outputFormat);
    }

    /**
     * 将语音合成到文件中-异步
     *
     * @param content        文本内容
     * @param outFile        输出文件对象
     * @param synthesisVoice 合成语言和声音名称
     * @param outputFormat   输出文件格式
     */
    public Future<SpeechSynthesisResult> speechToFileAsync(@NonNull String content, boolean ssml, @NonNull File outFile,
                                                           @NonNull SpeechSynthesisVoice synthesisVoice,
                                                           @Nullable SpeechSynthesisOutputFormat outputFormat) {
        return speechToFileAsync(content, ssml, outFile.getPath(), synthesisVoice, outputFormat);
    }

    /**
     * 将语音合成到文件中
     *
     * @param content        文本内容
     * @param outFilePath    输出文件路径
     * @param synthesisVoice 合成语言和声音名称
     * @param outputFormat   输出文件格式
     */
    public SpeechResult speechToFile(@NonNull String content, boolean ssml, @NonNull String outFilePath,
                                     @NonNull SpeechSynthesisVoice synthesisVoice,
                                     @Nullable SpeechSynthesisOutputFormat outputFormat) {
        SpeechConfigHelper speechConfigHelper = SpeechConfigHelper.builder()
                .voice(synthesisVoice)
                .outputFormat(outputFormat)
                .build();
        return speechToFile(content, ssml, outFilePath, speechConfigHelper.getSpeechConfig());
    }

    /**
     * 将语音合成到文件中-异步
     *
     * @param content        文本内容
     * @param outFilePath    输出文件路径
     * @param synthesisVoice 合成语言和声音名称
     * @param outputFormat   输出文件格式
     */
    public Future<SpeechSynthesisResult> speechToFileAsync(@NonNull String content, boolean ssml, @NonNull String outFilePath,
                                                           @NonNull SpeechSynthesisVoice synthesisVoice,
                                                           @Nullable SpeechSynthesisOutputFormat outputFormat) {
        SpeechConfigHelper speechConfigHelper = SpeechConfigHelper.builder()
                .voice(synthesisVoice)
                .outputFormat(outputFormat)
                .build();
        return speechToFileAsync(content, ssml, outFilePath, speechConfigHelper.getSpeechConfig());
    }

    /**
     * 将语音合成到文件中
     *
     * @param content      文本内容
     * @param outFilePath  输出文件路径
     * @param speechConfig 语音合成配置
     */
    public SpeechResult speechToFile(@NonNull String content, boolean ssml, @NonNull String outFilePath, SpeechConfig speechConfig) {
        Future<SpeechSynthesisResult> resultFuture = speechToFileAsync(content, ssml, outFilePath, speechConfig);
        SpeechSynthesisResult result = null;
        try {
            result = resultFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new AzureSpeechException(e);
        }
        return SpeechResult.fromSynthesis(result);
    }

    /**
     * 将语音合成到文件中-异步
     *
     * @param content      文本内容
     * @param outFilePath  输出文件路径
     * @param speechConfig 语音合成配置
     */
    public Future<SpeechSynthesisResult> speechToFileAsync(@NonNull String content, boolean ssml,
                                                           @NonNull String outFilePath, SpeechConfig speechConfig) {
        AudioConfig audioConfig = AudioConfig.fromWavFileOutput(outFilePath);
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfig, audioConfig);
        Future<SpeechSynthesisResult> result;
        if (ssml) {
            result = speechSynthesizer.SpeakSsmlAsync(content);
        } else {
            result = speechSynthesizer.SpeakTextAsync(content);
        }
        speechSynthesizer.close();
        return result;
    }

    /**
     * 合成到扬声器输出
     *
     * @param content        文本内容
     * @param synthesisVoice 合成语言和声音名称
     */
    public SpeechResult speechToMicrophone(@NonNull String content, boolean ssml, @NonNull SpeechSynthesisVoice synthesisVoice) {
        SpeechConfigHelper speechConfigHelper = SpeechConfigHelper.builder()
                .voice(synthesisVoice)
                .build();
        return speechToMicrophone(content, ssml, null, speechConfigHelper.getSpeechConfig());
    }

    /**
     * 合成到扬声器输出
     *
     * @param content        文本内容
     * @param outDevId       输出设备
     * @param synthesisVoice 合成语言和声音名称
     */
    public SpeechResult speechToMicrophone(String content, boolean ssml, @Nullable String outDevId,
                                           SpeechSynthesisVoice synthesisVoice) {
        SpeechConfigHelper speechConfigHelper = SpeechConfigHelper.builder()
                .voice(synthesisVoice)
                .build();
        return speechToMicrophone(content, ssml, outDevId, speechConfigHelper.getSpeechConfig());
    }

    /**
     * 合成到扬声器输出
     *
     * @param content      文本内容
     * @param outDevId     输出设备
     * @param speechConfig 语音合成配置
     */
    public SpeechResult speechToMicrophone(String content, boolean ssml, @Nullable String outDevId, SpeechConfig speechConfig) {
        AudioConfig audioConfig;
        if (StringUtils.hasText(outDevId)) {
            audioConfig = AudioConfig.fromMicrophoneInput(outDevId);
        } else {
            audioConfig = AudioConfig.fromDefaultSpeakerOutput();
        }
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfig, audioConfig);
        SpeechSynthesisResult result;
        if (ssml) {
            result = speechSynthesizer.SpeakSsml(content);
        } else {
            result = speechSynthesizer.SpeakText(content);
        }
        return SpeechResult.fromSynthesis(result);
    }

    /**
     * 从文件获取内容
     *
     * @param filePath            文件路径
     * @param synthesisVoice      合成语言和声音名称
     * @param outputFormat        输出格式
     * @param ssml                是否为 ssml
     * @param synthesizerListener 事件回调
     */
    public AudioDataStream speechFromFilePath(String filePath, boolean ssml, @NonNull SpeechSynthesisVoice synthesisVoice,
                                              @Nullable SpeechSynthesisOutputFormat outputFormat, SpeechSynthesizerListener synthesizerListener) {
        return speechToStream(xmlToString(filePath), ssml, synthesisVoice, outputFormat, synthesizerListener);
    }

    /**
     * 从文件获取内容
     *
     * @param file                文件对象
     * @param synthesisVoice      合成语言和声音名称
     * @param outputFormat        输出格式
     * @param ssml                是否为 ssml
     * @param synthesizerListener 事件回调
     */
    public AudioDataStream speechFromFile(File file, boolean ssml, @NonNull SpeechSynthesisVoice synthesisVoice,
                                          @Nullable SpeechSynthesisOutputFormat outputFormat, @Nullable SpeechSynthesizerListener synthesizerListener) {
        return speechToStream(xmlToString(file), ssml, synthesisVoice, outputFormat, synthesizerListener);
    }

    /**
     * 获取流
     *
     * @param content             内容
     * @param synthesisVoice      合成语言和声音名称
     * @param ssml                是否为 ssml
     * @param synthesizerListener 事件回调
     */
    public AudioDataStream speechToStream(String content, boolean ssml, @NonNull SpeechSynthesisVoice synthesisVoice, SpeechSynthesizerListener synthesizerListener) {
        return speechToStream(content, ssml, synthesisVoice, null, synthesizerListener);
    }



    /**
     * 获取流
     *
     * @param content             内容
     * @param synthesisVoice      合成语言和声音名称
     * @param outputFormat        输出格式
     * @param ssml                是否为 ssml
     * @param synthesizerListener 事件回调
     */
    public AudioDataStream speechToStream(String content, boolean ssml, @NonNull SpeechSynthesisVoice synthesisVoice,
                                          @Nullable SpeechSynthesisOutputFormat outputFormat, @Nullable SpeechSynthesizerListener synthesizerListener) {
        SpeechConfigHelper speechConfigHelper = SpeechConfigHelper.builder()
                .voice(synthesisVoice)
                .outputFormat(outputFormat)
                .listener(synthesizerListener)
                .build();
        SpeechSynthesizer speechSynthesizer = new SpeechSynthesizer(speechConfigHelper.getSpeechConfig(), null);
        speechConfigHelper.listenerBind(speechSynthesizer);
        SpeechSynthesisResult result;
        if (ssml) {
            result = speechSynthesizer.SpeakSsml(content);
        } else {
            result = speechSynthesizer.SpeakText(content);
        }
        speechSynthesizer.close();
        return AudioDataStream.fromResult(result);
    }

    private String xmlToString(String filePath) {
        File file = new File(filePath);
        return xmlToString(file);
    }

    private String xmlToString(File file) {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(System.lineSeparator());
            }
            return fileContents.toString().trim();
        } catch (FileNotFoundException ex) {
            return "File not found.";
        }
    }

}
