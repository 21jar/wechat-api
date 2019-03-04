package com.ainijar;

import com.ainijar.common.config.Result;
import com.ainijar.controller.HelloWorld;
import com.ainijar.dto.BaseResult;
import com.ainijar.dto.TemplateMessage;
import com.ainijar.service.IWechatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author slt
 * @date 2018/8/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml", "classpath:spring/spring-mvc.xml" })
@WebAppConfiguration
public class BaseTest {

    @Autowired
    HelloWorld helloWorld;

    @Autowired
    IWechatService IWechatService;

    @Test
    public void test1() throws Exception {
        Result result = helloWorld.say1("hello");
        System.out.println(result);
    }

    @Test
    public void test2() throws Exception {
        TemplateMessage msg = new TemplateMessage();
        msg.setTemplateId("jghn8MLQ59QfDyatxJTW_fYyZZrM6qrkVbzXf74HJZ8");
        msg.setTopcolor("#000033");
        msg.setTouser("oyzG31DoJG4h2l8GXGf_NjJz_IZI");
        msg.setUrl("www.baidu.com");
//        msg.setData();
//        String msg = "{\"touser\": \"oyzG31DoJG4h2l8GXGf_NjJz_IZI\",\"url\": \"http://rxwnuu.natappfree.cc/wechat/wxReceive\",\"topcolor\": \"#000033\",\"data\": {\"first\": {\"value\": \"这里是标题\"},\"delivername\": {\"value\": \"顺风\"},\"ordername\": {\"value\": \"3432432\"},\"productName\": {\"value\": \"小白兔\"},\"productCount\": {\"value\": \"100件\"},\"remark\": {\"value\": \"这里是备注\"}},\"template_id\": \"jghn8MLQ59QfDyatxJTW_fYyZZrM6qrkVbzXf74HJZ8\"}";
        BaseResult flag = IWechatService.sendTemplateMsg(msg);
        System.out.println(flag);
    }

}
