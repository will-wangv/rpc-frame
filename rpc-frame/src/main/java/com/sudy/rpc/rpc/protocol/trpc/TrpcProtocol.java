package com.sudy.rpc.rpc.protocol.trpc;

import com.sudy.rpc.common.serialize.Serialization;
import com.sudy.rpc.common.tools.SpiUtils;
import com.sudy.rpc.common.tools.URIUtils;
import com.sudy.rpc.remoting.Client;
import com.sudy.rpc.remoting.Transporter;
import com.sudy.rpc.rpc.Invoker;
import com.sudy.rpc.rpc.Response;
import com.sudy.rpc.rpc.RpcInvocation;
import com.sudy.rpc.rpc.protocol.Protocol;
import com.sudy.rpc.rpc.protocol.trpc.handler.TrpcClientHandler;
import com.sudy.rpc.rpc.protocol.trpc.handler.TrpcServerHandler;
import com.sudy.rpc.rpc.protocol.trpc.codec.TrpcCodec;

import java.net.URI;

public class TrpcProtocol implements Protocol {
    // 导出 -- 创建服务
    @Override
    public void export(URI exportUri, Invoker invoker) {
        // 找到序列化
        String serializationName = URIUtils.getParam(exportUri, "serialization");
        Serialization serialization = (Serialization) SpiUtils.getServiceImpl(serializationName, Serialization.class);

        // 1. 编解码器
        TrpcCodec trpcCodec = new TrpcCodec();
        trpcCodec.setDecodeType(RpcInvocation.class);
        trpcCodec.setSerialization(serialization);
        // 2. 收到请求处理器
        TrpcServerHandler trpcServerHandler = new TrpcServerHandler();
        trpcServerHandler.setInvoker(invoker);
        trpcServerHandler.setSerialization(serialization);
        // 3. 底层网络框架
        String transporterName = URIUtils.getParam(exportUri, "transporter");
        Transporter transporter = (Transporter) SpiUtils.getServiceImpl(transporterName, Transporter.class);
        // 4. 启动服务
        transporter.start(exportUri, trpcCodec, trpcServerHandler);
    }

    @Override
    public Invoker refer(URI consumerUri) {
        // 1. 找到序列化
        String serializationName = URIUtils.getParam(consumerUri, "serialization");
        Serialization serialization = (Serialization) SpiUtils.getServiceImpl(serializationName, Serialization.class);
        // 2. 编解码器
        TrpcCodec codec = new TrpcCodec();
        codec.setDecodeType(Response.class); // 客户端 -- 解码 -- 服务端发送过来的响应
        codec.setSerialization(serialization);
        // 3. 收到响应 处理
        TrpcClientHandler trpcClientHandler = new TrpcClientHandler();
        // 4. 连接 -- 长连接
        String transporterName = URIUtils.getParam(consumerUri, "transporter");
        Transporter transporter = (Transporter) SpiUtils.getServiceImpl(transporterName, Transporter.class);
        Client connect = transporter.connect(consumerUri, codec, trpcClientHandler);
        // 5. 创建一个invoker 通过网络连接发送数据
        TrpcClientInvoker trpcClientInvoker = new TrpcClientInvoker(connect, serialization);
        return trpcClientInvoker;
    }
}
