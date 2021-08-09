package com.sudy.rpc.config.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记这个类要成为一个服务提供者
 *
 * @author Tony
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface TRpcService {
    /**
     * 如果有多个接口，需要自己指定一个
     */
    Class<?> interfaceClass() default void.class;

}
