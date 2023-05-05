package club.ensoul.framework.aliyun.push.domain;

import club.ensoul.framework.aliyun.push.consts.*;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.push20160801.models.MassPushRequest;

import java.util.Date;
import java.util.Map;

/**
 * @author wy_peng_chen6
 */
public class PushTaskBuilder {

    private final MassPushRequest.MassPushRequestPushTask pushTask;

    private String title;
    private String content;
    private Map<String, Object> extParameters;

    private IOS iOS;
    private Android android;
    private Xiaomi xiaomi;
    private Huawei huawei;
    private Vivo vivo;

    public IOS getiOS() {
        return iOS;
    }

    public Android getAndroid() {
        return android;
    }

    public Xiaomi getXiaomi() {
        return xiaomi;
    }

    public Huawei getHuawei() {
        return huawei;
    }

    public Vivo getVivo() {
        return vivo;
    }


    public PushTaskBuilder(String title, String content, PushDeviceType pushDeviceType, PushType pushType, PushTarget pushTarget, Iterable<String> targetValues) {
        pushTask = new MassPushRequest.MassPushRequestPushTask();
        pushTask.setTitle(title);
        pushTask.setBody(content);
        pushTask.setDeviceType(pushDeviceType.name());
        pushTask.setPushType(pushType.name());
        pushTask.setTarget(pushTarget.name());
        pushTask.setTargetValue(String.join(",", targetValues));
        this.title = title;
        this.content = content;
    }

    /**
     * @param title          Android推送时通知/消息的标题以及iOS消息的标题（必填）。iOS 10+通知显示标题，（iOS 8.2 <= iOS系统 < iOS 10）替换通知应用名称（选填）。长度限制：200字节。
     * @param content        Android推送时通知的内容/消息的内容；iOS消息/通知内容，推送的内容大小是有限制的，参见<a href="https://help.aliyun.com/document_detail/92832.html">产品限制</a>
     * @param pushDeviceType 设备类型，取值范围为：
     *                       <blockquote>  iOS：iOS设备</blockquote>
     *                       <blockquote>  ANDROID：Android设备</blockquote>
     *                       <blockquote>  ALL：全部类型设备</blockquote>
     * @param pushType       推送类型。取值：
     *                       <blockquote>  MESSAGE：表示消息</blockquote>
     *                       <blockquote>  NOTICE：表示通知</blockquote>
     * @param pushTarget     推送目标。可取值：
     *                       <blockquote>  DEVICE：根据设备推送</blockquote>
     *                       <blockquote>  ACCOUNT：根据账号推送</blockquote>
     *                       <blockquote>  ALIAS：根据别名推送</blockquote>
     * @param targetValues   根据Target来设定，多个值使用逗号分隔，超过限制需要分多次推送。
     *                       <blockquote>  Target=DEVICE，值如deviceid111,deviceid1111（最多支持1000个）。</blockquote>
     *                       <blockquote>  Target=ACCOUNT，值如account111,account222（最多支持1000个）。</blockquote>
     *                       <blockquote>  Target=ALIAS，值如alias111,alias222（最多支持1000个）。</blockquote>
     *                       <blockquote>  Target=TAG，支持单Tag和多Tag，格式请参见 <a href="https://help.aliyun.com/document_detail/48055.html">标签格式</a></blockquote>
     *                       <blockquote>  Target=ALL，值为all。</blockquote>
     */
    public static PushTaskBuilder created(String title, String content, PushDeviceType pushDeviceType, PushType pushType, PushTarget pushTarget, Iterable<String> targetValues) {
        return new PushTaskBuilder(title, content, pushDeviceType, pushType, pushTarget, targetValues);
    }

    /**
     * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
     *
     * @param jobKey 该参数要以 Json map 的格式传入，否则会解析出错。
     * @return this
     */
    public PushTaskBuilder jobKey(String jobKey) {
        pushTask.setJobKey(jobKey);
        return this;
    }

    /**
     * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
     *
     * @param extParameters 该参数要以 Json map 的格式传入，否则会解析出错。
     * @return this
     */
    public PushTaskBuilder extParameters(Map<String, Object> extParameters) {
        pushTask.setAndroidExtParameters(JSONUtil.toJsonStr(extParameters));
        pushTask.setIOSExtParameters(JSONUtil.toJsonStr(extParameters));
        return this;
    }

