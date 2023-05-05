package club.ensoul.framework.aliyun.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import club.ensoul.framework.aliyun.sms.exception.AliyunShortUrlException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliyunShortUrlTemplate {

    @Getter
    private static Client client;

    public AliyunShortUrlTemplate(Client client) {
        AliyunShortUrlTemplate.client = client;
    }

    /**
     * 创建短链 <br/>
     * 查询短链状态 {@link #queryShortUrl(String)} <br/>
     * 删除短链 {@link #deleteShortUrl(String)} <br/>
     *
     * @param shortUrlName  短链服务名称。不超过12个字符。
     * @param sourceUrl     原始链接地址。不超过1000个字符。
     * @param effectiveDays 短链服务使用有效期。单位为天，有效期最长为30天。
     * @return {@link AddShortUrlResponseBody}<p/>
     */
    public AddShortUrlResponseBody addShortUrl(String shortUrlName, String sourceUrl, int effectiveDays) {
        AddShortUrlRequest shortUrlRequest = new AddShortUrlRequest();
        shortUrlRequest.setShortUrlName(shortUrlName);
        shortUrlRequest.setSourceUrl(sourceUrl);
        shortUrlRequest.setEffectiveDays(effectiveDays + "");
        try {
            AddShortUrlResponse shortUrlResponse = client.addShortUrl(shortUrlRequest);
            return shortUrlResponse.getBody();
        } catch (Exception e) {
            throw new AliyunShortUrlException(e);
        }
    }

    /**
     * 删除短链 <br/>
     * 查询短链状态 {@link #queryShortUrl(String)} <br/>
     * 创建短链 {@link #addShortUrl(String, String, int)} <br/>
     *
     * @param sourceUrl 原始链接地址。不超过1000个字符。
     * @return {@link DeleteShortUrlResponseBody}<p/>
     */
    public DeleteShortUrlResponseBody deleteShortUrl(String sourceUrl) {
        DeleteShortUrlRequest shortUrlRequest = new DeleteShortUrlRequest();
        shortUrlRequest.setSourceUrl(sourceUrl);
        try {
            DeleteShortUrlResponse shortUrlResponse = client.deleteShortUrl(shortUrlRequest);
            return shortUrlResponse.getBody();
        } catch (Exception e) {
            throw new AliyunShortUrlException(e);
        }
    }

    /**
     * 查询短链状态 <br/>
     * 创建短链 {@link #addShortUrl(String, String, int)} <br/>
     * 删除短链 {@link #deleteShortUrl(String)} <br/>
     *
     * @param shortUrl 短链接地址
     * @return {@link QueryShortUrlResponseBody}<p/>
     */
    public QueryShortUrlResponseBody queryShortUrl(String shortUrl) {
        QueryShortUrlRequest shortUrlRequest = new QueryShortUrlRequest();
        shortUrlRequest.setShortUrl(shortUrl);
        try {
            QueryShortUrlResponse shortUrlResponse = client.queryShortUrl(shortUrlRequest);
            return shortUrlResponse.getBody();
        } catch (Exception e) {
            throw new AliyunShortUrlException(e);
        }
    }

}
