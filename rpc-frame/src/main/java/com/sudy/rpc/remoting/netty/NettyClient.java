package com.sudy.rpc.remoting.netty;

import com.sudy.rpc.remoting.Client;
import com.sudy.rpc.remoting.Codec;
import com.sudy.rpc.remoting.TrpcChannel;
import com.sudy.rpc.remoting.Handler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.URI;

public class NettyClient implements Client {
    TrpcChannel channel = null;
    EventLoopGroup group = null;

    @Override
    public void connect(URI uri, Codec codec, Handler handler) {
        try {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            // 指定事件循环组
            bootstrap.group(group)
                    // 指定所使用的nio传输channel
                    .channel(NioSocketChannel.class)
                    // 添加一个handler
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyCodec(codec.createInstance()));
                            ch.pipeline().addLast(new NettyHandler(handler));
                        }
                    });
            // 同步-- 创建连接
            ChannelFuture channelFuture = bootstrap.connect(uri.getHost(), uri.getPort()).sync();
            channel = new NettyChannel(channelFuture.channel());

            // 优雅停机 -- kill pid -- 响应退出信号
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        System.out.println("我要停机了");
                        synchronized (NettyServer.class) {
                            group.shutdownGracefully().sync();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public TrpcChannel getChannel() {
        return channel;
    }

    public void setChannel(TrpcChannel channel) {
        this.channel = channel;
    }

}
