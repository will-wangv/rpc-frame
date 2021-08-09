package com.sudy.rpc.remoting;

// 由具体的协议去实现
public interface Handler {
    // 收到数据 【发过来的请求、服务器给的响应】
    void onReceive(TrpcChannel trpcChannel, Object message) throws Exception;

    void onWrite(TrpcChannel trpcChannel, Object message) throws Exception;
}
