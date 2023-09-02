package dev.omo.guihaul.docs;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface WikiFieldDesc {
    boolean optional();
    boolean bodyIsMap() default false;
    Class<?> mapValueClass() default Void.class;
    String mapKeyExample() default "any";
}
