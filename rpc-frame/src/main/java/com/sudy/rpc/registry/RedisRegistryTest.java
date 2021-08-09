package com.sudy.rpc.registry;

import com.sudy.rpc.registry.redis.RedisRegistry;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class RedisRegistryTest {
    public static void main(String[] args) throws URISyntaxException {
        RedisRegistry redisRegistry = new RedisRegistry();
        redisRegistry.init(new URI("RedisRegistry://127.0.0.1:6379"));

        redisRegistry.subscribe("com.study.dubbo.sms.api.SmsService", new NotifyListener() {
            @Override
            public void notify(Set<URI> uris) {
                System.out.println(uris);
            }
        });
    }
}
