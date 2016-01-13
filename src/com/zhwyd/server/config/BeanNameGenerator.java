package com.zhwyd.server.config;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
public class BeanNameGenerator implements org.springframework.beans.factory.support.BeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        String beanName = beanDefinition.getBeanClassName().substring(beanDefinition.getBeanClassName().lastIndexOf('.') + 1);
        beanName = StringUtils.uncapitalize(beanName);
        beanName = beanName.replaceAll("Impl$", "");
        return beanName;
    }
}
