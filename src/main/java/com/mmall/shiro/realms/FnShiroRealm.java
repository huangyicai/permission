package com.mmall.shiro.realms;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mmall.dao.SysRoleMapper;
import com.mmall.dao.SysUserMapper;
import com.mmall.model.SysRole;
import com.mmall.model.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Set;

/**
 *
 *
 * @author hyc
 * @since 2018-09-15
 */
@Slf4j
public class FnShiroRealm extends AuthorizingRealm {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String)principals.getPrimaryPrincipal();
        //获取用户角色
        Set<String> roles = getRolesByUsername(username);
        //获取用户权限
        Set<String> permissions = getPermissionsByUsername(username);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //添加权限
        authorizationInfo.addStringPermissions(permissions);
        //添加角色
        authorizationInfo.addRoles(roles);

        return authorizationInfo;
    }


    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.从主体传过来的认证信息中获取用户名
        String username = (String)token.getPrincipal();
        //通过用户名获取数据库密码
        String password = getPasswordByUsername(username);
        if(password==null){
            return null;
        }
        //Md5Hash md = new Md5Hash(password,username,1024);
        //3.提交认证信息
        SimpleAuthenticationInfo authenticationInfo =new SimpleAuthenticationInfo(
                username, //用户名
                password, //密码
                ByteSource.Util.bytes(username),//salt=username 盐值
                this.getName()  //realm name
        );

        return authenticationInfo;

    }

    /**
     * 通过用户名获取权限
     * @param username
     * @return
     */
    private Set<String> getPermissionsByUsername(String username) {
        Set<String> permissions = Sets.newHashSet();
        permissions.add("user:save");
        return permissions;
    }

    /**
     * 通过用户名获取角色
     * @param username
     * @return
     */
    private Set<String> getRolesByUsername(String username) {
        log.info("MYSQL*************");
        Set<String> roles = Sets.newHashSet();
        Set<SysRole> userRoleByusername = sysRoleMapper.findUserRoleByusername(username);
        for(SysRole s:userRoleByusername){
            roles.add(s.getRole());
        }
        return roles;
    }

    /**
     * 通过用户名获取密码
     * @param username
     * @return
     */
    private String getPasswordByUsername(String username){
        SysUser userByusername = sysUserMapper.findUserByusername(username);
        if(userByusername==null){
            return null;
        }
        return userByusername.getPassword();
    }

}
