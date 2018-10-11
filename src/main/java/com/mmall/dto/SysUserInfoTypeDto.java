package com.mmall.dto;

import com.mmall.model.CourierCompany;
import com.mmall.model.SysUserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@ToString
public class SysUserInfoTypeDto extends SysUserInfo {
    private CourierCompany courierCompany;

    public static SysUserInfoTypeDto adapt(SysUserInfo sysUserInfo) {
        SysUserInfoTypeDto dto = new SysUserInfoTypeDto();
        BeanUtils.copyProperties(sysUserInfo, dto);
        return dto;
    }
}