    /**
     * 用于定时发送。不设置缺省是立即发送。
     *
     * @param pushTime 推送时间，时间格式按照ISO8601标准表示，并需要使用 UTC 时间，格式为 YYYY-MM-DDThh:mm:ssZ。
     * @return this
     */
    public PushTaskBuilder pushTime(Date pushTime) {
        pushTask.setPushTime(DateUtil.format(pushTime, DatePattern.UTC_PATTERN));
        return this;
    }

    /**
     * 离线消息/通知的过期时间，和StoreOffline配合使用，过期则不会再被发送，最长保存72小时。默认为72小时。
     *
     * @param expireTime 时间格式按照ISO8601标准表示，并需要使用UTC时间，格式为YYYY-MM-DDThh:mm:ssZ，过期时间不能小于当前时间或者定时发送时间加上3秒（ExpireTime > PushTime+3秒），3秒是为了冗余网络和系统延迟造成的误差。建议单推不小于1分钟，全推、批量推送不少于10分钟。
     * @return this
     */
    public PushTaskBuilder setExpireTime(Date expireTime) {
        pushTask.setExpireTime(DateUtil.format(expireTime, DatePattern.UTC_PATTERN));
        pushTask.setStoreOffline(true);
        return this;
    }

    /**
     * 关闭离线消息，关闭后，离线消息/通知的过期时间将失效</p>
     * 说明：离线消息/通知是否保存。StoreOffline默认设置为false。若保存，在推送时候用户不在线，在过期时间（ExpireTime）内用户上线时会被再次发送。ExpireTime默认为72小时。iOS通知走APNs链路，不受StoreOffline影响。
     *
     * @return this
     */
    public PushTaskBuilder disableStoreOffline() {
        pushTask.setStoreOffline(true);
        return this;
    }

    /**
     * 该参数已废弃
     *
     * @deprecated
     */
    public PushTaskBuilder sendSpeed(Integer sendSpeed) {
        pushTask.setSendSpeed(sendSpeed);
        return this;
    }

    /**
     * iOS 通知声音。指定存放在app bundle或沙盒Library/Sounds目录下的音频文件名，请参见：iOS推送如何设定通知声音。 若指定为空串（””），通知为静音；若不设置，默认填充default为系统提示音。
     * 暂不支持 Android 设备
     */
    public PushTaskBuilder music(String music) {
        pushTask.setIOSMusic(music);
        pushTask.setAndroidMusic(music);
        return this;
    }


    public IOS iOS() {
        return this.iOS = new IOS(this);
    }

    public Android android() {
        return this.android = new Android(this);
    }

    public Xiaomi xiaomi() {
        return this.xiaomi = new Xiaomi(this);
    }

    public Huawei huawei() {
        return this.huawei = new Huawei(this);
    }

    public Vivo vivo() {
        return this.vivo = new Vivo(this);
    }

    public MassPushRequest.MassPushRequestPushTask getPushTask() {
        return pushTask;
    }

    /**
     * 小米手机推送设置，参考 <a href="https://dev.mi.com/console/doc/detail?pId=230">小米官方推送文档</a>
     */
    public static class Android {

        private final PushTaskBuilder taskBuilder;
        private final MassPushRequest.MassPushRequestPushTask pushTask;

        public Android(PushTaskBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushTask = taskBuilder.getPushTask();
        }

        public PushTaskBuilder end() {
            return taskBuilder;
        }

        /**
         * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
         *
         * @param extParameters 该参数要以 Json map 的格式传入，否则会解析出错。
         * @return this
         */
        public Android extParameters(Map<String, Object> extParameters) {
            pushTask.setAndroidExtParameters(JSONUtil.toJsonStr(extParameters));
            return this;
        }

        /**
         * 保留设置，当前版本暂不支持 Android 设备
         */
        public Android music(String music) {
            pushTask.setAndroidMusic(music);
            return this;
        }

        /**
         * 点击通知后动作
         *
         * @param androidOpenType 可取值： APPLICATION：打开应用（默认值）  ACTIVITY：打开应用  AndroidActivityURL：打开URL  NONE：无跳转
         * @return this
         */
        public Android openType(String androidOpenType) {
            pushTask.setAndroidOpenType(androidOpenType);
            return this;
        }

