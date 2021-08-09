package com.sudy.rpc.rpc.protocol.trpc.handler;

import com.sudy.rpc.common.serialize.Serialization;
import com.sudy.rpc.remoting.Handler;
import com.sudy.rpc.remoting.TrpcChannel;
import com.sudy.rpc.rpc.Invoker;
import com.sudy.rpc.rpc.Response;
import com.sudy.rpc.rpc.RpcInvocation;

public class TrpcServerHandler implements Handler {

    // message 就是 rpcinvocation
    @Override
    public void onReceive(TrpcChannel trpcChannel, Object message) throws Exception {
        RpcInvocation rpcInvocation = (RpcInvocation) message;
        System.out.println("收到rpcInvocation信息：" + rpcInvocation);
        // TODO 发起方法调用 -- 谁？
        // 发出数据 -- response
        Response response = new Response();
        try {
            // 调用目标 接口实现类
            Object result = getInvoker().invoke(rpcInvocation);
            response.setRequestId(rpcInvocation.getId());
            response.setStatus(200);
            response.setContent(result);
            System.out.println("服务端执行结果：" + result);
        } catch (Throwable e) {
            response.setStatus(99);
            response.setContent(e.getMessage());
            e.printStackTrace();
        }
        // 发送数据
        byte[] responseBody = getSerialization().serialize(response);
        trpcChannel.send(responseBody); // write方法
    }

    @Override
    public void onWrite(TrpcChannel trpcChannel, Object message) throws Exception {

    }

    Invoker invoker;

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public Invoker getInvoker() {
        return this.invoker;
    }

    Serialization serialization;

    public void setSerialization(Serialization serialization) {
        this.serialization = serialization;
    }

    public Serialization getSerialization() {
        return this.serialization;
    }
}
