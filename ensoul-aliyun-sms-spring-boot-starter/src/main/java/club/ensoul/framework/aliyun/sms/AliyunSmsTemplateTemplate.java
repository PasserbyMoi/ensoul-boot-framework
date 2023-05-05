package club.ensoul.framework.aliyun.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import club.ensoul.framework.aliyun.sms.exception.AliyunSmsTemplateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliyunSmsTemplateTemplate {

    @Getter
    private static Client client;

    public AliyunSmsTemplateTemplate(Client client) {
        AliyunSmsTemplateTemplate.client = client;
    }

    /**
     * 创建短信模板 <br/>
     *
     * @param templateName    模板名称，长度为1~30个字符
     * @param templateType    短信类型。取值：
     *                        <blockquote>  0：验证码。</blockquote>
     *                        <blockquote>  1：短信通知。</blockquote>
     *                        <blockquote>  2：推广短信。</blockquote>
     *                        <blockquote>  3：国际/港澳台消息。</blockquote>
     * @param templateContent 模板内容，长度为1~500个字符
     * @param remark          短信模板申请说明。请在申请说明中描述您的业务使用场景，长度为1~100个字符
     * @return {@link AddSmsTemplateResponseBody}<p/>
     */
    public AddSmsTemplateResponseBody addSmsTemplate(String templateName, Integer templateType, String templateContent, String remark) {
        AddSmsTemplateRequest shortUrlRequest = new AddSmsTemplateRequest();
        shortUrlRequest.setTemplateName(templateName);
        shortUrlRequest.setTemplateType(templateType);
        shortUrlRequest.setTemplateContent(templateContent);
        shortUrlRequest.setRemark(remark);
        try {
            AddSmsTemplateResponse smsTemplateResponse = client.addSmsTemplate(shortUrlRequest);
            return smsTemplateResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }

    /**
     * 修改未通过审核的短信模板。 <br/>
     *
     * @param templateName    模板名称，长度为1~30个字符
     * @param templateType    短信类型。取值：
     *                        <blockquote>  0：验证码。</blockquote>
     *                        <blockquote>  1：短信通知。</blockquote>
     *                        <blockquote>  2：推广短信。</blockquote>
     *                        <blockquote>  3：国际/港澳台消息。</blockquote>
     * @param templateContent 模板内容，长度为1~500个字符
     * @param remark          短信模板申请说明。请在申请说明中描述您的业务使用场景，长度为1~100个字符
     * @return {@link ModifySmsTemplateResponseBody}<p/>
     */
    public ModifySmsTemplateResponseBody modifySmsTemplate(String templateCode, String templateName, Integer templateType, String templateContent, String remark) {
        ModifySmsTemplateRequest modifySmsTemplateRequest = new ModifySmsTemplateRequest();
        modifySmsTemplateRequest.setTemplateCode(templateCode);
        modifySmsTemplateRequest.setTemplateName(templateName);
        modifySmsTemplateRequest.setTemplateType(templateType);
        modifySmsTemplateRequest.setTemplateContent(templateContent);
        modifySmsTemplateRequest.setRemark(remark);
        try {
            ModifySmsTemplateResponse smsTemplateResponse = client.modifySmsTemplate(modifySmsTemplateRequest);
            return smsTemplateResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }

    /**
     * 删除短信模板 <br/>
     *
     * @param templateCode 短信模板CODE
     * @return {@link DeleteSmsTemplateResponseBody}<p/>
     */
    public DeleteSmsTemplateResponseBody deleteSmsTemplate(String templateCode) {
        DeleteSmsTemplateRequest smsTemplateRequest = new DeleteSmsTemplateRequest();
        smsTemplateRequest.setTemplateCode(templateCode);
        try {
            DeleteSmsTemplateResponse smsTemplateResponse = client.deleteSmsTemplate(smsTemplateRequest);
            return smsTemplateResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }

    /**
     * 查询短信模板状态 <br/>
     *
     * @param templateCode 短信模板CODE
     * @return {@link QuerySmsTemplateResponseBody}<p/>
     */
    public QuerySmsTemplateResponseBody querySmsTemplate(String templateCode) {
        QuerySmsTemplateRequest smsTemplateRequest = new QuerySmsTemplateRequest();
        smsTemplateRequest.setTemplateCode(templateCode);
        try {
            QuerySmsTemplateResponse smsTemplateResponse = client.querySmsTemplate(smsTemplateRequest);
            return smsTemplateResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsTemplateException(e);
        }
    }

}