        /**
         * 点击通知后动作
         *
         * @param androidOpenType 可取值： APPLICATION：打开应用（默认值）  ACTIVITY：打开应用  AndroidActivityURL：打开URL  NONE：无跳转
         * @return this
         */
        public Android openType(AndroidOpenType androidOpenType) {
            pushTask.setAndroidOpenType(androidOpenType.name());
            return this;
        }

        /**
         * 设置NotificationChannel参数，具体用途请参见常见问题：Android 8.0以上设备通知接收不到。https://help.aliyun.com/knowledge_detail/67398.html
         *
         * @param notificationChannel 设置NotificationChannel参数
         * @return this
         */
        public Android notificationChannel(String notificationChannel) {
            pushTask.setAndroidNotificationChannel(notificationChannel);
            return this;
        }

        /**
         * Android自定义通知栏样式
         *
         * @param notificationBarType 取值：1-100
         * @return this
         */
        public Android notificationBarType(Integer notificationBarType) {
            pushTask.setAndroidNotificationBarType(1);
            return this;
        }

        /**
         * Android通知在通知栏展示时排列位置的优先级
         *
         * @param notificationBarPriority 可取值： -2，-1，0，1，2。
         * @return this
         */
        public Android notificationBarPriority(Integer notificationBarPriority) {
            pushTask.setAndroidNotificationBarPriority(notificationBarPriority);
            return this;
        }

        /**
         * 设定通知打开的activity, 仅当PushTask.N.AndroidOpenType=”Activity”时传入
         *
         * @param activity 如：com.alibaba.cloudpushdemo.bizactivity。
         * @return this
         */
        public Android activity(String activity) {
            pushTask.setAndroidActivity(activity);
            return this;
        }


        /**
         * 标识每条消息在通知显示时的唯一标识，不同的通知栏消息可以相同的NotifyId，实现新的通知栏消息覆盖老的，当前支持除vivo通道外的其他厂商通道。
         *
         * @param notificationNotifyId 每条消息在通知显示时的唯一标识
         * @return this
         */
        public Android notificationNotifyId(Integer notificationNotifyId) {
            pushTask.setAndroidNotificationNotifyId(notificationNotifyId);
            return this;
        }

        /**
         * 推送类型为消息时设备不在线，则这条推送会使用辅助弹窗功能。默认值为 false，仅当 PushType=MESSAGE 时生效。
         * 如果消息转通知推送成功，收到通知是展示的数据为服务端设置的 AndroidPopupTitle 和 AndroidPopupBody 参数值，点击通知在辅助弹窗的 onSysNoticeOpened 方法中获取到的数据是服务端设置的 Title 和 Body 参数值。。
         *
         * @param popupTitle    辅助弹窗模式下 title 内容
         * @param popupActivity 指定点击通知后跳转的 Activity
         * @param popupBody     辅助弹窗模式下 Body 内容。AndroidPopupActivity 参数不为空时，该参数必填。 长度限制：128字符。如使用厂商通道，则需同时符合厂商通道的限制，具体内容参见：Android 端辅助通道推送限制 https://help.aliyun.com/document_detail/165253.html
         * @return this
         */
        public Android enabledRemind(String popupTitle, String popupActivity, String popupBody) {
            pushTask.setAndroidRemind(true);
            pushTask.setAndroidPopupTitle(popupTitle);
            pushTask.setAndroidPopupActivity(popupActivity);
            pushTask.setAndroidPopupBody(popupBody);
            return this;
        }

        /**
         * 长文本模式
         *
         * @param imageUrl 大图标URL。当前支持：华为EMUI（仅长文本模式、Inbox模式下适用） 自有通道：安卓SDK3.5.0及以上
         * @param bigTitle 长文本模式下的 titley，发送时受具体厂商通道的限制。
         * @param bigBody  长文本模式下的 body，长度限制：1000字节（1个汉字算作3字节），发送时受具体厂商通道的限制。 若长文本模式下未提供此参数，则从Body、AndroidPopupBody里取第一个非空值。
         * @return this
         */
        public Android longText(String bigTitle, String bigBody, String imageUrl) {
            pushTask.setAndroidRenderStyle("1");
            pushTask.setAndroidImageUrl(imageUrl);
            pushTask.setAndroidBigTitle(bigTitle);
            pushTask.setAndroidBigBody(bigBody);
            pushTask.setSendChannels(SendChannel.accs.name());
            return this;
        }

