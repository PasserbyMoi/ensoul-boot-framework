package club.ensoul.framework.tencent.speech.domain;

import lombok.Data;

/**
 * @author wy_peng_chen6
 */
@Data
public class Callback {

    /** 任务标识 */
    private String TaskId;

    /** 任务状态码，0：任务等待，1：任务执行中，2：任务成功，3：任务失败 */
    private Integer Status;

    /** 任务状态，waiting：任务等待，doing：任务执行中，success：任务成功，failed：任务失败 */
    private String StatusStr;

    /** 合成音频链接（有效期一天） */
    private String ResultUrl;

    /** 错误描述 */
    private String ErrorMsg;

}
