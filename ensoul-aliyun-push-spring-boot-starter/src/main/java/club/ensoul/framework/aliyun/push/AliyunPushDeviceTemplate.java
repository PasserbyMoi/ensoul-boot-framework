package club.ensoul.framework.aliyun.push;

import club.ensoul.framework.aliyun.push.domain.PushDeviceResult;
import club.ensoul.framework.aliyun.push.exception.AliyunPushException;
import com.aliyun.push20160801.Client;
import com.aliyun.push20160801.models.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AliyunPushDeviceTemplate {

    @Getter
    private static Client client;

    public AliyunPushDeviceTemplate(Client client) {
        AliyunPushDeviceTemplate.client = client;
    }

    /**
     * 取消某次尚未执行的定时推送任务<br/>
     *
     * @param appKey  AppKey信息
     * @param account 账户，一次仅支持查询一个
     * @return {@link PushDeviceResult} <p/>
     */
    public PushDeviceResult queryDevicesByAccount(Long appKey, String account) {
        QueryDevicesByAccountRequest request = new QueryDevicesByAccountRequest();
        request.setAppKey(appKey);
        request.setAccount(account);
        try {
            QueryDevicesByAccountResponse response = client.queryDevicesByAccount(request);
            QueryDevicesByAccountResponseBody body = response.getBody();
            return PushDeviceResult.builder().requestId(body.requestId).deviceIds(body.deviceIds.deviceId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 通过别名查询设备列表 <br/>
     *
     * @param appKey AppKey信息
     * @param alias  别名，一次仅支持查询一个
     * @return {@link PushDeviceResult} <p/>
     */
    public PushDeviceResult queryDevicesByAlias(Long appKey, String alias) {
        QueryDevicesByAliasRequest request = new QueryDevicesByAliasRequest();
        request.setAppKey(appKey);
        request.setAlias(alias);
        try {
            QueryDevicesByAliasResponse response = client.queryDevicesByAlias(request);
            QueryDevicesByAliasResponseBody body = response.getBody();
            return PushDeviceResult.builder().requestId(body.requestId).deviceIds(body.deviceIds.deviceId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

}