        /**
         * 大图模式（仅支持小米），sendChannels 自动设置为 accs
         *
         * @param bigPictureUrl 大图模式下的图片URL，当前支持：自有通道：安卓SDK3.6.0及以上
         * @return this
         */
        public Android bigImage(String bigPictureUrl) {
            pushTask.setAndroidRenderStyle("2");
            pushTask.setSendChannels(SendChannel.accs.name());
            return this;
        }

        /**
         * 指定发送通道（测试中功能，仅可用于纯安卓应用）
         *
         * @param sendChannel 取值如下： accs：阿里自有通道 huawei：华为通道 xiaomi：小米通道 meizu：魅族通道 vivo：vivo通道 oppo：OPPO通道
         * @return this
         */
        public Android sendChannels(SendChannel sendChannel) {
            pushTask.setSendChannels(sendChannel.name());
            return this;
        }

        /**
         * Inbox模式下的正文，内容为合法的JSON Array，且元素不超过5个。当前支持：华为EMUI（仅长文本模式、Inbox模式下适用） 自有通道：安卓SDK3.5.0及以上
         *
         * @param inboxBody Inbox模式下的正文
         * @return this
         */
        public Android inboxBody(String inboxBody) {
            pushTask.setAndroidInboxBody(inboxBody);
            return this;
        }

    }

    /**
     * 小米手机推送设置，参考 <a href="https://dev.mi.com/console/doc/detail?pId=230">小米官方推送文档</a>
     */
    public static class IOS {

        private final PushTaskBuilder taskBuilder;
        private final MassPushRequest.MassPushRequestPushTask pushTask;

        public IOS(PushTaskBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushTask = taskBuilder.getPushTask();
            // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。
            pushTask.setIOSRemind(true);
            // iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。 DEV：表示开发环境。PRODUCT：表示生产环境。
            String activeProfile = SpringUtil.getActiveProfile();
            if ("prod".equalsIgnoreCase(activeProfile)) {
                pushTask.setIOSApnsEnv(PushIOSApnsEnv.PRODUCT.name());
                // iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效。
                pushTask.setIOSRemindBody(taskBuilder.content);
            } else {
                pushTask.setIOSApnsEnv(PushIOSApnsEnv.DEV.name());
            }
        }

        public PushTaskBuilder end() {
            return taskBuilder;
        }

        /**
         * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
         *
         * @param extParameters 该参数要以 Json map 的格式传入，否则会解析出错。
         * @return this
         */
        public IOS extParameters(Map<String, Object> extParameters) {
            pushTask.setIOSExtParameters(JSONUtil.toJsonStr(extParameters));
            return this;
        }

        /**
         * iOS 通知声音。指定存放在app bundle或沙盒Library/Sounds目录下的音频文件名，请参见：iOS推送如何设定通知声音。 若指定为空串（””），通知为静音；若不设置，默认填充default为系统提示音。
         */
        public IOS music(String music) {
            pushTask.setIOSMusic(music);
            return this;
        }

        /**
         * iOS应用图标右上角角标。若设置该项，BadgeAutoIncrement 将被设置为 不开启
         *
         * @param iOSBadge 角标数
         * @return this
         */
        public IOS setIOSBadge(Integer iOSBadge) {
            pushTask.setIOSBadge(iOSBadge);
            pushTask.setIOSBadgeAutoIncrement(false);
            return this;
        }

        /**
         * 开启角标自增功能，此时，iOSBadge 设置为空。 角标自增功能由推送服务端维护每个设备的角标计数，需要用户使用V1.9.5以上版本的sdk，并且需要用户主动同步角标数字到服务端。
         *
         * @return this
         */
        public IOS enabledBadgeAutoIncrement() {
            pushTask.setIOSBadge(null);
            pushTask.setIOSBadgeAutoIncrement(true);
            return this;
        }

        /**
         * 是否开启iOS静默通知
         */
        public IOS enabledSilentNotification() {
            pushTask.setIOSSilentNotification(true);
            return this;
        }

        /**
         * iOS通知副标题内容（iOS 10+）
         *
         * @param iOSSubtitle 副标题内容
         * @return this
         */
        public IOS subtitle(String iOSSubtitle) {
            pushTask.setIOSSubtitle(iOSSubtitle);
            return this;
        }

