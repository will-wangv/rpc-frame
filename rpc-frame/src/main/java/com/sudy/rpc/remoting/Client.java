package com.sudy.rpc.remoting;

import java.net.URI;

public interface Client {
    void connect(URI uri, Codec codec, Handler handler);

    TrpcChannel getChannel();
}
