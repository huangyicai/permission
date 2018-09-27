package com.mmall.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.mmall.model.Response.Result;
import com.mmall.model.SysMenu;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@ToString
public class SysMenuDto extends SysMenu{
    @JsonView(Result.ResultMenus.class)
    private List<SysMenuDto> sysMenus = Lists.newArrayList();

    private List<SysMenu> sysMenu = null;

    public static SysMenuDto adapt(SysMenu sysMenu) {
        SysMenuDto dto = new SysMenuDto();
        BeanUtils.copyProperties(sysMenu, dto);
        return dto;
    }
}
