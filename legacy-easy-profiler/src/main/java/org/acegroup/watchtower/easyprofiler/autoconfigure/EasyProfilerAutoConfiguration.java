package org.acegroup.watchtower.easyprofiler.autoconfigure;

import org.acegroup.watchtower.easyprofiler.ControllerAnnotationBeanPostProcessor;
import org.acegroup.watchtower.easyprofiler.web.ProfilerController;
import org.acegroup.watchtower.easyprofiler.util.EasyProfilerUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@ConditionalOnProperty(prefix = EasyProfilerUtil.EASY_PROFILER_PREFIX, name = "enabled", matchIfMissing = true, havingValue = "true")
@Configuration
@EnableConfigurationProperties(EasyProfilerConfigurationProperties.class)
public class EasyProfilerAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ControllerAnnotationBeanPostProcessor profileBeanPostProcessor() {
        return new ControllerAnnotationBeanPostProcessor();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProfilerController profilerController(EasyProfilerConfigurationProperties easyProfilerConfigurationProperties) {
        return new ProfilerController(easyProfilerConfigurationProperties);
    }

}
