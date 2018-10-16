package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.model.Total;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.vo.TotalVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * <p>
 * 月计表(客户账单) Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
public interface TotalMapper extends BaseMapper<Total> {

    Integer insertTotal(Total total);

    Total getToal(@Param("totalTime") String totalTime, @Param("userId") String userId);

    List<Total> listToal(@Param("totalTime")String totalTime, @Param("userId")String userId);

    Page<TotalVo> getBill(IPage page, @Param("totalTime") String totalTime, @Param("userId")String userId, @Param("state")Integer state);

    Total getBillCount(@Param("totalTime") String totalTime, @Param("userId")String userId,@Param("state")String state);

    /**
     * 轮询
     * @param time
     * @return
     */
    List<Total> getTotals(@Param("time") String time,@Param("sendId") Integer sendId);

    void updateByTotalId(@Param("totalId") String totalId,@Param("totalRemark")String totalRemark,@Param("date") String date);

}
