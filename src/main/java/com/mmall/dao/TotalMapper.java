package com.mmall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mmall.model.Total;
import com.mmall.model.params.TotalIncomeParam;
import com.mmall.vo.TotalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 月计表(客户账单) Mapper 接口
 * </p>
 *
 * @author qty
 * @since 2018-09-25
 */
@Mapper
public interface TotalMapper extends BaseMapper<Total> {

    Integer insertTotal(Total total);

    Total getToal(@Param("totalTime") String totalTime, @Param("userId") String userId,@Param("sendId") Integer sendId);

    List<Total> listToal(@Param("totalTime")String totalTime, @Param("userId")String userId);

    List<Total> listTotal(@Param("totalTime")String totalTime, @Param("sumId")String sumId,@Param("state")Integer state);

    void deleteTotal(@Param("totalTime")String totalTime, @Param("sumId")String sumId);

    Page<TotalVo> getBill(IPage page, @Param("totalTime") String totalTime, @Param("userId")String userId, @Param("state")Integer state,@Param("type") Integer type,@Param("sendId") Integer sendId);

    Total getBillCount(@Param("totalTime") String totalTime, @Param("userId")String userId,@Param("state")String state);

    /**
     * 轮询
     * @param time
     * @return
     */
    List<Total> getTotals(@Param("time") String time,@Param("sendId") Integer sendId,@Param("fileName")String fileName);

    void updateByTotalId(@Param("totalId") String totalId, @Param("totalRemark")String totalRemark, @Param("date") String date, @Param("totalAdditional") BigDecimal totalAdditional);

    /**
     * 查询账单详情
     * @param ipage
     * @param userId 用户id拼接的字符串
     * @param date 时间
     * @param id 发送者ID（快递公司ID）
     * @return
     */
    Page<Total> getAllBySendIdAndCreateTimeAndUserIds(Page ipage,
                                                      @Param("status")Integer status,
                                                      @Param("userId")String userId,
                                                      @Param("date")String date,
                                                      @Param("id")Integer id);

    /**
     * 获取账单应收与实收
     * @param userId
     * @param date
     * @param id
     * @return
     */
    Total getSumBiLLDetails(@Param("status")Integer status,
                            @Param("userId")String userId,
                            @Param("date")String date,
                            @Param("id")Integer id);

    List<Total> getAllBillByIds(String billIds);
}
