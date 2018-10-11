package com.mmall.model.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
    public interface ResultMenus{}

    @JsonView(ResultMenus.class)
    private Integer code;
    @JsonView(ResultMenus.class)
    private String info;
    @JsonView(ResultMenus.class)
    private T data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public Result(T data) {
        this(InfoEnums.SUCCESS, data);
    }

    public Result(InfoEnums code) {
        this(code.getCode(), code.getInfo(), null);
    }

    public Result(InfoEnums code, T data) {
        this(code.getCode(), code.getInfo(), data);
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.info = message;
        this.data = data;
    }
    public Result(Integer code, String message) {
        this.code = code;
        this.info = message;
    }

    /**
     * 成功时候的调用 有data
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T data){
        return new Result<T>(data);
    }

    /**
     * 成功，无data
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Result<T> ok() {
        return (Result<T>) ok(null);
    }
    /**
     * 失败时候的调用
     * @return
     */
    public static <T> Result<T> error(InfoEnums info){
        return new Result<T>(info);
    }
    /**
     * 失败时候的调用,扩展消息参数
     * @param cm
     * @param msg
     * @return
     */
    public static <T> Result<T> error(InfoEnums cm, String msg) {
        cm.setInfo(msg);
        return new Result<T>(cm);
    }


    public  Map<String,Object> toMap(){
        HashMap<String ,Object> map = Maps.newHashMap();
        map.put("code",code);
        map.put("info",info);
        map.put("data",data);
        return map;
    }
}
