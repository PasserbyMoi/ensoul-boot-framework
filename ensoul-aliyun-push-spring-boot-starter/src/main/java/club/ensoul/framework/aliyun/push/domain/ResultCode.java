package club.ensoul.framework.aliyun.push.domain;

import java.util.Arrays;
import java.util.Optional;

public enum ResultCode {

    OK("OK", "表示接口调用成功", ""),
    isv_SMS_SIGNATURE_SCENE_ILLEGAL("isv.SMS_SIGNATURE_SCENE_ILLEGAL", "短信所使用签名场景非法", "原因：签名的适用场景与短信类型不匹配。解决方案：请选择合适的签名和模版进行短信发送。适用场景为验证码的签名可与验证码模版匹配发送。适用场景为通用的签名可与验证码、短信通知、推广短信、国际或港澳台短信模版匹配发送。"),
    isv_EXTEND_CODE_ERROR("isv.EXTEND_CODE_ERROR", "扩展码使用错误，相同的扩展码不可用于多个签名", "原因：发送短信时不同签名的短信使用了相同扩展码。解决方案：在调用短信发送接口时，不同的短信签名使用不同的扩展码。"),
    isv_DOMESTIC_NUMBER_NOT_SUPPORTED("isv.DOMESTIC_NUMBER_NOT_SUPPORTED", "国际/港澳台消息模板不支持发送境内号码", "原因：国际/港澳台消息模板仅支持发送国际、港澳台地区的号码。解决方案：如果想发送中国大陆短信，请申请国内消息短信模版。"),
    isv_DENY_IP_RANGE("isv.DENY_IP_RANGE", "源IP地址所在的地区被禁用", "原因：被系统检测到源IP属于非中国（内地）大陆地区。解决方案：请将源IP地址修改为中国（内地）大陆地区的IP地址。国际/港澳台的IP地址禁止发送中国大陆短信业务。"),
    isv_DAY_LIMIT_CONTROL("isv.DAY_LIMIT_CONTROL", "触发日发送限额", "原因：已经达到您在控制台设置的短信日发送量限额值。解决方案：如需修改限额，请在短信服务控制台左侧导航栏中单击国内消息设置>安全设置，修改发送总量阈值。"),
    isv_SMS_CONTENT_ILLEGAL("isv.SMS_CONTENT_ILLEGAL", "短信内容包含禁止发送内容", "原因：短信内容包含禁止发送内容。解决方案：修改短信文案。"),
    isv_SMS_SIGN_ILLEGAL("isv.SMS_SIGN_ILLEGAL", "签名禁止使用", "原因：签名禁止使用。解决方案：请在短信服务控制台中申请符合规定的签名。"),
    isp_RAM_PERMISSION_DENY("isp.RAM_PERMISSION_DENY", "RAM权限不足", "原因：RAM权限不足。解决方案：请为当前使用的AK对应RAM用户进行授权：AliyunDysmsFullAccess（管理权限）。具体操作请参见：创建RAM用户。"),
    isv_OUT_OF_SERVICE("isv.OUT_OF_SERVICE", "业务停机", "原因：余额不足。余额不足时，套餐包中即使有短信额度也无法发送短信。解决方案：请及时充值。如果余额大于零仍报此错误，请通过工单联系工程师处理。"),
    isv_PRODUCT_UN_SUBSCRIPT("isv.PRODUCT_UN_SUBSCRIPT", "未开通云通信产品的阿里云客户", "原因：该AK所属的账号尚未开通云通信的服务，包括短信、语音、流量等服务。解决方案：当出现此类报错信息时，需要检查当前AK是否已经开通阿里云短信服务，如已开通短信服务，则参照短信服务文档调用接口。"),
    isv_PRODUCT_UNSUBSCRIBE("isv.PRODUCT_UNSUBSCRIBE", "产品未开通", "原因：该AK所属的账号尚未开通当前接口的产品，例如仅开通了短信服务的用户调用语音接口时会产生此报错信息。解决方案：检查AK对应账号是否已开通调用对应接口的服务。开通短信服务，请单击短信服务产品介绍。"),
    isv_ACCOUNT_NOT_EXISTS("isv.ACCOUNT_NOT_EXISTS", "账户不存在", "原因：使用了错误的账户名称或AK。解决方案：请确认账号信息。"),
    isv_ACCOUNT_ABNORMAL("isv.ACCOUNT_ABNORMAL", "账户异常", "原因：账户异常。解决方案：请确认账号信息。"),
    isv_SMS_TEMPLATE_ILLEGAL("isv.SMS_TEMPLATE_ILLEGAL", "短信模版不合法", "原因：短信模板不存在或未审核通过。解决方案：参数TemplateCode请传入通过审核的模版ID，模版ID请在控制台中模板管理页面查看。"),
    isv_SMS_SIGNATURE_ILLEGAL("isv.SMS_SIGNATURE_ILLEGAL", "短信签名不合法", "原因：签名不存在或未审核通过。解决方案：参数SignName请传入审核通过的签名名称，签名名称请在控制台中签名管理页面查看。"),
    isv_INVALID_PARAMETERS("isv.INVALID_PARAMETERS", "参数异常", "原因：参数格式不正确。解决方案：请根据对应的API文档检查参数格式。例如，短信查询接口QuerySendDetails的参数SendDate日期格式为yyyyMMdd，正确格式为20170101，错误格式为2017-01-01。"),
    isp_SYSTEM_ERROR("isp.SYSTEM_ERROR", "系统错误", "原因：系统错误。解决方案：请重新调用接口，如仍存在此情况请创建工单反馈工程师查看。"),
    isv_MOBILE_NUMBER_ILLEGAL("isv.MOBILE_NUMBER_ILLEGAL", "非法手机号", "原因：手机号码格式错误。解决方案：参数PhoneNumbers请传入正确的格式。国内消息：+/+86/0086/86或无任何前缀的11位手机号码，例如1595195****。国际/港澳台消息：国际区号+号码，例如8520000****。"),
    isv_MOBILE_COUNT_OVER_LIMIT("isv.MOBILE_COUNT_OVER_LIMIT", "手机号码数量超过限制", "原因：参数PhoneNumbers中指定的手机号码数量超出限制。解决方案：请将手机号码数量限制在1000个以内。"),
    isv_TEMPLATE_MISSING_PARAMETERS("isv.TEMPLATE_MISSING_PARAMETERS", "模版缺少变量", "原因：参数TemplateParam中，变量未全部赋值。解决方案：请用JSON格式字符串为模板变量赋值。例如，模版为您好${name}，验证码${code}，则参数TemplateParam可以指定为{\"name\":\"Tom\",\"code\":\"123\"}。"),
    isv_BUSINESS_LIMIT_CONTROL("isv.BUSINESS_LIMIT_CONTROL", "业务限流", "原因：短信发送频率超限。解决方案：请将短信发送频率限制在正常的业务流控范围内。默认流控：使用同一个签名，对同一个手机号码发送短信验证码，支持1条/分钟，5条/小时，累计10条/天。"),
    isv_INVALID_JSON_PARAM("isv.INVALID_JSON_PARAM", "JSON参数不合法，只接受字符串值", "原因：参数格式错误，不是合法的JSON格式。解决方案：请在参数TemplateParam中指定正确的JSON格式字符串，例如{\"code\":\"123\"}。"),
    isv_BLACK_KEY_CONTROL_LIMIT("isv.BLACK_KEY_CONTROL_LIMIT", "黑名单管控", "原因：黑名单管控是指变量内容含有限制发送的内容，例如变量中不允许透传URL。解决方案：请检查通过变量是否透传了一些敏感信息。"),
    isv_PARAM_LENGTH_LIMIT("isv.PARAM_LENGTH_LIMIT", "参数超出长度限制", "原因：参数超出长度限制。解决方案：针对2018年01月10日之后申请的模板，变量限制为1~25个字符。请修改参数长度。"),
    isv_PARAM_NOT_SUPPORT_URL("isv.PARAM_NOT_SUPPORT_URL", "不支持URL", "原因：黑名单管控是指变量内容含有限制发送的内容，例如变量中不允许透传URL。解决方案：请检查通过变量是否透传了URL或敏感信息。"),
    isv_AMOUNT_NOT_ENOUGH("isv.AMOUNT_NOT_ENOUGH", "账户余额不足", "原因：当前账户余额不足。解决方案：请及时充值。调用接口前请确认当前账户余额是否足以支付预计发送的短信量。"),
    isv_TEMPLATE_PARAMS_ILLEGAL("isv.TEMPLATE_PARAMS_ILLEGAL", "模版变量里包含非法关键字", "原因：变量内容含有限制发送的内容，例如变量中不允许透传URL。解决方案：请检查通过变量是否透传了URL或敏感信息。"),
    SignatureDoesNotMatch("SignatureDoesNotMatch", "指定的签名与我们的计算机不匹配", "原因：签名（Signature）加密错误。解决方案：如果使用SDK调用接口，请注意accessKeyId和accessKeySecret字符串赋值正确。如果自行加密签名（Signature），请参见请求签名检查加密逻辑。"),
    InvalidTimeStamp_Expired("InvalidTimeStamp.Expired", "时间戳或日期已过期", "原因：一般由于时区差异造成时间戳错误，发出请求的时间和服务器接收到请求的时间不在15分钟内。阿里云网关使用的时间是GMT时间。解决方案：请使用GMT时间。"),
    SignatureNonceUsed("SignatureNonceUsed", "签名随机数已被使用", "原因：唯一随机数重复，SignatureNonce为唯一随机数，用于防止网络重放攻击。解决方案：不同请求请使用不同的随机数值。"),
    InvalidVersion("InvalidVersion", "版本无效", "原因：版本号（Version）错误。解决方案：请确认接口的版本号，短信服务的API版本号（Version）为2017-05-25。"),
    InvalidAction_NotFound("InvalidAction.NotFound", "未找到指定的API，请检查您的URL和方法", "原因：参数Action中指定的接口名错误。解决方案：请在参数Action中使用正确的接口地址和接口名。"),
    isv_SIGN_COUNT_OVER_LIMIT("isv.SIGN_COUNT_OVER_LIMIT", "一个自然日中申请签名数量超过限制", "原因：一个自然日中申请签名数量超过限制。解决方案：合理安排每天的签名申请数量，次日重新申请。"),
    isv_TEMPLATE_COUNT_OVER_LIMIT("isv.TEMPLATE_COUNT_OVER_LIMIT", "一个自然日中申请模板数量超过限制", "原因：一个自然日中申请模板数量超过限制。解决方案：合理安排每天的模板申请数量，次日重新申请。"),
    isv_SIGN_NAME_ILLEGAL("isv.SIGN_NAME_ILLEGAL", "签名名称不符合规范", "原因：签名名称不符合规范。解决方案：详情请参见个人用户签名规范或企业用户签名规范重新申请签名。"),
    isv_SIGN_FILE_LIMIT("isv.SIGN_FILE_LIMIT", "签名认证材料附件大小超过限制", "原因：签名认证材料附件大小超过限制。解决方案：压缩签名认证材料至2MB以下。"),
    isv_SIGN_OVER_LIMIT("isv.SIGN_OVER_LIMIT", "签名字符数量超过限制", "原因：签名的名称或申请说明的字数超过限制。解决方案：修改签名名称或申请说明，并重新提交审核。"),
    isv_TEMPLATE_OVER_LIMIT("isv.TEMPLATE_OVER_LIMIT", "模板字符数量超过限制", "原因：模板的名称、内容或申请说明的字数超过限制。解决方案：修改模板的名称、内容或申请说明，并重新提交审核。"),
    SIGNATURE_BLACKLIST("SIGNATURE_BLACKLIST", "签名黑名单", "原因：签名内容涉及违规信息，详情请参见个人用户签名规范或企业用户签名规范重新申请签名。解决方案：修改签名内容。"),
    isv_SHORTURL_OVER_LIMIT("isv.SHORTURL_OVER_LIMIT", "一天创建短链数量超过限制", "原因：一天创建短链数量超过限制。解决方案：合理预估一天申请短链数量，第二天重新创建短链。"),
    isv_NO_AVAILABLE_SHORT_URL("isv.NO_AVAILABLE_SHORT_URL", "无有效短链", "原因：客户当前并无有效短链。解决方案：客户需重新申请有效短链，保证在短链有效期内调用短参生成接口。"),
    isv_SHORTURL_NAME_ILLEGAL("isv.SHORTURL_NAME_ILLEGAL", "短链名称非法", "原因：短链名称非法。解决方案：根据短链规范重新创建。"),
    isv_SOURCEURL_OVER_LIMIT("isv.SOURCEURL_OVER_LIMIT", "原始链接字符数量超过限制", "原因：原始链接字符数量超过限制。解决方案：重新创建判断字符长度是否符合平台规则。"),
    isv_SHORTURL_TIME_ILLEGAL("isv.SHORTURL_TIME_ILLEGAL", "短链有效期期限超过限制", "原因：选择短链有效期限超过30天限制。解决方案：请保证短链有效期输入在30天以内。"),
    isv_PHONENUMBERS_OVER_LIMIT("isv.PHONENUMBERS_OVER_LIMIT", "上传手机号个数超过上限", "原因：单次调用上传手机号个数超过50000个上限。解决方案：分多次调用短参生成接口，单次上传手机号个数不超过50000个。"),
    isv_SHORTURL_STILL_AVAILABLE("isv.SHORTURL_STILL_AVAILABLE", "原始链接生成的短链仍在有效期内", "原因：原始链接生成的短链仍在有效期内，无需重复创建。解决方案：待原始链接生成的短链失效后，再重新创建。或删除该短链后，重新创建。或使用新的原始链接，创建新的短链。"),
    isv_SHORTURL_NOT_FOUND("isv.SHORTURL_NOT_FOUND", "没有可删除的短链", "原因：未发现可以删除的短链，可能是该短链已被删除，或该短链并未成功创建。解决方案：无需重复删除已删除的短链。"),
    isv_ERROR_SIGN_NOT_MODIFY("isv.ERROR_SIGN_NOT_MODIFY", "签名不支持修改", "原因：修改了通过审核的签名。解决方案：检查签名审核状态，ModifySmsSign接口只支持修改未审核通过的短信签名证明文件，您可以调用接口QuerySmsSign查询短信签名审核状态。修改未审批通过的签名。"),

    ;

    public String code;
    public String message;
    public String error;

    ResultCode(String code, String message, String error) {
        this.code = code;
        this.message = message;
        this.error = error;
    }

    public static Optional<ResultCode> valueOfCode(String code) {
        return Arrays.stream(ResultCode.values()).filter(o -> o.code.equals(code)).findFirst();
    }

}
