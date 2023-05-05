package club.ensoul.framework.aliyun.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.google.gson.Gson;
import club.ensoul.framework.aliyun.sms.exception.AliyunSmsSendException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
public class AliyunSmsSendTemplate {

    @Getter
    private static Client client;

    private final static Gson gson = new Gson();

    public AliyunSmsSendTemplate(Client client) {
        AliyunSmsSendTemplate.client = client;
    }

    public static Sender builderSender() {
        return new Sender(client);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode 短信模板ID
     * @param signName     短信签名名称
     * @param phoneNumbers 接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, String signName, Collection<String> phoneNumbers) {
        return sendMessage(templateCode, "", signName, null, null, phoneNumbers);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode  短信模板ID
     * @param templateParam 短信模板变量对应的实际值，JSON格式。支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
     * @param signName      短信签名名称
     * @param phoneNumbers  接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, String templateParam, String signName, Collection<String> phoneNumbers) {
        return sendMessage(templateCode, templateParam, signName, null, null, phoneNumbers);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode   短信模板ID
     * @param templateParams 短信模板变量对应的实际值，键值对格式
     * @param signName       短信签名名称
     * @param phoneNumbers   接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, Map<String, Object> templateParams, String signName, Collection<String> phoneNumbers) {
        return sendMessage(templateCode, templateParams, signName, null, null, phoneNumbers);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode  短信模板ID
     * @param templateParam 短信模板变量对应的实际值，JSON格式。支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
     * @param signName      短信签名名称
     * @param outId         外部流水扩展字段, 可以为空
     * @param phoneNumbers  接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, String templateParam, String signName, String outId, Collection<String> phoneNumbers) {
        return sendMessage(templateCode, templateParam, signName, outId, null, phoneNumbers);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode   短信模板ID
     * @param templateParams 短信模板变量对应的实际值，键值对格式
     * @param signName       短信签名名称
     * @param outId          外部流水扩展字段, 可以为空
     * @param phoneNumbers   接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, Map<String, Object> templateParams, String signName, String outId, Collection<String> phoneNumbers) {
        return sendMessage(templateCode, templateParams, signName, outId, null, phoneNumbers);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode    短信模板ID
     * @param templateParams  短信模板变量对应的实际值，键值对格式
     * @param signName        短信签名名称
     * @param outId           外部流水扩展字段, 可以为空
     * @param smsUpExtendCode 上行短信扩展码，上行短信，指发送给通信服务提供商的短信，用于定制某种服务、完成查询，或是办理某种业务等，需要收费的，按运营商普通短信资费进行扣费
     * @param phoneNumbers    接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, Map<String, Object> templateParams, String signName, String outId, String smsUpExtendCode, Collection<String> phoneNumbers) {
        return sendMessage(templateCode, gson.toJson(templateParams), signName, outId, smsUpExtendCode, phoneNumbers);
    }

    /**
     * 发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode    短信模板ID
     * @param templateParam   短信模板变量对应的实际值，JSON格式。支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
     * @param signName        短信签名名称
     * @param outId           外部流水扩展字段, 可以为空
     * @param smsUpExtendCode 上行短信扩展码，上行短信，指发送给通信服务提供商的短信，用于定制某种服务、完成查询，或是办理某种业务等，需要收费的，按运营商普通短信资费进行扣费
     * @param phoneNumbers    接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, Collection)}
     */
    public SendSmsResponseBody sendMessage(String templateCode, String templateParam, String signName, String outId, String smsUpExtendCode, Collection<String> phoneNumbers) {

        if (CollectionUtils.isEmpty(phoneNumbers)) {
            throw new AliyunSmsSendException("aliyun sms: args phoneNumbers is null");
        }
        if (phoneNumbers.size() > 1000) {
            throw new AliyunSmsSendException("aliyun sms: phoneNumbers length is large, max 1000");
        }

        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(String.join(",", phoneNumbers));
        sendSmsRequest.setOutId(outId);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParam(templateParam);
        sendSmsRequest.setSignName(signName);
        sendSmsRequest.setSmsUpExtendCode(smsUpExtendCode);

        try {
            return client.sendSms(sendSmsRequest).getBody();
        } catch (Exception e) {
            throw new AliyunSmsSendException(e);
        }
    }

