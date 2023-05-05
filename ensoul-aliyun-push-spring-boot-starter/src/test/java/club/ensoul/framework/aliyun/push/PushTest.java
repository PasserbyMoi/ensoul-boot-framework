//package com.shimaoiot.framework.aliyun.push;
//
//import cn.hutool.json.JSONUtil;
//import com.shimaoiot.framework.aliyun.push.consts.PushTarget;
//import com.shimaoiot.framework.aliyun.push.domain.PushResult;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author wy_peng_chen6
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class PushTest {
//
//    @Resource
//    private AliyunPushTemplate aliyunPushTemplate;
//
//    private Long appKey = 122222L;
//    private String title = "测试推送消息标题";
//    private String content = "测试推送消息内容";
//    private PushTarget pushTarget = PushTarget.ACCOUNT;
//    private String jobKey = "jobKey";
//    private Iterable<String> targetValues = new ArrayList<String>(){{
//        add("18512592325");
//    }};
//
//    @Test
//    public void pushAndroid(){
//        PushResult pushResult = aliyunPushTemplate.pushMessageToAndroid(appKey, title, content, pushTarget, targetValues, jobKey);
//        System.out.println(JSONUtil.toJsonStr(pushResult));
//    }
//
//}
