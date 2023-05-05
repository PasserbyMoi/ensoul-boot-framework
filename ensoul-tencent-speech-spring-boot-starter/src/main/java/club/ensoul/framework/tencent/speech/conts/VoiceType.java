package club.ensoul.framework.tencent.speech.conts;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wy_peng_chen6
 */
public enum VoiceType {

    // 标准音色
    BZ_TY(1, "默认模型", ""),
    BZ_TY_ZhiLing(1002, "智聆", "通用女声"),
    BZ_TY_ZhiLi(1005, "智莉", "通用女声"),
    BZ_TY_ZhiYun(1004, "智云", "通用男声"),
    BZ_TY_ZhiHua(1010, "智华", "通用男声"),
    BZ_QG_ZhiYu(1001, "智瑜", "情感女声"),
    BZ_QG_ZhiRong(1017, "智蓉", "情感女声"),
    BZ_QG_ZhiJing(1018, "智靖", "情感男声"),
    BZ_KF_ZhiNa(1007, "智娜", "客服女声"),
    BZ_KF_ZhiQi(1008, "智琪", "客服女声"),
    BZ_KF_ZhiMei(1003, "智美", "客服女声"),
    BZ_ZX_ZhiYun(1009, "智芸", "知性女声"),
    BZ_WeJack(1050, "WeJack", "英文男声"),
    BZ_WeRose(1051, "WeRose", "英文女声"),

    // 精品音色: 精品音色拟真度更高，价格不同于标准音色，查看购买指南
    YD_ZhiXiaoYao(100510000, "智逍遥", "阅读男声"),
    QG_Zhiyu(101001, "智瑜", "情感女声"),
    QG_ZhiRong(101017, "智蓉", "情感女声"),
    QG_ZhiJing(101018, "智靖", "情感男声"),
    ZX_ZhiYun(101009, "智芸", "知性女声"),
    ZS_ZhiYan(101006, "智言", "助手女声"),


    LT_ZhiHao(101024, "智皓", "聊天男声"),
    LT_ZhiXuan(101023, "智萱", "聊天女声"),
    LT_ZhiWei(101025, "智薇", "聊天女声"),

    NT_ZhiMeng(101015, "智萌", "男童声"),
    NT_ZhiTian(101016, "智甜", "女童声"),

    YY_ZhiTong(101019, "智彤", "粤语女声"),
    SC_ZhiChuang(101040, "智川", "四川女声"),
    DB_ZhiLin(101056, "智林", "东北男声"),

    EN_WeJack(101050, "WeJack", "英文男声"),
    EN_WeRose(101051, "WeRose", "英文女声"),


    TY_ZhiLing(101002, "智聆", "通用女声"),
    TY_ZhiYun(101004, "智云", "通用男声"),
    TY_ZhiLi(101005, "智莉", "通用女声"),
    TY_ZhiHua(101010, "智华", "通用男声"),
    TY_ZhiXi(101026, "智希", "通用女声"),
    TY_ZhiMei(101027, "智梅", "通用女声"),
    TY_ZhiJie(101028, "智洁", "通用女声"),
    TY_ZhiKai(101029, "智凯", "通用男声"),
    TY_ZhiKe(101030, "智柯", "通用男声"),
    TY_ZhiKui(101031, "智奎", "通用男声"),
    TY_ZhiFang1(101032, "智芳", "通用女声"),
    TY_ZhiLian(101034, "智莲", "通用女声"),
    TY_ZhiWei(101052, "智味", "通用男声"),
    TY_ZhiFang2(101053, "智方", "通用男声"),
    TY_ZhiYou(101054, "智友", "通用男声"),
    TY_ZhiFu(101055, "智付", "通用女声"),
    TY_ZhiYi(101035, "智依", "通用女声"),

    KF_ZhiBei(101033, "智蓓", "客服女声"),
    KF_ZhiNa(101007, "智娜", "客服女声"),
    KF_ZhiQi(101008, "智琪", "客服女声"),
    KF_ZhiMei(101003, "智美", "客服女声"),

    XW_ZhiNing(101014, "智宁", "新闻男声"),
    XW_ZhiGang(101020, "智刚", "新闻男声"),
    XW_ZhiRui(101021, "智瑞", "新闻男声"),
    XW_ZhiHui(101013, "智辉", "新闻男声"),
    XW_ZhiHong(101022, "智虹", "新闻女声"),
    XW_ZhiYan(101011, "智燕", "新闻女声"),
    XW_ZhiDan(101012, "智丹", "新闻女声"),

    DQG_AiXiaoGuang(301000, "爱小广", "多情感通用男声"),
    DQG_AiXiaoDong(301001, "爱小栋", "多情感通用男声"),
    DQG_AiXiaoHai(301002, "爱小海", "多情感通用男声"),
    DQG_AiXiaoXia(301003, "爱小霞", "多情感通用女声"),
    DQG_AiXiaoLing(301004, "爱小玲", "多情感通用女声"),
    DQG_AiXiaoZhang(301005, "爱小章", "多情感通用男声"),
    DQG_AiXiaoFeng(301006, "爱小峰", "多情感通用男声"),
    DQG_AiXiaoLiang(301007, "爱小亮", "多情感通用男声"),
    DQG_AiXiaoBo(301008, "爱小博", "多情感通用男声"),
    DQG_AiXiaoYun(301009, "爱小芸", "多情感通用女声"),
    DQG_AiXiaoQiu(301010, "爱小秋", "多情感通用女声"),
    DQG_AiXiaoFang(301011, "爱小芳", "多情感通用女声"),
    DQG_AiXiaoQin(301012, "爱小琴", "多情感通用女声"),
    DQG_AiXiaoKang(301013, "爱小康", "多情感通用男声"),
    DQG_AiXiaoHui(301014, "爱小辉", "多情感通用男声"),
    DQG_AiXiaoLu(301015, "爱小璐", "多情感通用女声"),
    DQG_AiXiaoYang(301016, "爱小阳", "多情感通用男声"),
    DQG_AiXiaoQuan(301017, "爱小泉", "多情感通用男声"),
    DQG_AiXiaokun(301018, "爱小昆", "多情感通用男声"),
    DQG_AiXiaoCheng(301019, "爱小诚", "多情感通用男声"),
    DQG_AiXiaoLang(301020, "爱小岚", "多情感通用女声"),
    DQG_AiXiaoRu(301021, "爱小茹", "多情感通用女声"),
    DQG_AiXiaoRong(301022, "爱小蓉", "多情感通用女声"),
    DQG_AiXiaoYan(301023, "爱小燕", "多情感通用女声"),
    DQG_AiXiaoLian(301024, "爱小莲", "多情感通用女声"),
    DQG_AiXiaoWu(301025, "爱小武", "多情感通用男声"),
    DQG_AiXiaoXue(301026, "爱小雪", "多情感通用女声"),
    DQG_AiXiaoYuan(301027, "爱小媛", "多情感通用女声"),
    DQG_AiXiaoXian(301028, "爱小娴", "多情感通用女声"),
    DQG_AiXiaoTao(301029, "爱小涛", "多情感通用男声");

    public final long code;
    public final String name;
    public final String desc;

    VoiceType(long code, String name, String desc) {
        this.code = code;
        this.name = name;
        this.desc = desc;
    }

    public Map<Long, String> toMap() {
        Map<Long, String> map = new HashMap<>(VoiceType.values().length);
        for (VoiceType voiceType : VoiceType.values()) {
            map.put(voiceType.code, voiceType.desc + "：" + voiceType.name);
        }
        return map;
    }
}
