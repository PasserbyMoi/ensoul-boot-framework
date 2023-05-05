package club.ensoul.framework.aliyun.push;

import club.ensoul.framework.aliyun.push.consts.PushTagKeyType;
import club.ensoul.framework.aliyun.push.domain.PushTagResult;
import club.ensoul.framework.aliyun.push.exception.AliyunPushException;
import com.aliyun.push20160801.Client;
import com.aliyun.push20160801.models.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AliyunPushTagTemplate {

    @Getter
    private static Client client;

    public AliyunPushTagTemplate(Client client) {
        AliyunPushTagTemplate.client = client;
    }

    /**
     * 查询App全部的TAG列表 <br/>
     *
     * @param appKey AppKey信息
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult queryTags(Long appKey) {
        ListTagsRequest request = new ListTagsRequest();
        request.setAppKey(appKey);
        try {
            ListTagsResponse response = client.listTags(request);
            ListTagsResponseBody body = response.getBody();
            List<String> tagNames = new ArrayList<>();
            if(body.tagInfos != null && body.tagInfos.tagInfo !=null) {
                tagNames = body.tagInfos.tagInfo.stream()
                        .map(ListTagsResponseBody.ListTagsResponseBodyTagInfosTagInfo::getTagName)
                        .collect(Collectors.toList());
            }
            return PushTagResult.builder().requestId(body.requestId).tagNames(tagNames).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 根据条件查询TAG列表 <br/>
     *
     * @param appKey    AppKey信息
     * @param keyType   ClientKey 的类型。可取值：<br />
     *                  <blockquote> DEVICE：是设备</blockquote>
     *                  <blockquote> ACCOUNT：是账号</blockquote>
     *                  <blockquote> ALIAS：是别名</blockquote>
     * @param clientKey 设备或account或alias， 每次只能查询1个clientKey
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult queryTags(Long appKey, PushTagKeyType keyType, String clientKey) {
        QueryTagsRequest request = new QueryTagsRequest();
        request.setAppKey(appKey);
        request.setClientKey(clientKey);
        request.setKeyType(keyType.name());
        try {
            QueryTagsResponse response = client.queryTags(request);
            QueryTagsResponseBody body = response.getBody();
            List<String> tagNames = new ArrayList<>();
            if(body.tagInfos != null && body.tagInfos.tagInfo !=null) {
                tagNames = body.tagInfos.tagInfo.stream()
                        .map(QueryTagsResponseBody.QueryTagsResponseBodyTagInfosTagInfo::getTagName)
                        .collect(Collectors.toList());
            }
            return PushTagResult.builder().requestId(body.requestId).tagNames(tagNames).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 绑定TAG。TAG绑定在10分钟内生效 <br/>
     *
     * @param appKey     AppKey信息
     * @param keyType    ClientKey的类型。可取值：<br />
     *                   <blockquote> DEVICE：是设备</blockquote>
     *                   <blockquote> ACCOUNT：是账号</blockquote>
     *                   <blockquote> ALIAS：是别名</blockquote>
     * @param clientKeys 设备或account或alias，多个key用逗号分隔，最多支持1000个
     * @param tagNames   绑定的Tag，多个Tag用逗号分隔，系统总共支持1万个Tag，此接口一次最多能解绑10个Tag。长度限制：不大于128字符
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult bindTags(Long appKey, PushTagKeyType keyType, Iterable<String> clientKeys, Iterable<String> tagNames) {
        String clientKey = String.join(",", clientKeys);
        String tagName = String.join(",", tagNames);
        return bindTags(appKey, keyType, clientKey, tagName);
    }

    /**
     * 绑定TAG。TAG绑定在10分钟内生效 <br/>
     *
     * @param appKey    AppKey信息
     * @param keyType   ClientKey的类型。可取值：<br />
     *                  <blockquote> DEVICE：是设备</blockquote>
     *                  <blockquote> ACCOUNT：是账号</blockquote>
     *                  <blockquote> ALIAS：是别名</blockquote>
     * @param clientKey 设备或account或alias，多个key用逗号分隔，最多支持1000个
     * @param tagName   绑定的Tag，多个Tag用逗号分隔，系统总共支持1万个Tag，此接口一次最多能解绑10个Tag。长度限制：不大于128字符
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult bindTags(Long appKey, PushTagKeyType keyType, String clientKey, String tagName) {
        BindTagRequest request = new BindTagRequest();
        request.setAppKey(appKey);
        request.setClientKey(clientKey);
        request.setKeyType(keyType.name());
        request.setTagName(tagName);
        try {
            BindTagResponse response = client.bindTag(request);
            BindTagResponseBody body = response.getBody();
            return PushTagResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 解绑TAG <br/>
     *
     * @param appKey     AppKey信息
     * @param keyType    ClientKey的类型。可取值：<br />
     *                   <blockquote> DEVICE：是设备</blockquote>
     *                   <blockquote> ACCOUNT：是账号</blockquote>
     *                   <blockquote> ALIAS：是别名</blockquote>
     * @param clientKeys 设备或account或alias，多个key用逗号分隔，最多支持1000个。
     * @param tagNames   绑定的Tag，多个Tag用逗号分隔，系统总共支持1万个Tag，此接口一次最多能解绑10个Tag。长度限制：不大于128字符
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult unbindTags(Long appKey, PushTagKeyType keyType, Iterable<String> clientKeys, Iterable<String> tagNames) {
        String clientKey = String.join(",", clientKeys);
        String tagName = String.join(",", tagNames);
        return unbindTags(appKey, keyType, clientKey, tagName);
    }

    /**
     * 解绑TAG <br/>
     *
     * @param appKey    AppKey信息
     * @param keyType   ClientKey的类型。可取值：<br />
     *                  <blockquote> DEVICE：是设备</blockquote>
     *                  <blockquote> ACCOUNT：是账号</blockquote>
     *                  <blockquote> ALIAS：是别名</blockquote>
     * @param clientKey 设备或account或alias，多个key用逗号分隔，最多支持1000个。
     * @param tagName   绑定的Tag，多个Tag用逗号分隔，系统总共支持1万个Tag，此接口一次最多能解绑10个Tag。长度限制：不大于128字符。
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult unbindTags(Long appKey, PushTagKeyType keyType, String clientKey, String tagName) {
        UnbindTagRequest request = new UnbindTagRequest();
        request.setAppKey(appKey);
        request.setClientKey(clientKey);
        request.setKeyType(keyType.name());
        request.setTagName(tagName);
        try {
            UnbindTagResponse response = client.unbindTag(request);
            UnbindTagResponseBody body = response.getBody();
            return PushTagResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 删除TAG <br/>
     *
     * @param appKey  AppKey信息
     * @param tagName 要删除的Tag名称。单次只能删除一个Tag。长度限制：不大于128字符。
     * @return {@link PushTagResult}<p/>
     */
    public PushTagResult removeTag(Long appKey, String tagName) {
        RemoveTagRequest request = new RemoveTagRequest();
        request.setAppKey(appKey);
        request.setTagName(tagName);
        try {
            RemoveTagResponse response = client.removeTag(request);
            RemoveTagResponseBody body = response.getBody();
            return PushTagResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

}
