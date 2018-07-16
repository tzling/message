package com.tzl.routing;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Inherited
public @interface RequestMapping {
    String value() default "";
}