        /**
         * 设备收到有相同CollapseId的消息，会合并成一条。设备不在线，连续发相同CollapseId的消息，通知栏只会显示一条，iOS 10+支持设置此参数。
         *
         * @param iOSNotificationCollapseId CollapseId
         * @return this
         */
        public IOS notificationCollapseId(String iOSNotificationCollapseId) {
            pushTask.setIOSNotificationCollapseId(null);
            return this;
        }

        /**
         * 指定iOS通知Category（iOS 10+）。
         *
         * @param iOSNotificationCategory Category
         * @return this
         */
        public IOS notificationCategory(String iOSNotificationCategory) {
            pushTask.setIOSNotificationCategory(null);
            return this;
        }

        /**
         * 通过该属性对iOS的远程通知进行分组，标记折叠的组别识别名。仅支持iOS 12.0+版本
         *
         * @param iOSNotificationThreadId 分组id
         * @return this
         */
        public IOS notificationThreadId(String iOSNotificationThreadId) {
            pushTask.setIOSNotificationThreadId(iOSNotificationThreadId);
            return this;
        }

        /**
         * 是否使能iOS通知扩展处理（iOS 10+）
         *
         * @return this
         */
        public IOS enabledMutableContent() {
            pushTask.setIOSMutableContent(true);
            return this;
        }
    }

    /**
     * 小米手机推送设置，参考 <a href="https://dev.mi.com/console/doc/detail?pId=230">小米官方推送文档</a>
     */
    public static class Xiaomi {

        private final PushTaskBuilder taskBuilder;
        private final MassPushRequest.MassPushRequestPushTask pushTask;

        public Xiaomi(PushTaskBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushTask = taskBuilder.getPushTask();
        }

        public PushTaskBuilder end() {
            return taskBuilder;
        }

        /**
         * 设置小米通知类型的channelId，需要在小米平台申请
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Xiaomi notificationChannel(String notificationChannel) {
            pushTask.setAndroidNotificationXiaomiChannel(notificationChannel);
            return this;
        }

        /**
         * 大图模式下的大图URL。大图模式下图片上传至小米推送后会返回一个图片URL，本参数内填写这个URL。
         *
         * @param bigPictureUrl 小米通知类型的channelId
         * @return this
         */
        public Xiaomi bigPictureUrl(String bigPictureUrl) {
            pushTask.setAndroidXiaomiBigPictureUrl(bigPictureUrl);
            return this;
        }

        /**
         * 大图标URL。当前支持：小米MIUI12及以上（仅长文本模式下适用）。上传到小米服务器上，返回的图标URL。<p/>
         * 具体请参考：<a href=https://dev.mi.com/console/doc/detail?spm=a2c4g.11186623.0.0.30254833wtqEDa&pId=1278#_3_3>小米官方推送文档</a>
         *
         * @param imageUrl 小米通知类型的channelId
         * @return this
         */
        public Xiaomi imageUrl(String imageUrl) {
            pushTask.setAndroidXiaomiImageUrl(imageUrl);
            return this;
        }

        /**
         * 已废弃，所有第三方辅助弹窗都由新参数AndroidPopupActivity统一支持。
         *
         * @param notifyBody 小米通知类型的channelId
         * @return this
         * @deprecated
         */
        public Xiaomi notifyBody(String notifyBody) {
            pushTask.setAndroidXiaoMiNotifyBody(notifyBody);
            return this;
        }

        /**
         * 已废弃，所有第三方辅助弹窗都由新参数AndroidPopupActivity统一支持。
         *
         * @param miActivity 小米通知类型的channelId
         * @return this
         * @deprecated
         */
        public Xiaomi miActivity(String miActivity) {
            pushTask.setAndroidXiaoMiActivity(miActivity);
            return this;
        }

        /**
         * 已废弃，所有第三方辅助弹窗都由新参数AndroidPopupActivity统一支持。
         *
         * @param notifyTitle 小米通知类型的channelId
         * @return this
         * @deprecated
         */
        public Xiaomi notifyTitle(String notifyTitle) {
            pushTask.setAndroidXiaoMiNotifyTitle(notifyTitle);
            return this;
        }

    }

