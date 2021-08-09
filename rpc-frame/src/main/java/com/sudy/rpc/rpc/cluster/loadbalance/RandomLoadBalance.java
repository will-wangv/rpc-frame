package com.sudy.rpc.rpc.cluster.loadbalance;

import com.sudy.rpc.rpc.cluster.LoadBalance;
import com.sudy.rpc.rpc.Invoker;

import java.net.URI;
import java.util.Map;
import java.util.Random;

public class RandomLoadBalance implements LoadBalance {
    @Override
    public Invoker select(Map<URI, Invoker> invokerMap) {
        int index = new Random().nextInt(invokerMap.values().size());
        return invokerMap.values().toArray(new Invoker[]{})[index];
    }
}
