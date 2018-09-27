package com.mmall.excel;//package com.mmall.excel;
//
//
//import com.mmall.model.Response.Result;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//@ControllerAdvice(basePackages = "com.mmall.controller")
//public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
//
//    Logger logger= LoggerFactory.getLogger(ApiResponseAdvice.class);
//
//    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
//        if(converterType.equals(MappingJackson2HttpMessageConverter.class)){
//            return true;
//        }
//        return false;
//    }
//
//    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType,
//                                  Class<? extends HttpMessageConverter<?>> converterType,
//                                  ServerHttpRequest serverHttpRequest,
//                                  ServerHttpResponse serverHttpResponse) {
//
//        if(converterType.equals(MappingJackson2HttpMessageConverter.class)){
//            Result apiResponse=Result.ok(body);
//            return apiResponse;
//        }
//        return body;
//    }
//}