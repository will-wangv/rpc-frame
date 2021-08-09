package com.sudy.rpc.config.spring;

import com.sudy.rpc.config.ProtocolConfig;
import com.sudy.rpc.config.ReferenceConfig;
import com.sudy.rpc.config.RegistryConfig;
import com.sudy.rpc.config.ServiceConfig;
import com.sudy.rpc.config.annotation.TRpcReference;
import com.sudy.rpc.config.annotation.TRpcService;
import com.sudy.rpc.config.util.TrpcBootstrap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

/**
 * spring扫描 初始化对象之后， 我要找到里面 TRpcService
 */
public class TRPCPostProcessor implements ApplicationContextAware, InstantiationAwareBeanPostProcessor {
    ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * bean 做完 其他拓展
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 1. 服务 提供者
        if(bean.getClass().isAnnotationPresent(TRpcService.class)) {
            System.out.println("找到了需要开放网络访问的service实现类。构建serviceConfig配置");
            ServiceConfig serviceConfig = new ServiceConfig();
            serviceConfig.addProtocolConfig(applicationContext.getBean(ProtocolConfig.class));
            serviceConfig.addRegistryConfig(applicationContext.getBean(RegistryConfig.class));
            serviceConfig.setReference(bean);

            TRpcService tRpcService = bean.getClass().getAnnotation(TRpcService.class);
            if (void.class == tRpcService.interfaceClass()) {
                serviceConfig.setService(bean.getClass().getInterfaces()[0]);
            } else {
                serviceConfig.setService(tRpcService.interfaceClass());
            }

            // boot
            TrpcBootstrap.export(serviceConfig);

        }

        // 2. 服务引用- 注入
        for (Field field : bean.getClass().getDeclaredFields()) {
            try {
                if (!field.isAnnotationPresent(TRpcReference.class)) {
                    continue; // 不继续下面的代码，继续循环
                }
                // 引用相关 配置 保存在一个对象里边 // TODO 思考：如果一个引用需要在多个类被使用
                ReferenceConfig referenceConfig = new ReferenceConfig();
                referenceConfig.addRegistryConfig(applicationContext.getBean(RegistryConfig.class));
                referenceConfig.addProtocolConfig(applicationContext.getBean(ProtocolConfig.class));
                referenceConfig.setService(field.getType());

                TRpcReference tRpcReference = field.getAnnotation(TRpcReference.class);
                referenceConfig.setLoadbalance(tRpcReference.loadbalance());

                Object referenceBean = TrpcBootstrap.getReferenceBean(referenceConfig);
                field.setAccessible(true);
                field.set(bean, referenceBean);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bean;
    }


}
