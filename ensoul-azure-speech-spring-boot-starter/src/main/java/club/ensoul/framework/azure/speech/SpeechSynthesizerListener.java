package club.ensoul.framework.azure.speech;

import com.microsoft.cognitiveservices.speech.SpeechSynthesisBookmarkEventArgs;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisEventArgs;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisVisemeEventArgs;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisWordBoundaryEventArgs;
import com.microsoft.cognitiveservices.speech.util.EventHandler;

/**
 * @author wy_peng_chen6
 */
public interface SpeechSynthesizerListener {

    /**
     * 指示语音合成已开始。可以确认合成是何时开始的。
     *
     * @return EventHandler<SpeechSynthesisEventArgs>
     */
    EventHandler<SpeechSynthesisEventArgs> started();

    /**
     * 指示已取消语音合成。可以确认何时取消了合成。
     *
     * @return EventHandler<SpeechSynthesisEventArgs>
     */
    EventHandler<SpeechSynthesisEventArgs> canceled();

    /**
     * 指示已取完成音合成。可以确认何时完成了合成。
     *
     * @return EventHandler<SpeechSynthesisEventArgs>
     */
    EventHandler<SpeechSynthesisEventArgs> completed();

    /**
     * 指示语音合成正在进行。 每次 SDK 从语音服务收到音频区块，都会触发此事件。可以确认合成何时正在进行。
     *
     * @return EventHandler<SpeechSynthesisEventArgs>
     */
    EventHandler<SpeechSynthesisEventArgs> synthesizing();

    /**
     * 指示已进入书签。 若要触发书签进入事件，需要在 SSML 中指定一个 bookmark 元素。 此事件会报告输出音频在合成开始处到 bookmark 元素之间经历的时间。
     * 该事件的 Text 属性是在书签的 mark 特性中设置的字符串值。 不会读出 bookmark 元素。
     * 可以使用 bookmark 元素在 SSML 中插入自定义标记，以获得音频流中每个标记的偏移量。 bookmark 元素可用于引用文本或标记序列中的特定位置。
     *
     * @return EventHandler<SpeechSynthesisBookmarkEventArgs>
     */
    EventHandler<SpeechSynthesisBookmarkEventArgs> bookmarkReached();

    /**
     * 指示已收到嘴形视位事件。	视素通常用于表示观察到的语音中的关键姿态。 关键姿态包括在产生特定音素时嘴唇、下巴和舌头的位置。
     * 可以使用嘴形视位来来动画显示播放语音音频时人物的面部。
     * @return EventHandler<SpeechSynthesisVisemeEventArgs>
     */
    EventHandler<SpeechSynthesisVisemeEventArgs> visemeReceived();

    /**
     * 指示已收到字边界。 在每个新的讲述字词、标点和句子的开头引发此事件。 该事件报告当前字词从输出音频开始的时间偏移(以时钟周期为单位)。
     * 此事件还会报告输入文本（或 SSML）中紧靠在要说出的字词之前的字符位置。
     * 此事件通常用于获取文本和相应音频的相对位置。 你可能想要知道某个新字词，然后根据时间采取操作。
     * 例如，可以获取所需的信息来帮助确定在说出字词时，何时突出显示这些字词，以及要突出显示多长时间。
     * @return EventHandler<SpeechSynthesisWordBoundaryEventArgs>
     */
    EventHandler<SpeechSynthesisWordBoundaryEventArgs> wordBoundary();
}
