package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.component.ApplicationContextHelper;
import com.mmall.constants.LevelConstants;
import com.mmall.dao.*;
import com.mmall.dto.SysMenuDto;
import com.mmall.excel.thread.ImportPrice;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.model.params.UserInfoExpressParm;
import com.mmall.service.PricingGroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mmall.util.LevelUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hyc
 * @since 2018-09-28
 */
@Service
@Slf4j
public class PricingGroupServiceImpl extends ServiceImpl<PricingGroupMapper, PricingGroup> implements PricingGroupService {
    @Resource
    private PricingGroupMapper pricingGroupMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;
    @Autowired
    private SpecialPricingGroupMapper specialPricingGroupMapper;
    @Autowired
    private SpecialPricingGroupKeyMapper specialPricingGroupKeyMapper;

    @Autowired
    private PricingGroupMapper totalMapper;

    public Result<Map<String, List<PricingGroup>>> getPricingGroup(Integer userId, Integer cityId) {
        List<PricingGroup> pricingGroups = pricingGroupMapper.selectList(new QueryWrapper<PricingGroup>()
                .eq("user_id", userId)
                .eq("city_id", cityId));
        Multimap<String, PricingGroup> weightMap = ArrayListMultimap.create();
        String firstWeight = "firstWeight";//首重
        String continuedWeight = "continuedWeight";//续重
        for(PricingGroup pg:pricingGroups){
            weightMap.put(pg.getFirstOrContinued()==1?firstWeight:continuedWeight,pg);
        }
        Map<String,List<PricingGroup>> map = Maps.newHashMap();
        //首重集合
        List<PricingGroup> firstWeightList = (List<PricingGroup>) weightMap.get(firstWeight);
        //续重集合
        List<PricingGroup> continuedWeightList = (List<PricingGroup>) weightMap.get(continuedWeight);

        map.put(firstWeight,firstWeightList);
        map.put(continuedWeight,continuedWeightList);
        return Result.ok(map);
    }
    @Transactional
    public Result savePricingGroup(List<PricingGroupParam> pricingGroups,Integer userId,Integer cityId) {
        boolean firstWeight = true;
        boolean continuedWeight = true;
        for(PricingGroupParam pg:pricingGroups){
            if(pg.getFirstOrContinued()==1)firstWeight=false;
            if(pg.getFirstOrContinued()==2)continuedWeight=false;
        }
        if(firstWeight||continuedWeight) return Result.error(InfoEnums.WEIGHT_NOT_WRITE);


        pricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",userId).eq("city_id",cityId));
        List<PricingGroup> pricingGroupList = Lists.newArrayList();
        for(PricingGroupParam prp : pricingGroups){
            PricingGroup pg = PricingGroup.builder()
                    .userId(userId)
                    .cityId(cityId)
                    .areaBegin(prp.getAreaBegin())
                    .areaEnd(prp.getAreaEnd())
                    .weightStandard(prp.getWeightStandard())
                    .price(prp.getPrice())
                    .firstOrContinued(prp.getFirstOrContinued())
                    .firstWeightPrice(prp.getFirstWeightPrice())
                    .firstWeight(prp.getFirstWeight())
                    .build();
            pricingGroupList.add(pg);
            pricingGroupMapper.insert(pg);
        }
        //pricingGroupMapper.insertPricingGroupList(pricingGroupList);
        return Result.ok();
    }

    public Result getAllPricingGroups(Integer userId) {
        List<Integer> allCityId = pricingGroupMapper.getAllPricingGroups(userId);
        if(allCityId.isEmpty()){
            return Result.ok(allCityId);
        }
        String ids = "";
        for(Integer i : allCityId){
            ids += ","+i;
        }
        List<City> cityIds = cityMapper.getAllById(ids);
        return Result.ok(cityIds);
    }

    @Override
    public Result getAllCustomers(SysUserInfo userInfo) {
        String level = LevelUtil.calculateLevel(userInfo.getLevel(), userInfo.getId());
        List<SysUserInfo> allLikeLevel = sysUserInfoMapper.findAllLikeLevel(level+"%");
        return Result.ok(allLikeLevel);
    }

    @Override
    @Transactional
    public Result saveExistingPricingGroups(Integer userId, Integer selfId) {
        pricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",selfId));

        List<PricingGroup> pricingGroupList = pricingGroupMapper.selectList(new QueryWrapper<PricingGroup>().eq("user_id", userId));
        for(PricingGroup pg:pricingGroupList){
            pg.setUserId(selfId);
            pricingGroupMapper.insert(pg);
        }
        return Result.ok();
    }