    /**
     * 批量发送短信, 该方法参数基本为SDK原始类型
     *
     * @param templateCode        短信模板ID
     * @param templateParamJson   短信模板变量对应的实际值，JSON格式。支持传入多个参数，示例：{"name":"张三","number":"15038****76"}
     * @param signNameJson        短信签名名称
     * @param smsUpExtendCodeJson 上行短信扩展码，上行短信，指发送给通信服务提供商的短信，用于定制某种服务、完成查询，或是办理某种业务等，需要收费的，按运营商普通短信资费进行扣费
     * @param phoneNumberJson     接收短信的手机号码。格式：无任何前缀的11位手机号码，例如1381111****。支持对多个手机号码发送短信，手机号码之间以半角逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟
     * @return {@link SendBatchSmsResponseBody}<p/>
     * {@link #sendMessage(String, String, String, String, Collection)}
     */
    public SendBatchSmsResponseBody sendBatchMessage(String templateCode, String templateParamJson, String signNameJson, String smsUpExtendCodeJson, String phoneNumberJson) {

        SendBatchSmsRequest sendSmsRequest = new SendBatchSmsRequest();
        sendSmsRequest.setPhoneNumberJson(phoneNumberJson);
        sendSmsRequest.setTemplateCode(templateCode);
        sendSmsRequest.setTemplateParamJson(templateParamJson);
        sendSmsRequest.setSignNameJson(signNameJson);
        sendSmsRequest.setSmsUpExtendCodeJson(smsUpExtendCodeJson);

        try {
            return client.sendBatchSms(sendSmsRequest).getBody();
        } catch (Exception e) {
            throw new AliyunSmsSendException(e);
        }
    }

    public static class Sender {

        private final Client client;

        private String templateCode;
        private String signName;
        private String outId;
        private String smsUpExtendCode;

        private final Map<String, Object> templateParams = new HashMap<>();
        private final List<String> phoneNumbers = new ArrayList<>();
        private final SendSmsRequest sendSmsRequest = new SendSmsRequest();

        Sender(Client client) {
            this.client = client;
        }

        public Sender templateCode(String templateCode) {
            Assert.hasLength(templateCode, "aliyun sms: templateCode is null");
            this.templateCode = templateCode;
            return this;
        }

        public Sender signName(String signName) {
            Assert.hasLength(signName, "aliyun sms: signName is null");
            this.signName = signName;
            return this;
        }


        public Sender outId(String outId) {
            this.outId = outId;
            return this;
        }

        public Sender smsUpExtendCode(String smsUpExtendCode) {
            this.smsUpExtendCode = smsUpExtendCode;
            return this;
        }

        public Sender templateParam(String name, Object value) {
            Assert.hasLength(name, "aliyun sms: templateParam name is null");
            templateParams.put(name, value);
            return this;
        }

        public Sender templateParamWithSortUrl(String name, String url, String shortUrlName, int effectiveDays) {
            Assert.hasLength(name, "aliyun sms: templateParam name is null");
            Assert.hasLength(name, "aliyun sms: templateParam value is null");

            AddShortUrlRequest shortUrlRequest = new AddShortUrlRequest();
            shortUrlRequest.setShortUrlName(shortUrlName);
            shortUrlRequest.setEffectiveDays(effectiveDays + "");
            shortUrlRequest.setSourceUrl(url);
            try {
                AddShortUrlResponse shortUrl = client.addShortUrl(shortUrlRequest);
                templateParams.put(name, shortUrl);
            } catch (Exception e) {
                throw new AliyunSmsSendException(e);
            }
            return this;
        }

        public Sender phoneNumbers(String phoneNumber) {
            Assert.hasLength(phoneNumber, "aliyun sms: phoneNumber is null");
            phoneNumbers.add(phoneNumber);
            return this;
        }


        public SendSmsRequest getBody() {
            return sendSmsRequest;
        }

        public SendSmsResponseBody send() {
            Assert.hasLength(templateCode, "aliyun sms: templateCode is null");
            Assert.hasLength(signName, "aliyun sms: templateCode is null");
            Assert.notEmpty(templateParams, "aliyun sms: templateParams is empty");
            Assert.notEmpty(phoneNumbers, "aliyun sms: phoneNumbers is empty");

            sendSmsRequest.setTemplateCode(templateCode);
            sendSmsRequest.setSignName(signName);
            sendSmsRequest.setOutId(outId);
            sendSmsRequest.setSmsUpExtendCode(smsUpExtendCode);

            try {
                sendSmsRequest.setTemplateParam(gson.toJson(templateParams));
                sendSmsRequest.setPhoneNumbers(String.join(",", phoneNumbers));
                return client.sendSms(sendSmsRequest).getBody();
            } catch (Exception e) {
                throw new AliyunSmsSendException(e);
            }
        }

    }

}
