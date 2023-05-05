package club.ensoul.framework.tencent.speech.conts;

/**
 * @author wy_peng_chen6
 */
public enum SampleRate {

    $16000(16000),
    $8000(8000);

    public final long sampleRate;

    SampleRate(long sampleRate) {
        this.sampleRate = sampleRate;
    }
}
