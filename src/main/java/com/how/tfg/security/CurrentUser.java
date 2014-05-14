package com.how.tfg.security;

import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Created by Sergio on 19/04/2014.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {

}
