package com.study.dubbo.sms;

import com.study.dubbo.sms.api.SmsService;
import com.sudy.rpc.config.annotation.TRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

// 面向java接口 远程调用
@TRpcService // 告诉rpc框架 此服务需要开放
public class SmsServiceImpl implements SmsService {
    public Object send(String phone, String content) {
        System.out.println("发送短信：" + phone + ":" + content);
        return "短信发送成功" + UUID.randomUUID().toString();
    }
}
