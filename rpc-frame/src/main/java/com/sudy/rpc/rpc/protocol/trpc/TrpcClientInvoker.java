package com.sudy.rpc.rpc.protocol.trpc;

import com.sudy.rpc.common.serialize.Serialization;
import com.sudy.rpc.remoting.Client;
import com.sudy.rpc.rpc.Invoker;
import com.sudy.rpc.rpc.Response;
import com.sudy.rpc.rpc.RpcInvocation;
import com.sudy.rpc.rpc.protocol.trpc.handler.TrpcClientHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class TrpcClientInvoker implements Invoker {
    Client client;
    Serialization serialization;

    public TrpcClientInvoker(Client client, Serialization serialization) {
        this.client = client;
        this.serialization = serialization;
    }
    @Override
    public Class getInterface() {
        return null;
    }

    @Override
    public Object invoke(RpcInvocation rpcInvocation) throws Exception {
        // 1. 序列化 rpcInvocation -- 根据服务提供者的配置决定
        byte[] requestBody = serialization.serialize(rpcInvocation);
        // 2. 发起请求 -- rpcInvocation -- 协议数据包 -- 编码
        this.client.getChannel().send(requestBody);
        // 3.另一个线程 接收响应? TODO ? 解码--> handler
        // 实现 等待结果的
        CompletableFuture completableFuture = TrpcClientHandler.waitResult(rpcInvocation.getId());
        // future.get 获取结果
        Object result = completableFuture.get(60, TimeUnit.SECONDS);
        Response response = (Response) result;
        if(response.getStatus() == 200) {
            return response.getContent();
        } else {
            throw new Exception("server error:" + response.getContent().toString());
        }
    }
}