    @Override
    public Result<Map<String, List<SpecialPricingGroup>>> getSpecialPricingGroupByKey( Integer specialId) {
        List<SpecialPricingGroup> pricingGroups = specialPricingGroupMapper.selectList(new QueryWrapper<SpecialPricingGroup>()
                .eq("key_id", specialId));
        Multimap<String, SpecialPricingGroup> weightMap = ArrayListMultimap.create();
        String firstWeight = "firstWeight";//首重
        String continuedWeight = "continuedWeight";//续重
        for(SpecialPricingGroup pg:pricingGroups){
            weightMap.put(pg.getFirstOrContinued()==1?firstWeight:continuedWeight,pg);
        }
        Map<String,List<SpecialPricingGroup>> map = Maps.newHashMap();
        //首重集合
        List<SpecialPricingGroup> firstWeightList = (List<SpecialPricingGroup>) weightMap.get(firstWeight);
        //续重集合
        List<SpecialPricingGroup> continuedWeightList = (List<SpecialPricingGroup>) weightMap.get(continuedWeight);

        map.put(firstWeight,firstWeightList);
        map.put(continuedWeight,continuedWeightList);
        return Result.ok(map);
    }

    @Override
    public Result saveSpecialPricingGroup(List<PricingGroupParam> pricingGroups, Integer userId,Integer keyId) {
        if(pricingGroups.isEmpty()){
            return Result.error(InfoEnums.PLEASE_ADD_PRICING);
        }
        boolean firstWeight = true;
        boolean continuedWeight = true;
        for(PricingGroupParam pg:pricingGroups){
            if(pg.getFirstOrContinued()==1)firstWeight=false;
            if(pg.getFirstOrContinued()==2)continuedWeight=false;
        }

        if(firstWeight||continuedWeight) return Result.error(InfoEnums.WEIGHT_NOT_WRITE);
        //specialPricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",userId).eq("city_id",cityId));
        List<SpecialPricingGroup> pricingGroupList = Lists.newArrayList();
        for(PricingGroupParam prp : pricingGroups){
            SpecialPricingGroup pg = SpecialPricingGroup.builder()
                    .keyId(keyId)
                    .areaBegin(prp.getAreaBegin())
                    .areaEnd(prp.getAreaEnd())
                    .weightStandard(prp.getWeightStandard())
                    .price(prp.getPrice())
                    .firstOrContinued(prp.getFirstOrContinued())
                    .firstWeightPrice(prp.getFirstWeightPrice())
                    .firstWeight(prp.getFirstWeight())
                    .build();
            pricingGroupList.add(pg);
            specialPricingGroupMapper.insert(pg);
        }
        //pricingGroupMapper.insertPricingGroupList(pricingGroupList);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result updateSpecialPricingGroup(List<PricingGroupParam> pricingGroups, Integer keyId) {
        boolean firstWeight = true;
        boolean continuedWeight = true;
        for(PricingGroupParam pg:pricingGroups){
            if(pg.getFirstOrContinued()==1)firstWeight=false;
            if(pg.getFirstOrContinued()==2)continuedWeight=false;
        }
        if(firstWeight||continuedWeight) return Result.error(InfoEnums.WEIGHT_NOT_WRITE);
        specialPricingGroupMapper.delete(new QueryWrapper<SpecialPricingGroup>().eq("key_id",keyId));
        List<SpecialPricingGroup> pricingGroupList = Lists.newArrayList();
        for(PricingGroupParam prp : pricingGroups){
            SpecialPricingGroup pg = SpecialPricingGroup.builder()
                    .keyId(keyId)
                    .areaBegin(prp.getAreaBegin())
                    .areaEnd(prp.getAreaEnd())
                    .weightStandard(prp.getWeightStandard())
                    .price(prp.getPrice())
                    .firstOrContinued(prp.getFirstOrContinued())
                    .firstWeightPrice(prp.getFirstWeightPrice())
                    .firstWeight(prp.getFirstWeight())
                    .build();
            pricingGroupList.add(pg);
            specialPricingGroupMapper.insert(pg);
        }
        //pricingGroupMapper.insertPricingGroupList(pricingGroupList);
        return Result.ok();
    }

    @Override
    @Transactional
    public Result deleteSpecialPricingGroup(Integer keyId) {
        specialPricingGroupKeyMapper.deleteById(keyId);
        specialPricingGroupMapper.delete(new QueryWrapper<SpecialPricingGroup>().eq("key_id",keyId));
        return Result.ok();
    }

