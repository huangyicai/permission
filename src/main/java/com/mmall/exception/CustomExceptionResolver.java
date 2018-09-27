package com.mmall.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 *全局异常处理器
 * @author hyc
 * @since 2018-09-15
 */
@Slf4j
public class CustomExceptionResolver implements HandlerExceptionResolver{
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,  Exception ex) {
        ModelAndView mv ;
        Result result ;
        if (ex instanceof UnauthenticatedException) {
            result=Result.error(InfoEnums.SignIn);
            mv = new ModelAndView("jsonView",result.toMap());
        } else if (ex instanceof UnauthorizedException) {
            result = Result.error(InfoEnums.UNAUTHORIZATION);
            mv = new ModelAndView("jsonView",result.toMap());
        }
        else if (ex instanceof ParamException) {
            result = Result.error(InfoEnums.PARAM_NOT,ex.getMessage());
            mv = new ModelAndView("jsonView", result.toMap());
        }
        else {
            ex.printStackTrace();
            result = Result.error(InfoEnums.ERROR);
            mv = new ModelAndView("jsonView",result.toMap());
        }
        log.debug("异常:" + ex.getMessage(), ex);
        return mv;
    }
}

