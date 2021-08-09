package com.sudy.rpc.tools;

import com.sudy.rpc.common.serialize.Serialization;

import java.util.ServiceLoader;

public class SpiTest {
    public static void main(String[] args) {
        // 创建 具体实现 -- jdk 加载 （jar -- 第三方 - 拓展性）
        ServiceLoader services = ServiceLoader.load(Serialization.class, Thread.currentThread().getContextClassLoader());
        for (Object s : services) {
            System.out.println(s.getClass().getSimpleName());
        }
    }
}
