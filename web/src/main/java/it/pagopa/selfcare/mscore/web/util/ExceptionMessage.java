package it.pagopa.selfcare.mscore.web.util;

import it.pagopa.selfcare.mscore.constant.ErrorEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExceptionMessage {

    ErrorEnum message() default ErrorEnum.ERROR;

}
