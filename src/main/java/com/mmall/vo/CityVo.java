package com.mmall.vo;

import com.mmall.model.City;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Getter
@Setter
@ToString
public class CityVo extends City {

    private Boolean status=false;

    public static CityVo adapt(City city) throws InvocationTargetException, IllegalAccessException {
        CityVo dto = new CityVo();
        BeanUtils.copyProperties(city, dto);
        return dto;
    }


}
