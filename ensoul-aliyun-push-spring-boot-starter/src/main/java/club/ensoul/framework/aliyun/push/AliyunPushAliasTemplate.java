package club.ensoul.framework.aliyun.push;

import club.ensoul.framework.aliyun.push.domain.PushAliasResult;
import club.ensoul.framework.aliyun.push.exception.AliyunPushException;
import com.aliyun.push20160801.Client;
import com.aliyun.push20160801.models.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AliyunPushAliasTemplate {

    @Getter
    private static Client client;

    public AliyunPushAliasTemplate(Client client) {
        AliyunPushAliasTemplate.client = client;
    }

    /**
     * 查询设备绑定的别名 <br/>
     *
     * @param appKey   AppKey信息
     * @param deviceId 设备在推送的唯一标识，32位，数字和小写字母组合
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult queryAliass(Long appKey, String deviceId) {
        QueryAliasesRequest request = new QueryAliasesRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceId);
        try {
            QueryAliasesResponse response = client.queryAliases(request);
            QueryAliasesResponseBody body = response.getBody();
            List<String> aliasNames = new ArrayList<>();
            if (body.aliasInfos != null && body.aliasInfos.aliasInfo != null) {
                aliasNames = body.aliasInfos.aliasInfo.stream()
                        .map(QueryAliasesResponseBody.QueryAliasesResponseBodyAliasInfosAliasInfo::getAliasName)
                        .collect(Collectors.toList());
            }
            return PushAliasResult.builder().requestId(body.requestId).aliasNames(aliasNames).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 绑定别名。一次最多只能绑定10个别名，绑定别名之后立即生效 <br/>
     *
     * @param appKey     AppKey信息
     * @param deviceId   设备在推送的唯一标识，32位，数字和小写字母组合
     * @param aliasNames 需要绑定的别名。一次最多只能绑定10个，Alias最长128个字节（中文算三个字符），一个设备最多绑定128个别名，一个别名最多允许绑定128个设备
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult bindAliass(Long appKey, String deviceId, Iterable<String> aliasNames) {
        String aliasName = String.join(",", aliasNames);
        return bindAliass(appKey, deviceId, aliasName);
    }

    /**
     * 绑定别名。一次最多只能绑定10个别名，绑定别名之后立即生效 <br/>
     *
     * @param appKey    AppKey信息
     * @param deviceId  设备在推送的唯一标识，32位，数字和小写字母组合
     * @param aliasName 需要绑定的别名。一次最多只能绑定10个，多个Alias用逗号分隔，Alias最长128个字节（中文算三个字符），一个设备最多绑定128个别名，一个别名最多允许绑定128个设备
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult bindAliass(Long appKey, String deviceId, String aliasName) {
        BindAliasRequest request = new BindAliasRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceId);
        request.setAliasName(aliasName);
        try {
            BindAliasResponse response = client.bindAlias(request);
            BindAliasResponseBody body = response.getBody();
            return PushAliasResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 解绑别名。解绑别名之后立即生效 <br/>
     *
     * @param appKey     AppKey信息
     * @param deviceId   设备在推送的唯一标识，32位，数字和小写字母组合。
     * @param aliasNames 需要绑定的别名。一次最多只能绑定10个，Alias最长128个字节（中文算三个字符），一个设备最多绑定128个别名，一个别名最多允许绑定128个设备
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult unbindAliass(Long appKey, String deviceId, Iterable<String> aliasNames) {
        String aliasName = String.join(",", aliasNames);
        return unbindAliass(appKey, deviceId, aliasName, false);
    }

    /**
     * 解绑别名。解绑别名之后立即生效 <br/>
     *
     * @param appKey     AppKey信息
     * @param deviceId   设备在推送的唯一标识，32位，数字和小写字母组合
     * @param aliasNames 需要绑定的别名。一次最多只能绑定10个，Alias最长128个字节（中文算三个字符），一个设备最多绑定128个别名，一个别名最多允许绑定128个设备
     * @param unbindAll  是否全部解绑，默认为”false”。如果值为”true”，则解绑一个设备当前绑定的所有别名；如果值为”false”，则解绑”AliasName”指定的别名
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult unbindAliass(Long appKey, String deviceId, Iterable<String> aliasNames, boolean unbindAll) {
        String aliasName = String.join(",", aliasNames);
        return unbindAliass(appKey, deviceId, aliasName, unbindAll);
    }

    /**
     * 解绑别名。解绑别名之后立即生效 <br/>
     *
     * @param appKey    AppKey信息
     * @param deviceId  设备在推送的唯一标识，32位，数字和小写字母组合
     * @param aliasName 需要绑定的别名。一次最多只能绑定10个，多个Alias用逗号分隔，Alias最长128个字节（中文算三个字符），一个设备最多绑定128个别名，一个别名最多允许绑定128个设备
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult unbindAliass(Long appKey, String deviceId, String aliasName) {
        return unbindAliass(appKey, deviceId, aliasName, false);
    }

    /**
     * 解绑别名。解绑别名之后立即生效 <br/>
     *
     * @param appKey    AppKey信息
     * @param deviceId  设备在推送的唯一标识，32位，数字和小写字母组合。
     * @param aliasName 需要绑定的别名。一次最多只能绑定10个，多个Alias用逗号分隔，Alias最长128个字节（中文算三个字符），一个设备最多绑定128个别名，一个别名最多允许绑定128个设备
     * @param unbindAll 是否全部解绑，默认为”false”。如果值为”true”，则解绑一个设备当前绑定的所有别名；如果值为”false”，则解绑”AliasName”指定的别名。
     * @return {@link PushAliasResult}<p/>
     */
    public PushAliasResult unbindAliass(Long appKey, String deviceId, String aliasName, boolean unbindAll) {
        UnbindAliasRequest request = new UnbindAliasRequest();
        request.setAppKey(appKey);
        request.setDeviceId(deviceId);
        request.setAliasName(aliasName);
        request.setUnbindAll(unbindAll);
        try {
            UnbindAliasResponse response = client.unbindAlias(request);
            UnbindAliasResponseBody body = response.getBody();
            return PushAliasResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

}
