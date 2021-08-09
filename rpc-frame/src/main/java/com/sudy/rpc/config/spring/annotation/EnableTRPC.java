package com.sudy.rpc.config.spring.annotation;

import com.sudy.rpc.config.spring.TRPCConfiguration;
import com.sudy.rpc.config.spring.TRPCPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启动 trpc功能
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({TRPCPostProcessor.class, TRPCConfiguration.class})
public @interface EnableTRPC {
}
