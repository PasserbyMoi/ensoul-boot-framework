package club.ensoul.framework.aliyun.push.domain;

import club.ensoul.framework.aliyun.push.consts.*;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.push20160801.models.PushRequest;

import java.util.Date;
import java.util.Map;

/**
 * @author wy_peng_chen6
 */
public class PushBuilder {

    private final PushRequest pushRequest;

    private String title;
    private String content;
    private Map<String, Object> extParameters;

    private IOS iOS;
    private Android android;
    private Xiaomi xiaomi;
    private Huawei huawei;
    private Vivo vivo;
    private Sms sms;

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

    public Sms getSms() {
        return sms;
    }

    public PushBuilder(Long appKey, String title, String content, PushDeviceType pushDeviceType, PushType pushType, PushTarget pushTarget, Iterable<String> targetValues) {
        pushRequest = new PushRequest();
        pushRequest.setAppKey(appKey);
        pushRequest.setTitle(title);
        pushRequest.setBody(content);
        pushRequest.setDeviceType(pushDeviceType.name());
        pushRequest.setPushType(pushType.name());
        pushRequest.setTarget(pushTarget.name());
        pushRequest.setTargetValue(String.join(",", targetValues));
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
    public static PushBuilder created(Long appKey, String title, String content, PushDeviceType pushDeviceType, PushType pushType, PushTarget pushTarget, Iterable<String> targetValues) {
        return new PushBuilder(appKey, title, content, pushDeviceType, pushType, pushTarget, targetValues);
    }

    /**
     * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
     *
     * @param jobKey 该参数要以 Json map 的格式传入，否则会解析出错。
     * @return this
     */
    public PushBuilder jobKey(String jobKey) {
        pushRequest.setJobKey(jobKey);
        return this;
    }

    /**
     * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
     *
     * @param extParameters 该参数要以 Json map 的格式传入，否则会解析出错。
     * @return this
     */
    public PushBuilder extParameters(Map<String, Object> extParameters) {
        pushRequest.setAndroidExtParameters(JSONUtil.toJsonStr(extParameters));
        pushRequest.setIOSExtParameters(JSONUtil.toJsonStr(extParameters));
        return this;
    }

    /**
     * 用于定时发送。不设置缺省是立即发送。
     *
     * @param pushTime 推送时间，时间格式按照ISO8601标准表示，并需要使用 UTC 时间，格式为 YYYY-MM-DDThh:mm:ssZ。
     * @return this
     */
    public PushBuilder pushTime(Date pushTime) {
        pushRequest.setPushTime(DateUtil.format(pushTime, DatePattern.UTC_PATTERN));
        return this;
    }

    /**
     * 离线消息/通知的过期时间，和StoreOffline配合使用，过期则不会再被发送，最长保存72小时。默认为72小时。
     *
     * @param expireTime 时间格式按照ISO8601标准表示，并需要使用UTC时间，格式为YYYY-MM-DDThh:mm:ssZ，过期时间不能小于当前时间或者定时发送时间加上3秒（ExpireTime > PushTime+3秒），3秒是为了冗余网络和系统延迟造成的误差。建议单推不小于1分钟，全推、批量推送不少于10分钟。
     * @return this
     */
    public PushBuilder setExpireTime(Date expireTime) {
        pushRequest.setExpireTime(DateUtil.format(expireTime, DatePattern.UTC_PATTERN));
        pushRequest.setStoreOffline(true);
        return this;
    }

    /**
     * 关闭离线消息，关闭后，离线消息/通知的过期时间将失效</p>
     * 说明：离线消息/通知是否保存。StoreOffline默认设置为false。若保存，在推送时候用户不在线，在过期时间（ExpireTime）内用户上线时会被再次发送。ExpireTime默认为72小时。iOS通知走APNs链路，不受StoreOffline影响。
     *
     * @return this
     */
    public PushBuilder disableStoreOffline() {
        pushRequest.setStoreOffline(true);
        return this;
    }

    /**
     * 该参数已废弃
     *
     * @deprecated
     */
    public PushBuilder sendSpeed(Integer sendSpeed) {
        pushRequest.setSendSpeed(sendSpeed);
        return this;
    }

    /**
     * iOS 通知声音。指定存放在app bundle或沙盒Library/Sounds目录下的音频文件名，请参见：iOS推送如何设定通知声音。 若指定为空串（””），通知为静音；若不设置，默认填充default为系统提示音。
     * 暂不支持 Android 设备
     */
    public PushBuilder music(String music) {
        pushRequest.setIOSMusic(music);
        pushRequest.setAndroidMusic(music);
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

    public PushRequest getPushRequest() {
        return pushRequest;
    }

    /**
     * 小米手机推送设置，参考 <a href="https://dev.mi.com/console/doc/detail?pId=230">小米官方推送文档</a>
     */
    public static class Android {

        private final PushBuilder taskBuilder;
        private final PushRequest pushRequest1;

        public Android(PushBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushRequest1 = taskBuilder.getPushRequest();
        }

        public PushBuilder end() {
            return taskBuilder;
        }

        /**
         * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
         *
         * @param extParameters 该参数要以 Json map 的格式传入，否则会解析出错。
         * @return this
         */
        public Android extParameters(Map<String, Object> extParameters) {
            pushRequest1.setAndroidExtParameters(JSONUtil.toJsonStr(extParameters));
            return this;
        }

        /**
         * 保留设置，当前版本暂不支持 Android 设备
         */
        public Android music(String music) {
            pushRequest1.setAndroidMusic(music);
            return this;
        }

        /**
         * 点击通知后动作
         *
         * @param androidOpenType 可取值： APPLICATION：打开应用（默认值）  ACTIVITY：打开应用  AndroidActivityURL：打开URL  NONE：无跳转
         * @return this
         */
        public Android openType(String androidOpenType) {
            pushRequest1.setAndroidOpenType(androidOpenType);
            return this;
        }

        /**
         * 点击通知后动作
         *
         * @param androidOpenType 可取值： APPLICATION：打开应用（默认值）  ACTIVITY：打开应用  AndroidActivityURL：打开URL  NONE：无跳转
         * @return this
         */
        public Android openType(AndroidOpenType androidOpenType) {
            pushRequest1.setAndroidOpenType(androidOpenType.name());
            return this;
        }

        /**
         * 设置NotificationChannel参数，具体用途请参见常见问题：Android 8.0以上设备通知接收不到。https://help.aliyun.com/knowledge_detail/67398.html
         *
         * @param notificationChannel 设置NotificationChannel参数
         * @return this
         */
        public Android notificationChannel(String notificationChannel) {
            pushRequest1.setAndroidNotificationChannel(notificationChannel);
            return this;
        }

        /**
         * Android自定义通知栏样式
         *
         * @param notificationBarType 取值：1-100
         * @return this
         */
        public Android notificationBarType(Integer notificationBarType) {
            pushRequest1.setAndroidNotificationBarType(1);
            return this;
        }

        /**
         * Android通知在通知栏展示时排列位置的优先级
         *
         * @param notificationBarPriority 可取值： -2，-1，0，1，2。
         * @return this
         */
        public Android notificationBarPriority(Integer notificationBarPriority) {
            pushRequest1.setAndroidNotificationBarPriority(notificationBarPriority);
            return this;
        }

        /**
         * 设定通知打开的activity, 仅当PushTask.N.AndroidOpenType=”Activity”时传入
         *
         * @param activity 如：com.alibaba.cloudpushdemo.bizactivity。
         * @return this
         */
        public Android activity(String activity) {
            pushRequest1.setAndroidActivity(activity);
            return this;
        }


        /**
         * 标识每条消息在通知显示时的唯一标识，不同的通知栏消息可以相同的NotifyId，实现新的通知栏消息覆盖老的，当前支持除vivo通道外的其他厂商通道。
         *
         * @param notificationNotifyId 每条消息在通知显示时的唯一标识
         * @return this
         */
        public Android notificationNotifyId(Integer notificationNotifyId) {
            pushRequest1.setAndroidNotificationNotifyId(notificationNotifyId);
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
            pushRequest1.setAndroidRemind(true);
            pushRequest1.setAndroidPopupTitle(popupTitle);
            pushRequest1.setAndroidPopupActivity(popupActivity);
            pushRequest1.setAndroidPopupBody(popupBody);
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
            pushRequest1.setAndroidRenderStyle(1);
            pushRequest1.setAndroidImageUrl(imageUrl);
            pushRequest1.setAndroidBigTitle(bigTitle);
            pushRequest1.setAndroidBigBody(bigBody);
            pushRequest1.setSendChannels(SendChannel.accs.name());
            return this;
        }

        /**
         * 大图模式（仅支持小米），sendChannels 自动设置为 accs
         *
         * @param bigPictureUrl 大图模式下的图片URL，当前支持：自有通道：安卓SDK3.6.0及以上
         * @return this
         */
        public Android bigImage(String bigPictureUrl) {
            pushRequest1.setAndroidRenderStyle(2);
            pushRequest1.setSendChannels(SendChannel.accs.name());
            return this;
        }

        /**
         * 指定发送通道（测试中功能，仅可用于纯安卓应用）
         *
         * @param sendChannel 取值如下： accs：阿里自有通道 huawei：华为通道 xiaomi：小米通道 meizu：魅族通道 vivo：vivo通道 oppo：OPPO通道
         * @return this
         */
        public Android sendChannels(SendChannel sendChannel) {
            pushRequest1.setSendChannels(sendChannel.name());
            return this;
        }

        /**
         * Inbox模式下的正文，内容为合法的JSON Array，且元素不超过5个。当前支持：华为EMUI（仅长文本模式、Inbox模式下适用） 自有通道：安卓SDK3.5.0及以上
         *
         * @param inboxBody Inbox模式下的正文
         * @return this
         */
        public Android inboxBody(String inboxBody) {
            pushRequest1.setAndroidInboxBody(inboxBody);
            return this;
        }

    }

    /**
     * 小米手机推送设置，参考 <a href="https://dev.mi.com/console/doc/detail?pId=230">小米官方推送文档</a>
     */
    public static class IOS {

        private final PushBuilder taskBuilder;
        private final PushRequest pushRequest1;

        public IOS(PushBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushRequest1 = taskBuilder.getPushRequest();
            // 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。
            pushRequest1.setIOSRemind(true);
            // iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。 DEV：表示开发环境。PRODUCT：表示生产环境。
            String activeProfile = SpringUtil.getActiveProfile();
            if ("prod".equalsIgnoreCase(activeProfile)) {
                pushRequest1.setIOSApnsEnv(PushIOSApnsEnv.PRODUCT.name());
                // iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效。
                pushRequest1.setIOSRemindBody(taskBuilder.content);
            } else {
                pushRequest1.setIOSApnsEnv(PushIOSApnsEnv.DEV.name());
            }
        }

        public PushBuilder end() {
            return taskBuilder;
        }

        /**
         * 设定通知的扩展属性。当推送类型PushType设置为MESSAGE消息类型时，该属性不生效。
         *
         * @param extParameters 该参数要以 Json map 的格式传入，否则会解析出错。
         * @return this
         */
        public IOS extParameters(Map<String, Object> extParameters) {
            pushRequest1.setIOSExtParameters(JSONUtil.toJsonStr(extParameters));
            return this;
        }

        /**
         * iOS 通知声音。指定存放在app bundle或沙盒Library/Sounds目录下的音频文件名，请参见：iOS推送如何设定通知声音。 若指定为空串（””），通知为静音；若不设置，默认填充default为系统提示音。
         */
        public IOS music(String music) {
            pushRequest1.setIOSMusic(music);
            return this;
        }

        /**
         * iOS应用图标右上角角标。若设置该项，BadgeAutoIncrement 将被设置为 不开启
         *
         * @param iOSBadge 角标数
         * @return this
         */
        public IOS setIOSBadge(Integer iOSBadge) {
            pushRequest1.setIOSBadge(iOSBadge);
            pushRequest1.setIOSBadgeAutoIncrement(false);
            return this;
        }

        /**
         * 开启角标自增功能，此时，iOSBadge 设置为空。 角标自增功能由推送服务端维护每个设备的角标计数，需要用户使用V1.9.5以上版本的sdk，并且需要用户主动同步角标数字到服务端。
         *
         * @return this
         */
        public IOS enabledBadgeAutoIncrement() {
            pushRequest1.setIOSBadge(null);
            pushRequest1.setIOSBadgeAutoIncrement(true);
            return this;
        }

        /**
         * 是否开启iOS静默通知
         */
        public IOS enabledSilentNotification() {
            pushRequest1.setIOSSilentNotification(true);
            return this;
        }

        /**
         * iOS通知副标题内容（iOS 10+）
         *
         * @param iOSSubtitle 副标题内容
         * @return this
         */
        public IOS subtitle(String iOSSubtitle) {
            pushRequest1.setIOSSubtitle(iOSSubtitle);
            return this;
        }

        /**
         * 设备收到有相同CollapseId的消息，会合并成一条。设备不在线，连续发相同CollapseId的消息，通知栏只会显示一条，iOS 10+支持设置此参数。
         *
         * @param iOSNotificationCollapseId CollapseId
         * @return this
         */
        public IOS notificationCollapseId(String iOSNotificationCollapseId) {
            pushRequest1.setIOSNotificationCollapseId(null);
            return this;
        }

        /**
         * 指定iOS通知Category（iOS 10+）。
         *
         * @param iOSNotificationCategory Category
         * @return this
         */
        public IOS notificationCategory(String iOSNotificationCategory) {
            pushRequest1.setIOSNotificationCategory(null);
            return this;
        }

        /**
         * 通过该属性对iOS的远程通知进行分组，标记折叠的组别识别名。仅支持iOS 12.0+版本
         *
         * @param iOSNotificationThreadId 分组id
         * @return this
         */
        public IOS notificationThreadId(String iOSNotificationThreadId) {
            pushRequest1.setIOSNotificationThreadId(iOSNotificationThreadId);
            return this;
        }

        /**
         * 是否使能iOS通知扩展处理（iOS 10+）
         *
         * @return this
         */
        public IOS enabledMutableContent() {
            pushRequest1.setIOSMutableContent(true);
            return this;
        }
    }

    /**
     * 小米手机推送设置，参考 <a href="https://dev.mi.com/console/doc/detail?pId=230">小米官方推送文档</a>
     */
    public static class Xiaomi {

        private final PushBuilder taskBuilder;
        private final PushRequest pushRequest1;

        public Xiaomi(PushBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushRequest1 = taskBuilder.getPushRequest();
        }

        public PushBuilder end() {
            return taskBuilder;
        }

        /**
         * 设置小米通知类型的channelId，需要在小米平台申请
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Xiaomi notificationChannel(String notificationChannel) {
            pushRequest1.setAndroidNotificationXiaomiChannel(notificationChannel);
            return this;
        }

        /**
         * 大图模式下的大图URL。大图模式下图片上传至小米推送后会返回一个图片URL，本参数内填写这个URL。
         *
         * @param bigPictureUrl 小米通知类型的channelId
         * @return this
         */
        public Xiaomi bigPictureUrl(String bigPictureUrl) {
            pushRequest1.setAndroidXiaomiBigPictureUrl(bigPictureUrl);
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
            pushRequest1.setAndroidXiaomiImageUrl(imageUrl);
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
            pushRequest1.setAndroidXiaoMiNotifyBody(notifyBody);
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
            pushRequest1.setAndroidXiaoMiActivity(miActivity);
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
            pushRequest1.setAndroidXiaoMiNotifyTitle(notifyTitle);
            return this;
        }

    }

    /**
     * 华为手机推送设置，参考 <a href="https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/service-introduction-0000001050040060">华为官方推送文档</a>
     */
    public static class Huawei {

        private final PushBuilder taskBuilder;
        private final PushRequest pushRequest1;

        public Huawei(PushBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushRequest1 = taskBuilder.getPushRequest();
        }

        public PushBuilder end() {
            return taskBuilder;
        }

        /**
         * 华为通道标识高优先级透传消息的特殊场景，取值如下：PLAY_VOICE：语音播报 VOIP：VoIP电话 需申请特殊权限，
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Huawei notificationChannel(String notificationChannel) {
            pushRequest1.setAndroidNotificationHuaweiChannel(notificationChannel);
            return this;
        }

        /**
         * 华为通道标识高优先级透传消息的特殊场景，取值如下：PLAY_VOICE：语音播报 VOIP：VoIP电话 需申请特殊权限，
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Huawei notificationChannel(HuaweiChannel notificationChannel) {
            pushRequest1.setAndroidNotificationHuaweiChannel(notificationChannel.name());
            return this;
        }

        /**
         * 华为通道透传消息投递优先级，取值如下： HIGH NORMAL
         *
         * @param messageUrgency 华为通道透传消息投递优先级
         * @return this
         */
        public Huawei messageUrgency(String messageUrgency) {
            pushRequest1.setAndroidMessageHuaweiUrgency(messageUrgency);
            return this;
        }

        /**
         * 华为通道透传消息投递优先级，取值如下： HIGH NORMAL
         *
         * @param messageUrgency 华为通道透传消息投递优先级
         * @return this
         */
        public Huawei messageUrgency(HuaweiUrgency messageUrgency) {
            pushRequest1.setAndroidMessageHuaweiUrgency(messageUrgency.name());
            return this;
        }

        /**
         * 设置Huawei通知消息分类importance参数，决定用户设备消息通知行为，取值如下：LOW：资讯营销类消息 NORMAL：服务与通讯类消息 需要在Huawei平台申请
         *
         * @param messageCategory Huawei通知消息分类importance参数
         * @return this
         */
        public Huawei messageCategory(String messageCategory) {
            pushRequest1.setAndroidMessageHuaweiCategory(messageCategory);
            return this;
        }

        /**
         * 设置Huawei通知消息分类importance参数，决定用户设备消息通知行为，取值如下：LOW：资讯营销类消息 NORMAL：服务与通讯类消息 需要在Huawei平台申请
         *
         * @param messageCategory Huawei通知消息分类importance参数
         * @return this
         */
        public Huawei messageCategory(HuaweiCategory messageCategory) {
            pushRequest1.setAndroidMessageHuaweiCategory(messageCategory.name());
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

        private final PushBuilder taskBuilder;
        private final PushRequest pushRequest1;

        public Vivo(PushBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushRequest1 = taskBuilder.getPushRequest();
        }

        public PushBuilder end() {
            return taskBuilder;
        }

        /**
         * 设置vivo通知消息分类，取值为： 0：运营类消息（默认） 1：系统类消息，参考 <a href="https://dev.vivo.com.cn/documentCenter/doc/359">vivo 官方推送文档</a>
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Vivo notificationChannel(String notificationChannel) {
            pushRequest1.setAndroidNotificationVivoChannel(notificationChannel);
            return this;
        }

        /**
         * 设置vivo通知消息分类，取值为： 0：运营类消息（默认） 1：系统类消息，参考 <a href="https://dev.vivo.com.cn/documentCenter/doc/359">vivo 官方推送文档</a>
         *
         * @param notificationChannel 小米通知类型的channelId
         * @return this
         */
        public Vivo notificationChannel(VivoChannel notificationChannel) {
            pushRequest1.setAndroidNotificationVivoChannel(notificationChannel.ordinal() + "");
            return this;
        }

        public enum VivoChannel {
            OPERATING, SYSTEM
        }
    }

    /**
     * 补发短信设置，参考 <a href="https://dev.vivo.com.cn/documentCenter/doc/541">vivo 官方推送文档</a>
     */
    public static class Sms {

        private final PushBuilder taskBuilder;
        private final PushRequest pushRequest1;

        public Sms(PushBuilder taskBuilder) {
            this.taskBuilder = taskBuilder;
            this.pushRequest1 = taskBuilder.getPushRequest();
        }

        public PushBuilder end() {
            return taskBuilder;
        }

        /**
         * 补发短信的模板名，可以在短信模板管理界面获取，是系统分配的名称，而非开发者设置的名称。
         *
         * @param smsTemplateName 短信模板名称
         * @return this
         */
        public Sms smsTemplateName(String smsTemplateName) {
            pushRequest1.setSmsTemplateName(smsTemplateName);
            return this;
        }

        /**
         * 短信签名
         *
         * @param smsSignName 短信签名
         * @return this
         */
        public Sms smsSignName(String smsSignName) {
            pushRequest1.setSmsSignName(smsSignName);
            return this;
        }

        /**
         * 短信模板的变量名值对，格式： key1=value1&key2=value2。
         *
         * @param smsParams 短信模板的变量名值对，格式： key1=value1&key2=value2。
         * @return this
         */
        public Sms smsParams(String smsParams) {
            pushRequest1.setSmsParams(smsParams);
            return this;
        }

        /**
         * 短信模板的变量名值对，格式： key1=value1&key2=value2。
         *
         * @param smsParams 短信模板的变量名值对，格式： key1=value1&key2=value2。
         * @return this
         */
        public Sms smsParams(Map<String, Object> smsParams) {
            pushRequest1.setSmsParams(MapUtil.join(smsParams, "&", "="));
            return this;
        }

        /**
         * 触发短信的延迟时间，单位为秒
         *
         * @param smsDelaySecs 触发短信的延迟时间，单位为秒，推荐设置为15秒以上，避免短信和推送的重复。
         * @return this
         */
        public Sms smsDelaySecs(Integer smsDelaySecs) {
            pushRequest1.setSmsDelaySecs(smsDelaySecs);
            return this;
        }

        /**
         * 触发短信的条件
         *
         * @param smsSendPolicy 取值为： 0：推送未收到时触发。 1：用户未打开时触发。
         * @return this
         */
        public Sms smsSendPolicy(Integer smsSendPolicy) {
            pushRequest1.setSmsSendPolicy(smsSendPolicy);
            return this;
        }

        /**
         * 触发短信的条件
         *
         * @param smsSendPolicy 取值为： 0：推送未收到时触发。 1：用户未打开时触发。
         * @return this
         */
        public Sms smsSendPolicy(SmsSendPolicy smsSendPolicy) {
            pushRequest1.setSmsSendPolicy(smsSendPolicy.ordinal());
            return this;
        }

        public enum SmsSendPolicy {
            UNRECEIVE, UNREAD
        }

    }

}
