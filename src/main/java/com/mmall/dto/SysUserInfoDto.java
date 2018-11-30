package com.mmall.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SysUserInfoDto extends SysUserInfo {
    public interface UserInfoView extends Result.ResultMenus {}

    @JsonView(UserInfoView.class)
    private List<SysUserInfoDto> sysUserInfos = null;

    private List<SysUserInfo> sysMenu = null;

    public static SysUserInfoDto adapt(SysUserInfo sysUserInfo) {
        SysUserInfoDto dto = new SysUserInfoDto();
        BeanUtils.copyProperties(sysUserInfo, dto);
        return dto;
    }

}
