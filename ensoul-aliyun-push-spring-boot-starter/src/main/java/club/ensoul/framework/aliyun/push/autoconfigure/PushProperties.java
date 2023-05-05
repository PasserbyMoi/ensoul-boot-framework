package club.ensoul.framework.aliyun.push.autoconfigure;

import club.ensoul.framework.aliyun.push.consts.Format;
import club.ensoul.framework.aliyun.push.consts.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "aliyun.sms")
public class PushProperties {

    /**
     * 是否开启自动配置
     */
    private boolean enabled = false;

    /**
     * 用于标识用户
     **/
    private String accessKeyId;

    /**
     * 用于验证用户的密钥。AccessKey Secret必须保密。
     **/
    private String accessKeySecret;

    /**
     * 连接超时时间
     **/
    private Integer connectTimeout = 10000;

    /**
     * 读取数据超时时间
     **/
    private Integer readTimeout = 10000;

    /**
     * API支持的RegionID，如短信API的值为：cn-hangzhou。
     **/
    private String RegionId = "cn-hangzhou";

    /**
     * 支持HTTP或HTTPS协议请求通信。为了获得更高的安全性，推荐使用HTTPS协议发送请求， 默认 https
     **/
    private Protocol protocol = Protocol.https;

    /**
     * 返回参数的语言类型。返回类型：json或xml。默认值：json。
     **/
    private Format format = Format.json;

    /**
     * 访问的域名
     **/
    private String endpoint = "cloudpush.aliyuncs.com";

}
