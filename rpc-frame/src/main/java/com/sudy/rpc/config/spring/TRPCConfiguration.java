package com.sudy.rpc.config.spring;

import com.sudy.rpc.config.ProtocolConfig;
import com.sudy.rpc.config.RegistryConfig;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotationMetadata;

import java.lang.reflect.Field;

// 如何把自己创建的对象 放到 spring --  BeanDefinition
public class TRPCConfiguration implements ImportBeanDefinitionRegistrar {

    StandardEnvironment environment;

    public TRPCConfiguration(Environment environment) {
        this.environment = (StandardEnvironment) environment;
    }

    // 让spring启动的时候，装置 没有注解 /xml配置
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry,
                                 BeanNameGenerator importBeanNameGenerator) {
        // 告诉spring 让它 完成配置对象加载
        BeanDefinitionBuilder beanDefinitionBuilder = null;

        // 1.2 ProtocolConfig - 读取配置 赋值 trpc.protocol.name
        beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ProtocolConfig.class);
        for (Field field : ProtocolConfig.class.getDeclaredFields()) {
            String value = environment.getProperty("trpc.protocol." + field.getName());// 从配置文件 找到 相匹配的值
            beanDefinitionBuilder.addPropertyValue(field.getName(), value);
        }
        registry.registerBeanDefinition("protocolConfig", beanDefinitionBuilder.getBeanDefinition());

        // 1.2 RegistryConfig - 读取配置 赋值 trpc.registry.name
        beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(RegistryConfig.class);
        for (Field field : RegistryConfig.class.getDeclaredFields()) {
            String value = environment.getProperty("trpc.registry." + field.getName());// 从配置文件 找到 相匹配的值
            beanDefinitionBuilder.addPropertyValue(field.getName(), value);
        }
        registry.registerBeanDefinition("registryConfig", beanDefinitionBuilder.getBeanDefinition());

    }
}
