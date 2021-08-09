package com.sudy.rpc.proxy;

import com.sudy.rpc.TestService;
import com.sudy.rpc.rpc.proxy.ProxyFactory;
import com.sudy.rpc.rpc.Invoker;
import com.sudy.rpc.rpc.RpcInvocation;

public class ProxyTest {
    public static void main(String[] args) {
        // 被代理对象 -- invoke RpcInvocation -- 需要注入的serice接口相关的描述
        Invoker invoker = new Invoker() {
            @Override
            public Class getInterface() {
                return null;
            }

            @Override
            public Object invoke(RpcInvocation rpcInvocation) throws Exception {
                // 把方法调用的相关描述，转成了一个 rpcInvocation 对象
                // TODO 此处就是客户端发起远程接口访问的地方，通过网络，调用远程，返回结果
                return "这是远程访问接口的结果";
            }
        };

        TestService helloService = (TestService) ProxyFactory.getProxy(invoker, new Class[]{TestService.class});
        String result = helloService.say("Tony");
        System.out.println(result);
    }
}
