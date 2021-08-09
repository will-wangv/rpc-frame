package com.sudy.rpc.protocol;

import com.sudy.rpc.common.serialize.json.JsonSerialization;
import com.sudy.rpc.remoting.netty.Netty4Transporter;
import com.sudy.rpc.rpc.protocol.trpc.handler.TrpcServerHandler;
import com.sudy.rpc.rpc.RpcInvocation;
import com.sudy.rpc.rpc.protocol.trpc.codec.TrpcCodec;

import java.net.URI;
import java.net.URISyntaxException;

// 集成了trpc 一套协议处理机制
public class TrpcProtocolTransporterTest {
    public static void main(String[] args) throws URISyntaxException {
        TrpcCodec trpcCodec = new TrpcCodec();
        trpcCodec.setDecodeType(RpcInvocation.class);
        trpcCodec.setSerialization(new JsonSerialization());

        TrpcServerHandler trpcServerHandler = new TrpcServerHandler();
        new Netty4Transporter().start(new URI("TRPP://127.0.0.1:8080"),
                trpcCodec,trpcServerHandler );
    }
}
