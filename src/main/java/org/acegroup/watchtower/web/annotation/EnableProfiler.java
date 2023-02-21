package org.acegroup.watchtower.web.annotation;

import org.acegroup.watchtower.web.autoconfigure.EasyProfilerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Import({EasyProfilerAutoConfiguration.class})
public @interface EnableProfiler {
}
