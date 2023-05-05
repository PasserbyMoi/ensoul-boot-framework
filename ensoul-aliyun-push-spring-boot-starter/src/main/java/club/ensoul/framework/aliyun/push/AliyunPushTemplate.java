package club.ensoul.framework.aliyun.push;

import club.ensoul.framework.aliyun.push.consts.PushIOSApnsEnv;
import club.ensoul.framework.aliyun.push.domain.PushBuilder;
import club.ensoul.framework.aliyun.push.domain.PushResult;
import club.ensoul.framework.aliyun.push.domain.PushTaskBuilder;
import club.ensoul.framework.aliyun.push.exception.AliyunPushException;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.push20160801.Client;
import com.aliyun.push20160801.models.*;
import club.ensoul.framework.aliyun.push.consts.PushDeviceType;
import club.ensoul.framework.aliyun.push.consts.PushTarget;
import club.ensoul.framework.aliyun.push.consts.PushType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
public class AliyunPushTemplate {

    @Getter
    private static Client client;

    public AliyunPushTemplate(Client client) {
        AliyunPushTemplate.client = client;
    }

    /**
     * 批量推送接口。<br/>
     * 在某种业务场景中，可能需要在极短时间内大量向不同设备单推不同消息，在设备数、消息数极大时，会产生较高QPS，达到我们单来源IP的QPS限制，造成部分推送失败。<br/>
     * 本接口针对此问题设计。它支持单次调用最大传入100个独立的推送任务，通过聚合请求的方式有效降低QPS，提高大量单推的稳定性和成功率。批量推送单账号限制每分钟100000调用。<br/>
     * 每个独立推送任务仅支持根据设备/账号/别名三类推送目标，且暂不支持短信融合配置。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。可取值：{@link PushTarget}
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     *                     <blockquote>  TAG：根据标签推送</blockquote>
     *                     <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                     <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                     <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult massPush(Long appKey, String title, String content, PushDeviceType pushDeviceType, PushType pushType, PushTarget pushTarget, Iterable<String> targetValues) {
        PushTaskBuilder pushTaskBuilder = PushTaskBuilder.created(title, content, pushDeviceType, pushType, pushTarget, targetValues);
        ArrayList<PushTaskBuilder> taskBuilders = new ArrayList<>();
        taskBuilders.add(pushTaskBuilder);
        return massPush(appKey, taskBuilders);
    }

    /**
     * 批量推送接口。<br/>
     * 在某种业务场景中，可能需要在极短时间内大量向不同设备单推不同消息，在设备数、消息数极大时，会产生较高QPS，达到我们单来源IP的QPS限制，造成部分推送失败。<br/>
     * 本接口针对此问题设计。它支持单次调用最大传入100个独立的推送任务，通过聚合请求的方式有效降低QPS，提高大量单推的稳定性和成功率。批量推送单账号限制每分钟100000调用。<br/>
     * 每个独立推送任务仅支持根据设备/账号/别名三类推送目标，且暂不支持短信融合配置。<br/>
     *
     * @param appKey       AppKey信息
     * @param taskBuilders {@link PushTaskBuilder}
     * @return {@link PushResult}<p/>
     */
    public PushResult massPush(Long appKey, Iterable<PushTaskBuilder> taskBuilders) {
        MassPushRequest request = new MassPushRequest();
        request.setAppKey(appKey);
        ArrayList<MassPushRequest.MassPushRequestPushTask> pushTasks = new ArrayList<>();
        MassPushRequest.MassPushRequestPushTask pushTask = new MassPushRequest.MassPushRequestPushTask();
        for (PushTaskBuilder taskBuilder : taskBuilders) {
            pushTasks.add(taskBuilder.getPushTask());
        }
        request.setPushTask(pushTasks);
        try {
            MassPushResponse response = client.massPush(request);
            MassPushResponseBody body = response.getBody();
            return PushResult.builder().messageIds(body.messageIds.messageId).requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 推送高级接口。本接口区分 Android 和 iOS 平台，对于不同平台的推送调用时需要传入平台对应的AppKey。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。可取值：{@link PushTarget}
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     *                     <blockquote>  TAG：根据标签推送</blockquote>
     *                     <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                     <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                     <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult push(Long appKey, String title, String content, PushDeviceType pushDeviceType, PushType pushType, PushTarget pushTarget, Iterable<String> targetValues) {
        PushBuilder pushBuilder = PushBuilder.created(appKey, title, content, pushDeviceType, pushType, pushTarget, targetValues);
        return push(appKey, pushBuilder);
    }

    /**
     * 推送高级接口。本接口区分 Android 和 iOS 平台，对于不同平台的推送调用时需要传入平台对应的AppKey<br/>
     *
     * @param appKey      AppKey信息
     * @param pushBuilder {@link PushBuilder}
     * @return {@link PushResult}<p/>
     */
    public PushResult push(Long appKey, PushBuilder pushBuilder) {
        PushRequest request = pushBuilder.getPushRequest();
        request.setAppKey(appKey);
        try {
            PushResponse response = client.push(request);
            PushResponseBody body = response.getBody();
            return PushResult.builder().messageId(body.messageId).requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 持续推送，本接口是用于解决 Push 接口中，无论是按设备推、按账号推、按别名推，在单次调用中都存在目标个数上限的问题。<br/>
     *
     * <blockquote> - 使用持续推送意味着业务场景是要往大量设备上，发送同一条消息。这时候，应当合理调用持续推送接口，每次调用，聚合一组设备进行推送(目前按设备推、按账号推、按别名推，单次推送目标个数上限是1000)。在同一个 MessageId 上推送的总次数为 10000 。</blockquote>
     * <blockquote> - 使用这个接口前，需要调用Push接口，将 Target 设置为 TBD（ToBeDetermined），填入消息内容，调用获得该消息在推送系统中的MessageId。 然后，可以基于这个 MessgeId，重复调用此接口，指定不同的目标组，将此消息推送到这些目标上。</blockquote>
     * <blockquote> - 调用Push接口设置目标为TBD获得MessageId后，这个消息默认会在推送系统保存24个小时，过期之前，可以随时使用本接口进行指定目标推送。过期后不再允许推送。超出总次数上限后不再允许推送。</blockquote>
     * <blockquote> - 每次调用本接口指定设备时，消息都是立即推出，不支持设置定时推送。</blockquote>
     *
     * @param appKey       AppKey信息
     * @param messageId    消息ID。调用Push接口，设置Target为TBD，调用后获得的MessageId。它代表了一个已经保存到推送系统的消息。
     * @param pushTarget   推送目标。可取值：{@link PushTarget}
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     *                     <blockquote>  TAG：根据标签推送</blockquote>
     *                     <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     *                     <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                     <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                     <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult continuouslyPush(Long appKey, String messageId, PushTarget pushTarget, Iterable<String> targetValues) {
        ContinuouslyPushRequest request = new ContinuouslyPushRequest();
        request.setAppKey(appKey);
        request.setMessageId(messageId);
        request.setTarget(pushTarget.name());
        request.setTargetValue(String.join(",", targetValues));
        try {
            ContinuouslyPushResponse response = client.continuouslyPush(request);
            ContinuouslyPushResponseBody body = response.getBody();
            return PushResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 调用 PushNoticeToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。不支持 TAG、ALL，可取值：
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     *                     <blockquote>  TAG：根据标签推送</blockquote>
     *                     <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToAndroid(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues) {
        String targetValue = String.join(",", targetValues);
        return pushMessageToAndroid(appKey, title, content, pushTarget, targetValue, null);
    }

    /**
     * 调用 PushMessageToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey      AppKey信息
     * @param title       发送的通知标题
     * @param content     发送的通知内容
     * @param pushTarget  推送目标。可取值：{@link PushTarget}
     *                    <blockquote>  DEVICE：根据设备推送</blockquote>
     *                    <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                    <blockquote>  ALIAS：根据别名推送</blockquote>
     *                    <blockquote>  TAG：根据标签推送</blockquote>
     *                    <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                    <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                    <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToAndroid(Long appKey, String title, String content, PushTarget pushTarget, String targetValue) {
        return pushMessageToAndroid(appKey, title, content, pushTarget, targetValue, null);
    }

    /**
     * 调用 PushNoticeToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。不支持 TAG、ALL，可取值：
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     *                     <blockquote>  TAG：根据标签推送</blockquote>
     *                     <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToAndroid(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues, String jobKey) {
        String targetValue = String.join(",", targetValues);
        return pushMessageToAndroid(appKey, title, content, pushTarget, targetValue, jobKey);
    }

    /**
     * 调用 PushMessageToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey      AppKey信息
     * @param title       发送的通知标题
     * @param content     发送的通知内容
     * @param pushTarget  推送目标。可取值：{@link PushTarget}
     *                    <blockquote>  DEVICE：根据设备推送</blockquote>
     *                    <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                    <blockquote>  ALIAS：根据别名推送</blockquote>
     *                    <blockquote>  TAG：根据标签推送</blockquote>
     *                    <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     *                    <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                    <blockquote>  Target=ALL，值为all</blockquote>
     * @param jobKey      推送任务自定义标识，当JobKey不为空时，回执日志中会附带该字段。查看回执日志参见 <a href="https://help.aliyun.com/document_detail/178178.html">回执日志</a>
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToAndroid(Long appKey, String title, String content, PushTarget pushTarget, String targetValue, String jobKey) {
        PushMessageToAndroidRequest request = new PushMessageToAndroidRequest();
        request.setAppKey(appKey);
        request.setJobKey(jobKey);
        request.setTarget(pushTarget.name());
        request.setTitle(title);
        request.setTargetValue(targetValue);
        request.setBody(content);
        try {
            PushMessageToAndroidResponse response = client.pushMessageToAndroid(request);
            PushMessageToAndroidResponseBody body = response.getBody();
            return PushResult.builder().messageId(body.messageId).requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }


    /**
     * 调用 PushNoticeToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。不支持 TAG、ALL，可取值：
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     *                     <blockquote>  TAG：根据标签推送</blockquote>
     *                     <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToAndroid(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues) {
        String targetValue = String.join(",", targetValues);
        return pushNoticeToAndroid(appKey, title, content, pushTarget, targetValue, null, null);
    }

    /**
     * 调用 PushNoticeToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey      AppKey信息
     * @param title       发送的通知标题
     * @param content     发送的通知内容
     * @param pushTarget  推送目标。可取值：{@link PushTarget}
     *                    <blockquote>  DEVICE：根据设备推送</blockquote>
     *                    <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                    <blockquote>  ALIAS：根据别名推送</blockquote>
     *                    <blockquote>  TAG：根据标签推送</blockquote>
     *                    <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                    <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                    <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToAndroid(Long appKey, String title, String content, PushTarget pushTarget, String targetValue) {
        return pushNoticeToAndroid(appKey, title, content, pushTarget, targetValue, null, null);
    }


    /**
     * 调用 PushNoticeToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey        AppKey信息
     * @param title         发送的通知标题
     * @param content       发送的通知内容
     * @param pushTarget    推送目标。不支持 TAG、ALL，可取值：
     *                      <blockquote>  DEVICE：根据设备推送</blockquote>
     *                      <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                      <blockquote>  ALIAS：根据别名推送</blockquote>
     *                      <blockquote>  TAG：根据标签推送</blockquote>
     *                      <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValues  根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @param jobKey        推送任务自定义标识，当JobKey不为空时，回执日志中会附带该字段。查看回执日志参见 <a href="https://help.aliyun.com/document_detail/178178.html">回执日志</a>
     * @param extParameters 自定义的KV结构，扩展用
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToAndroid(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues, String jobKey, Map<String, Object> extParameters) {
        String targetValue = String.join(",", targetValues);
        return pushNoticeToAndroid(appKey, title, content, pushTarget, targetValue, jobKey, extParameters);
    }


    /**
     * 调用 PushNoticeToAndroid 推送通知给 Android 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey        AppKey信息
     * @param title         发送的通知标题
     * @param content       发送的通知内容
     * @param pushTarget    推送目标。可取值：{@link PushTarget}
     *                      <blockquote>  DEVICE：根据设备推送</blockquote>
     *                      <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                      <blockquote>  ALIAS：根据别名推送</blockquote>
     *                      <blockquote>  TAG：根据标签推送</blockquote>
     *                      <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue   根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                      <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                      <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                      <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                      <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                      <blockquote>  Target=ALL，值为all</blockquote>
     * @param jobKey        推送任务自定义标识，当JobKey不为空时，回执日志中会附带该字段。查看回执日志参见 <a href="https://help.aliyun.com/document_detail/178178.html">回执日志</a>
     * @param extParameters 自定义的KV结构，扩展用
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToAndroid(Long appKey, String title, String content, PushTarget pushTarget, String targetValue, String jobKey, Map<String, Object> extParameters) {
        PushNoticeToAndroidRequest request = new PushNoticeToAndroidRequest();
        request.setAppKey(appKey);
        request.setJobKey(jobKey);
        request.setTarget(pushTarget.name());
        request.setTitle(title);
        request.setTargetValue(targetValue);
        request.setBody(content);
        request.setExtParameters(JSONUtil.toJsonStr(extParameters));
        try {
            PushNoticeToAndroidResponse response = client.pushNoticeToAndroid(request);
            PushNoticeToAndroidResponseBody body = response.getBody();
            return PushResult.builder().messageId(body.messageId).requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }


    /**
     * 调用 PushMessageToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。不支持 TAG、ALL，可取值：
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToiOS(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues) {
        String targetValue = String.join(",", targetValues);
        return pushMessageToiOS(appKey, title, content, pushTarget, targetValue, null);
    }

    /**
     * 调用 PushMessageToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey      AppKey信息
     * @param title       发送的通知标题
     * @param content     发送的通知内容
     * @param pushTarget  推送目标。可取值：{@link PushTarget}
     *                    <blockquote>  DEVICE：根据设备推送</blockquote>
     *                    <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                    <blockquote>  ALIAS：根据别名推送</blockquote>
     *                    <blockquote>  TAG：根据标签推送</blockquote>
     *                    <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                    <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                    <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToiOS(Long appKey, String title, String content, PushTarget pushTarget, String targetValue) {
        return pushMessageToiOS(appKey, title, content, pushTarget, targetValue, null);
    }

    /**
     * 调用 PushNoticeToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。不支持 TAG、ALL，可取值：
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToiOS(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues, String jobKey) {
        String targetValue = String.join(",", targetValues);
        return pushMessageToiOS(appKey, title, content, pushTarget, targetValue, jobKey);
    }

    /**
     * 调用 PushMessageToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey      AppKey信息
     * @param title       发送的通知标题
     * @param content     发送的通知内容
     * @param pushTarget  推送目标。可取值：{@link PushTarget}
     *                    <blockquote>  DEVICE：根据设备推送</blockquote>
     *                    <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                    <blockquote>  ALIAS：根据别名推送</blockquote>
     *                    <blockquote>  TAG：根据标签推送</blockquote>
     *                    <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                    <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                    <blockquote>  Target=ALL，值为all</blockquote>
     * @param jobKey      推送任务自定义标识，当JobKey不为空时，回执日志中会附带该字段。查看回执日志参见 <a href="https://help.aliyun.com/document_detail/178178.html">回执日志</a>
     * @return {@link PushResult}<p/>
     */
    public PushResult pushMessageToiOS(Long appKey, String title, String content, PushTarget pushTarget, String targetValue, String jobKey) {
        PushMessageToiOSRequest request = new PushMessageToiOSRequest();
        request.setAppKey(appKey);
        request.setJobKey(jobKey);
        request.setTarget(pushTarget.name());
        request.setTitle(title);
        request.setTargetValue(targetValue);
        request.setBody(content);
        try {
            PushMessageToiOSResponse response = client.pushMessageToiOS(request);
            PushMessageToiOSResponseBody body = response.getBody();
            return PushResult.builder().messageId(body.messageId).requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 调用 PushNoticeToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey       AppKey信息
     * @param title        发送的通知标题
     * @param content      发送的通知内容
     * @param pushTarget   推送目标。不支持 TAG、ALL，可取值：
     *                     <blockquote>  DEVICE：根据设备推送</blockquote>
     *                     <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                     <blockquote>  ALIAS：根据别名推送</blockquote>
     * @param targetValues 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToiOS(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues) {
        String targetValue = String.join(",", targetValues);
        return pushNoticeToiOS(appKey, title, content, pushTarget, targetValue, null, null);
    }

    /**
     * 调用 pushNoticeToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey      AppKey信息
     * @param title       发送的通知标题
     * @param content     发送的通知内容
     * @param pushTarget  推送目标。可取值：{@link PushTarget}
     *                    <blockquote>  DEVICE：根据设备推送</blockquote>
     *                    <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                    <blockquote>  ALIAS：根据别名推送</blockquote>
     *                    <blockquote>  TAG：根据标签推送</blockquote>
     *                    <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue 根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     *                    <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                    <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                    <blockquote>  Target=ALL，值为all</blockquote>
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToiOS(Long appKey, String title, String content, PushTarget pushTarget, String targetValue) {
        return pushNoticeToiOS(appKey, title, content, pushTarget, targetValue, null, null);
    }

    /**
     * 调用 PushNoticeToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey        AppKey信息
     * @param title         发送的通知标题
     * @param content       发送的通知内容
     * @param pushTarget    推送目标。不支持 TAG、ALL，可取值：
     *                      <blockquote>  DEVICE：根据设备推送</blockquote>
     *                      <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                      <blockquote>  ALIAS：根据别名推送</blockquote>
     * @param targetValues  根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送
     * @param jobKey        推送任务自定义标识，当JobKey不为空时，回执日志中会附带该字段。查看回执日志参见 <a href="https://help.aliyun.com/document_detail/178178.html">回执日志</a>
     * @param extParameters 自定义的KV结构，扩展用
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToiOS(Long appKey, String title, String content, PushTarget pushTarget, Iterable<String> targetValues, String jobKey, Map<String, Object> extParameters) {
        String targetValue = String.join(",", targetValues);
        return pushNoticeToiOS(appKey, title, content, pushTarget, targetValue, jobKey, extParameters);
    }

    /**
     * 调用 PushNoticeToiOS 推送通知给 iOS 设备。该接口默认只发送给在线设备，要发送离线保存消息请使用推送高级接口。<br/>
     *
     * @param appKey        AppKey信息
     * @param title         发送的通知标题
     * @param content       发送的通知内容
     * @param pushTarget    推送目标。可取值：
     *                      <blockquote>  DEVICE：根据设备推送</blockquote>
     *                      <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                      <blockquote>  ALIAS：根据别名推送</blockquote>
     *                      <blockquote>  TAG：根据标签推送</blockquote>
     *                      <blockquote>  ALL：推送给全部设备</blockquote>
     * @param targetValue   根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                      <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）</blockquote>
     *                      <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）</blockquote>
     *                      <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）</blockquote>
     *                      <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                      <blockquote>  Target=ALL，值为all</blockquote>
     * @param jobKey        推送任务自定义标识，当JobKey不为空时，回执日志中会附带该字段。查看回执日志参见 <a href="https://help.aliyun.com/document_detail/178178.html">回执日志</a>
     * @param extParameters 自定义的KV结构，扩展用
     * @return {@link PushResult}<p/>
     */
    public PushResult pushNoticeToiOS(Long appKey, String title, String content, PushTarget pushTarget, String targetValue, String jobKey, Map<String, Object> extParameters) {
        PushNoticeToiOSRequest request = new PushNoticeToiOSRequest();
        request.setAppKey(appKey);
        request.setJobKey(jobKey);
        request.setTarget(pushTarget.name());
        request.setTitle(title);
        request.setTargetValue(targetValue);
        request.setBody(content);
        request.setExtParameters(JSONUtil.toJsonStr(extParameters));
        if ("prod".equalsIgnoreCase(SpringUtil.getActiveProfile())) {
            request.setApnsEnv(PushIOSApnsEnv.PRODUCT.name());
        } else {
            request.setApnsEnv(PushIOSApnsEnv.DEV.name());
        }

        try {
            PushNoticeToiOSResponse response = client.pushNoticeToiOS(request);
            PushNoticeToiOSResponseBody body = response.getBody();
            return PushResult.builder().messageId(body.messageId).requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

    /**
     * 取消某次尚未执行的定时推送任务<br/>
     *
     * @param appKey    AppKey信息
     * @param messageId 发送的通知标题
     * @return {@link PushResult} messageId 为空<p/>
     */
    public PushResult cancelPush(Long appKey, Long messageId) {
        CancelPushRequest request = new CancelPushRequest();
        request.setAppKey(appKey);
        request.setMessageId(messageId);
        try {
            CancelPushResponse response = client.cancelPush(request);
            CancelPushResponseBody body = response.getBody();
            return PushResult.builder().requestId(body.requestId).build();
        } catch (Exception e) {
            throw new AliyunPushException(e);
        }
    }

}
