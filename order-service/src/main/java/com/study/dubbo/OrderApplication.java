package com.study.dubbo;

import com.study.dubbo.order.api.OrderService;
import com.sudy.rpc.config.spring.annotation.EnableTRPC;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.CyclicBarrier;

@Configuration
@ComponentScan("com.study.dubbo")
@PropertySource("classpath:/trpc.properties")
@EnableTRPC
public class OrderApplication {
    public static void main(String[] args) throws Exception {
        final AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(OrderApplication.class);
        context.start();

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(1);
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        cyclicBarrier.await();
                        // 测试..模拟调用接口 -- 一定是远程，因为当前的系统没有具体实现类
                        OrderService orderService = context.getBean(OrderService.class);
                        orderService.create("买一瓶水");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


        // 阻塞不退出
        System.in.read();
        context.close();
    }
}