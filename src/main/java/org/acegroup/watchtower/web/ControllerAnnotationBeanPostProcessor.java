package org.acegroup.watchtower.web;

import org.acegroup.watchtower.web.annotation.Profiler;
import org.acegroup.watchtower.web.autoconfigure.EasyProfilerConfigurationProperties;
import org.acegroup.watchtower.web.controller.ProfilerController;
import org.acegroup.watchtower.web.model.Profile;
import org.acegroup.watchtower.web.util.ProfileInfoHolder;
import org.acegroup.watchtower.web.util.ProfilerQueue;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class ControllerAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    EasyProfilerConfigurationProperties properties;

    private ProfilerQueue queue = new ProfilerQueue();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }


    public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
        ProfileInfoHolder.setStartTime(LocalDateTime.now());
        final Class<?> beanClass = bean.getClass();
        final String beanClassName = beanClass.getName();
        String basePackage = properties.getBasePackage();
        if (basePackage != null && !beanClassName.startsWith(basePackage)) {
            return bean;
        }
        if (beanClassName.startsWith("org.springframework") || bean instanceof ProfilerController) {
            return bean;
        }
        if (!AnnotatedElementUtils.hasAnnotation(beanClass, Controller.class)) {
            return bean;
        }
        Profiler profiler = AnnotationUtils.findAnnotation(beanClass, Profiler.class);
        if (profiler != null && !profiler.enable()) {
            return bean;
        }
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(bean);
        proxyFactory.addAdvice((MethodInterceptor) invocation -> {
            Method method = invocation.getMethod();
            Profiler methodProfiler = AnnotationUtils.findAnnotation(method, Profiler.class);
            if (methodProfiler != null && !methodProfiler.enable()) {
                return method.invoke(bean, invocation.getArguments());
            }
            RequestMapping requestMappingAnnotation = AnnotatedElementUtils.getMergedAnnotation(method, RequestMapping.class);
            if (requestMappingAnnotation == null) {
                return method.invoke(bean, invocation.getArguments());
            }
            return wrapInvoke(requestMappingAnnotation, bean, invocation, method);
        });
        return proxyFactory.getProxy();
    }

    private Object wrapInvoke(RequestMapping requestMappingAnnotation, Object bean, MethodInvocation invocation, Method method) throws Throwable {
        String uri = "";
        boolean invokeSuccess = true;
        long startAt = 0, endAt = 0;
        try {
            RequestMapping restController = AnnotationUtils.findAnnotation(bean.getClass(), RequestMapping.class);
            if (restController != null) {
                uri = restController.value()[0];
            }
            ProfileInfoHolder.processingCount.incrementAndGet();
            uri += requestMappingAnnotation.value()[0];
            startAt = System.currentTimeMillis();
            Object result = method.invoke(bean, invocation.getArguments());
            endAt = System.currentTimeMillis();
            return result;
        } catch (InvocationTargetException e) {
            endAt = System.currentTimeMillis();
            Throwable cause = e.getCause();
            if (properties.getExcludeClass() != null && !(cause.getClass().getName().equals(properties.getExcludeClass()))) {
                invokeSuccess = false;
            } else {
                ProfileInfoHolder.bizExCount.incrementAndGet();
            }
            throw cause;
        } finally {
            ProfileInfoHolder.processingCount.decrementAndGet();
            saveInvokeInfo(startAt, endAt, uri, bean.getClass(), method, invokeSuccess);

        }
    }


    private void saveInvokeInfo(long start, long end, String uri, Class clazz, Method method, boolean occurError) {
        Profile info = new Profile();
        info.setStartTime(start);
        info.setEndTime(end);
        info.setUri(uri);
        info.setClazz(clazz);
        info.setMethod(method);
        info.setSuccess(occurError);
        queue.addProfileInfo(info);
    }
}
