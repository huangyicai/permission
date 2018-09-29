package com.mmall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.model.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.NoticeParam;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hyc
 * @since 2018-09-29
 */
public interface NoticeService extends IService<Notice> {
    /**
     * 添加通知
     * @param notice
     * @return
     */
    Result saveNotice(SysUserInfo user,NoticeParam notice);

    /**
     * 获取通知列表
     * @param user
     * @param page
     * @return
     */
    Result<IPage<Notice>> getAllNotices(SysUserInfo user, Page page);

    /**
     * 获取首页通知
     * @param user
     * @return
     */
    Result<List<Notice>> getNotices(SysUserInfo user);
}
