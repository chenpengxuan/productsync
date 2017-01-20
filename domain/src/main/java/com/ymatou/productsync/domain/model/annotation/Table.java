package com.ymatou.productsync.domain.model.annotation;

/**
 * Created by zhangyifan on 2016/12/13.
 */

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Table {
    /**
     * The key to be used to store the field inside the document.
     *
     * @return
     */
    String value() default "";
}
