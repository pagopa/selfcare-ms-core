package it.pagopa.selfcare.mscore.web.util;

import it.pagopa.selfcare.mscore.constant.GenericErrorEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@NoArgsConstructor(access = AccessLevel.NONE)
public class CustomExceptionMessage {


    public static void setCustomMessage(GenericErrorEnum genericErrorEnum){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        request.setAttribute("errorEnum", genericErrorEnum);
    }
}
