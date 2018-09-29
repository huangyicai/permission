package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.constants.LevelConstants;
import com.mmall.model.Notice;
import com.mmall.dao.NoticeMapper;
import com.mmall.model.Response.Result;
import com.mmall.model.SysUserInfo;
import com.mmall.model.params.NoticeParam;
import com.mmall.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyc
 * @since 2018-09-29
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {


    @Autowired
    private NoticeMapper noticeMapper;

    public Result saveNotice(SysUserInfo user,NoticeParam notice) {
        Integer platformId = user.getPlatformId();
        Integer status = 0;
        switch (platformId){
            case 1:
                status=1;
                break;
            case 2:
                status=2;
                break;
            default:
                throw  new UnauthorizedException();
        }
        Notice notice1 = Notice.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .userId(user.getId())
                .status(status)
                .build();
        noticeMapper.insert(notice1);
        return Result.ok();
    }

    public Result<IPage<Notice>> getAllNotices(SysUserInfo user, Page page) {
        IPage<Notice> notices = noticeMapper.selectPage(page, new QueryWrapper<Notice>().eq("user_id", user.getId()));
        return Result.ok(notices);
    }

    public Result<List<Notice>> getNotices(SysUserInfo user) {
        if(user.getPlatformId()==LevelConstants.SUPER){
            throw new UnauthorizedException();
        }
        List<Notice> notices ;
        String[] split = user.getLevel().split("\\.");
        Integer userId1 = Integer.parseInt(split[1]);
        if(user.getPlatformId()== LevelConstants.EXPRESS){
            notices = noticeMapper.selectList(new QueryWrapper<Notice>().eq("user_id", userId1));
        }else {
            Integer userId2 = Integer.parseInt(split[2]);
            notices = noticeMapper.selectList(new QueryWrapper<Notice>()
                    .in("user_id", userId1,userId2));
        }

        return Result.ok(notices);
    }

}