    /**
     * 华为手机推送设置，参考 <a href="https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/service-introduction-0000001050040060">华为官方推送文档</a>
     */
    public static class Huawei {

        private final PushTaskBuilder taskBuilder;
        private final MassPushRequest.MassPushRequestPushTask pushTask;

        public Huawei(PushTaskBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushTask = taskBuilder.getPushTask();
        }

        public PushTaskBuilder end() {
            return taskBuilder;
        }

        /**
         * 华为通道标识高优先级透传消息的特殊场景，取值如下：PLAY_VOICE：语音播报 VOIP：VoIP电话 需申请特殊权限，
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Huawei notificationChannel(String notificationChannel) {
            pushTask.setAndroidNotificationHuaweiChannel(notificationChannel);
            return this;
        }

        /**
         * 华为通道标识高优先级透传消息的特殊场景，取值如下：PLAY_VOICE：语音播报 VOIP：VoIP电话 需申请特殊权限，
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Huawei notificationChannel(HuaweiChannel notificationChannel) {
            pushTask.setAndroidNotificationHuaweiChannel(notificationChannel.name());
            return this;
        }

        /**
         * 华为通道透传消息投递优先级，取值如下： HIGH NORMAL
         *
         * @param messageUrgency 华为通道透传消息投递优先级
         * @return this
         */
        public Huawei messageUrgency(String messageUrgency) {
            pushTask.setAndroidMessageHuaweiUrgency(messageUrgency);
            return this;
        }

        /**
         * 华为通道透传消息投递优先级，取值如下： HIGH NORMAL
         *
         * @param messageUrgency 华为通道透传消息投递优先级
         * @return this
         */
        public Huawei messageUrgency(HuaweiUrgency messageUrgency) {
            pushTask.setAndroidMessageHuaweiUrgency(messageUrgency.name());
            return this;
        }

        /**
         * 设置Huawei通知消息分类importance参数，决定用户设备消息通知行为，取值如下：LOW：资讯营销类消息 NORMAL：服务与通讯类消息 需要在Huawei平台申请
         *
         * @param messageCategory Huawei通知消息分类importance参数
         * @return this
         */
        public Huawei messageCategory(String messageCategory) {
            pushTask.setAndroidMessageHuaweiCategory(messageCategory);
            return this;
        }

        /**
         * 设置Huawei通知消息分类importance参数，决定用户设备消息通知行为，取值如下：LOW：资讯营销类消息 NORMAL：服务与通讯类消息 需要在Huawei平台申请
         *
         * @param messageCategory Huawei通知消息分类importance参数
         * @return this
         */
        public Huawei messageCategory(HuaweiCategory messageCategory) {
            pushTask.setAndroidMessageHuaweiCategory(messageCategory.name());
            return this;
        }

        public enum HuaweiChannel {
            PLAY_VOICE, VOIP
        }

        public enum HuaweiUrgency {
            HIGH, NORMAL
        }

        public enum HuaweiCategory {
            LOW, NORMAL
        }

    }

    /**
     * vivo 手机推送设置，参考 <a href="https://dev.vivo.com.cn/documentCenter/doc/541">vivo 官方推送文档</a>
     */
    public static class Vivo {

        private final PushTaskBuilder taskBuilder;
        private final MassPushRequest.MassPushRequestPushTask pushTask;

        public Vivo(PushTaskBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushTask = taskBuilder.getPushTask();
        }

        public PushTaskBuilder end() {
            return taskBuilder;
        }

        /**
         * 设置vivo通知消息分类，取值为： 0：运营类消息（默认） 1：系统类消息，参考 <a href="https://dev.vivo.com.cn/documentCenter/doc/359">vivo 官方推送文档</a>
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Vivo notificationChannel(String notificationChannel) {
            pushTask.setAndroidNotificationVivoChannel(notificationChannel);
            return this;
        }

        /**
         * 设置vivo通知消息分类，取值为： 0：运营类消息（默认） 1：系统类消息，参考 <a href="https://dev.vivo.com.cn/documentCenter/doc/359">vivo 官方推送文档</a>
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Vivo notificationChannel(VivoChannel notificationChannel) {
            pushTask.setAndroidNotificationVivoChannel(notificationChannel.ordinal() + "");
            return this;
        }

        public enum VivoChannel {
            OPERATING, SYSTEM
        }

    }

}
