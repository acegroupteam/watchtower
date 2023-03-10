package org.acegroup.watchtower.easyprofiler.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Profiler {

    @AliasFor("enable")
    boolean value() default true;

    @AliasFor("value")
    boolean enable() default true;
}