    @Override
    @Transactional
    public Result saveAllExistingPricingGroups(String pgsId, Integer cityId,Integer userId) {
        List<PricingGroup> pricingGroups = pricingGroupMapper.selectList(new QueryWrapper<PricingGroup>()
                .eq("user_id", userId)
                .eq("city_id", cityId));

        List<PricingGroup> allByUserAndCitys = pricingGroupMapper.getAllByUserAndCitys(pgsId, cityId, userId);
        for(PricingGroup pg :allByUserAndCitys){
            pricingGroupMapper.deleteById(pg);
        }
        String[] a = pgsId.split(",");
        for(String i :a){
            if(i.isEmpty()){
                continue;
            }
            for(PricingGroup pg:pricingGroups){
                pg.setCityId(Integer.parseInt(i));
                pricingGroupMapper.insert(pg);
            }
        }

        return Result.ok();
    }

    @Override
    public Result importPrice(MultipartFile file, Integer userId) throws IOException, InterruptedException, ExecutionException {
        List<PricingGroup> list = getList(file, userId);
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        for(PricingGroup pg:list){
            List<PricingGroup> pricingGroups = totalMapper.selectList(new QueryWrapper<PricingGroup>().eq("user_id", pg.getUserId()).eq("city_id", pg.getCityId()));
            if(pricingGroups.size()!=0){
                totalMapper.delete(new UpdateWrapper<PricingGroup>().eq("user_id", pg.getUserId()).eq("city_id", pg.getCityId()));
            }
        }

        for(PricingGroup pg:list){
            ImportPrice ip=new ImportPrice(pg);
            threadPool.submit(ip);
        }

        //判断线程是否执行完毕
        threadPool.shutdown();
        while (true){
            if (threadPool.isTerminated()) {
                log.info("线程已执行完毕");
                break;
            }
            Thread.sleep(500);
        }
        return Result.ok();
    }

    /**
     * 获取定价表数据
     * @param file
     * @param userId
     * @return
     * @throws IOException
     */
    public List<PricingGroup> getList(MultipartFile file, Integer userId) throws IOException {
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

        //初始化数据
        List<PricingGroup> list = new ArrayList<PricingGroup>();
        Integer CityId=0;

        //获取省份数据
        List<City> cities = cityMapper.selectList(new QueryWrapper<City>());
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }

            for (int rowNum = 4; rowNum < xssfSheet.getLastRowNum(); rowNum++) {

                XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                //获取省份
                String check = check(xssfRow.getCell(0));
                String check3 = check(xssfRow.getCell(1));
                if("".equals(check) || check==null){
                    if("".equals(check3) || check3==null){
                        break;
                    }
                }

                if(!"".equals(check) && check!=null){
                    for(City c:cities){
                        if(check.startsWith(c.getProvinceName())){
                            CityId=c.getId();
                        }
                    }
                }

                //添加首重
                PricingGroup pg=new PricingGroup();
                pg.setCityId(CityId);
                pg.setUserId(userId);
                pg.setAreaBegin(Double.parseDouble(check(xssfRow.getCell(1))));
                pg.setAreaEnd(Double.parseDouble(check(xssfRow.getCell(2))));
                pg.setPrice(Double.parseDouble(check(xssfRow.getCell(3))));
                pg.setFirstOrContinued(1);
                list.add(pg);

                //添加续重
                String check1 = check(xssfRow.getCell(4));
                if(!"".equals(check1) && check!=null){
                    PricingGroup pg1=new PricingGroup();
                    pg1.setCityId(CityId);
                    pg1.setUserId(userId);
                    pg1.setAreaBegin(Double.parseDouble(check(xssfRow.getCell(4))));

                    //判断是否是“~”
                    String check2 = check(xssfRow.getCell(5));
                    if("~".equals(check2)){
                        pg1.setAreaEnd(Double.parseDouble("1000000"));
                    }else{
                        pg1.setAreaEnd(Double.parseDouble(check2));
                    }

                    pg1.setFirstWeight(Double.parseDouble(check(xssfRow.getCell(6))));
                    pg1.setFirstWeightPrice(Double.parseDouble(check(xssfRow.getCell(7))));
                    pg1.setWeightStandard(Double.parseDouble(check(xssfRow.getCell(8))));
                    pg1.setPrice(Double.parseDouble(check(xssfRow.getCell(9))));
                    pg1.setFirstOrContinued(2);
                    list.add(pg1);
                }
            }
        }
        return list;
    }

    /**
     * 校验数据，去除空格
     * @return
     */
    public String check(XSSFCell xssfCell){
        return xssfCell.toString().replaceAll("\u00A0", "");
    }
    public static void main(String[] args) {
        String s = ",1,2,3";
        String[] a = s.split(",");
        for(String i :a){
            if(i.isEmpty()){
                continue;
            }
            System.out.println(i);
        }
    }
}
