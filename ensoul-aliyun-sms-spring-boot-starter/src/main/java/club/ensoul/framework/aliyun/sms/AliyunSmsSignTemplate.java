package club.ensoul.framework.aliyun.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import club.ensoul.framework.aliyun.sms.exception.AliyunSmsSignException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class AliyunSmsSignTemplate {

    @Getter
    private static Client client;

    public AliyunSmsSignTemplate(Client client) {
        AliyunSmsSignTemplate.client = client;
    }

    /**
     * 创建短信签名
     *
     * @param signName     签名名称
     * @param signSource   签名来源。取值：
     *                     <blockquote> 0：企事业单位的全称或简称。</blockquote>
     *                     <blockquote> 1：工信部备案网站的全称或简称。</blockquote>
     *                     <blockquote> 2：App应用的全称或简称。</blockquote>
     *                     <blockquote> 3：公众号或小程序的全称或简称。</blockquote>
     *                     <blockquote> 4：电商平台店铺名的全称或简称。</blockquote>
     *                     <blockquote> 5：商标名的全称或简称。</blockquote>
     * @param remark       短信签名申请说明。请在申请说明中详细描述您的业务使用场景，申请工信部备案网站的全称或简称请在此处填写域名，长度不超过200个字符。
     * @param signFileList 签名信息
     *                     </br>SignFileList.FileContents  签名的资质证明文件经base64编码后的字符串。图片不超过2 MB。个别场景下，申请签名需要上传证明文件。
     *                     </br> SignFileList.FileSuffix  签名的证明文件格式，支持上传多张图片。当前支持JPG、PNG、GIF或JPEG格式的图片。个别场景下，申请签名需要上传证明文件。
     * @return {@link AddSmsSignResponseBody}<p/>
     * @see <a href="https://help.aliyun.com/document_detail/108076.htm">个人用户签名规范</a>
     * @see <a href="https://help.aliyun.com/document_detail/108254.htm">企业用户签名规范</a>
     */
    public AddSmsSignResponseBody addSmsSign(String signName, Integer signSource, String remark, List<AddSmsSignRequest.AddSmsSignRequestSignFileList> signFileList) {
        AddSmsSignRequest smsSignRequest = new AddSmsSignRequest();
        smsSignRequest.setSignName(signName);
        smsSignRequest.setSignSource(signSource);
        smsSignRequest.setRemark(remark);
        smsSignRequest.setSignFileList(signFileList);
        try {
            AddSmsSignResponse SmsSignResponse = client.addSmsSign(smsSignRequest);
            return SmsSignResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsSignException(e);
        }
    }

    /**
     * 修改未审核通过的短信签名证明文件，并重新提交审核
     *
     * @param signName     签名名称
     * @param signSource   签名来源。取值：
     *                     <blockquote> 0：企事业单位的全称或简称。</blockquote>
     *                     <blockquote> 1：工信部备案网站的全称或简称。</blockquote>
     *                     <blockquote> 2：App应用的全称或简称。</blockquote>
     *                     <blockquote> 3：公众号或小程序的全称或简称。</blockquote>
     *                     <blockquote> 4：电商平台店铺名的全称或简称。</blockquote>
     *                     <blockquote> 5：商标名的全称或简称。</blockquote>
     * @param remark       短信签名申请说明。请在申请说明中详细描述您的业务使用场景，申请工信部备案网站的全称或简称请在此处填写域名，长度不超过200个字符。
     * @param signFileList 签名信息
     *                     </br>SignFileList.FileContents  签名的资质证明文件经base64编码后的字符串。图片不超过2 MB。个别场景下，申请签名需要上传证明文件。
     *                     </br> SignFileList.FileSuffix  签名的证明文件格式，支持上传多张图片。当前支持JPG、PNG、GIF或JPEG格式的图片。个别场景下，申请签名需要上传证明文件。
     * @return {@link ModifySmsSignResponseBody}<p/>
     * @see <a href="https://help.aliyun.com/document_detail/108076.htm">个人用户签名规范</a>
     * @see <a href="https://help.aliyun.com/document_detail/108254.htm">企业用户签名规范</a>
     */
    public ModifySmsSignResponseBody modifySmsSign(String signName, Integer signSource, String remark, List<ModifySmsSignRequest.ModifySmsSignRequestSignFileList> signFileList) {
        ModifySmsSignRequest smsSignRequest = new ModifySmsSignRequest();
        smsSignRequest.setSignName(signName);
        smsSignRequest.setSignSource(signSource);
        smsSignRequest.setRemark(remark);
        smsSignRequest.setSignFileList(signFileList);
        try {
            ModifySmsSignResponse smsSignResponse = client.modifySmsSign(smsSignRequest);
            return smsSignResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsSignException(e);
        }
    }

    /**
     * 删除短信签名 <br/>
     *
     * @param signName 短信签名
     * @return {@link DeleteSmsSignResponseBody}
     */
    public DeleteSmsSignResponseBody deleteSmsSign(String signName) {
        DeleteSmsSignRequest smsSignRequest = new DeleteSmsSignRequest();
        smsSignRequest.setSignName(signName);
        try {
            DeleteSmsSignResponse smsSignResponse = client.deleteSmsSign(smsSignRequest);
            return smsSignResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsSignException(e);
        }
    }

    /**
     * 查询短信签名状态 <br/>
     *
     * @param smsSign 签名名称
     * @return {@link QuerySmsSignResponseBody}<p/>
     */
    public QuerySmsSignResponseBody querySmsSign(String smsSign) {
        QuerySmsSignRequest smsSignRequest = new QuerySmsSignRequest();
        smsSignRequest.setSignName(smsSign);
        try {
            QuerySmsSignResponse smsSignResponse = client.querySmsSign(smsSignRequest);
            return smsSignResponse.getBody();
        } catch (Exception e) {
            throw new AliyunSmsSignException(e);
        }
    }

}
