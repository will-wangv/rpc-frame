package com.study.dubbo.order;

import com.study.dubbo.order.api.OrderService;
import com.study.dubbo.sms.api.SmsService;
import com.sudy.rpc.config.annotation.TRpcReference;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @TRpcReference(loadbalance = "RandomLoadBalance") // 引用一个远程的服务
    SmsService smsService; //  smsService.send 本质 RPC调用 -- 网络数据传输

    public void create(String orderContent) {
        System.out.println("订单创建成功：" + orderContent);
        Object smsResult = smsService.send("10086" + UUID.randomUUID().toString(), "订单创建成功");
        System.out.println("smsService调用结果：" + smsResult);
    }
}
