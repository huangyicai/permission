package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.dao.TotalMapper;
import com.mmall.model.Total;
import com.mmall.service.TotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 月计表(客户账单) 服务实现类
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Service
public class TotalServiceImpl extends ServiceImpl<TotalMapper, Total> implements TotalService {

    @Autowired
    private TotalMapper totalMapper;

    /**
     * 获取客户月计统计
     * @param totalTime
     * @param userId
     * @return
     */
    public Total getToal(String totalTime, String userId) {
        return totalMapper.getToal(totalTime,userId);
    }
}
