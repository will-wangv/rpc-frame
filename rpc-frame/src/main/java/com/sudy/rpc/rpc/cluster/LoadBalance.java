package com.sudy.rpc.rpc.cluster;

import com.sudy.rpc.rpc.Invoker;

import java.net.URI;
import java.util.Map;

// 负载均衡器 --
public interface LoadBalance {
    // 传入集合 -- 选择其中一个
    public Invoker select(Map<URI, Invoker> invokerMap);
}
