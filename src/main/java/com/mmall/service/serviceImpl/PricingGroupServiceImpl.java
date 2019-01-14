package com.mmall.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mmall.dao.*;
import com.mmall.excel.thread.ImportPrice;
import com.mmall.model.*;
import com.mmall.model.Response.InfoEnums;
import com.mmall.model.Response.Result;
import com.mmall.model.params.PricingGroupParam;
import com.mmall.service.PricingGroupService;
import com.mmall.util.LevelUtil;
import com.mmall.vo.PGVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    public Result saveExistingPricingGroups(Integer userId, Integer selfId,Integer type) {
        pricingGroupMapper.delete(new QueryWrapper<PricingGroup>().eq("user_id",selfId));
        List<PricingGroup> pricingGroupList = pricingGroupMapper.selectList(new QueryWrapper<PricingGroup>().eq("user_id", userId));
        for(PricingGroup pg:pricingGroupList){
            pg.setUserId(selfId);
            pricingGroupMapper.insert(pg);
        }
        if(type==1){
            List<SpecialPricingGroupKey> specialPricingGroupKeyList = specialPricingGroupKeyMapper.selectList(new QueryWrapper<SpecialPricingGroupKey>().eq("user_id", userId));

            for(SpecialPricingGroupKey sp :specialPricingGroupKeyList){
                SpecialPricingGroupKey gk = new SpecialPricingGroupKey();
                gk.setStatus(sp.getStatus());
                gk.setKeyName(sp.getKeyName());
                gk.setUserId(selfId);
                specialPricingGroupKeyMapper.insert(gk);
                List<SpecialPricingGroup> pricingGroups = specialPricingGroupMapper.selectList(new QueryWrapper<SpecialPricingGroup>()
                        .eq("key_id",sp.getId()));
                for(SpecialPricingGroup spg : pricingGroups){
                    spg.setId(null);
                    spg.setKeyId(gk.getId());
                    specialPricingGroupMapper.insert(spg);
                }
            }
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
    @Transactional
    public Result importPrice(MultipartFile file, Integer userId) throws IOException, InterruptedException{

        //初始化数据
        ArrayListMultimap<Integer, PGVo> map = ArrayListMultimap.create();
        Map<String, List> map1 = getList(file, userId);
        List<PGVo> list = map1.get("list");
        List<String> listError = map1.get("listError");
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        //处理读取的数据并判断空值
        for(PGVo pg:list){

            //根据省份分离数据
            map.put(pg.getCityId(),pg);

            //判断首重区间是否为0
            if(pg.getFirstOrContinued()==1 && pg.getAreaBegin()==0 && pg.getAreaEnd()==0){
                continue;
            }

            //判断并处理空值
            if(pg.getFirstOrContinued()==1){
                if(pg.getAreaBegin()==Double.parseDouble("0")||pg.getAreaEnd()==Double.parseDouble("0")){
                    if(pg.isNull()){
                        listError.add(pg.getRow()+"行：区间数据为空，首重或者续重的区间必须成对出现");
                    }
                }
            }

            else if(pg.getFirstOrContinued()==2){
                if(pg.getFirstWeight()==Double.parseDouble("0")){
                    if(pg.isNull()) {
                        listError.add(pg.getRow() + "行：起算首重重量不能为空");
                    }
                }

                if(pg.getFirstWeightPrice()==Double.parseDouble("0")){
                    if(pg.isNull()) {
                        listError.add(pg.getRow() + "行：起算首重金额不能为空");
                    }
                }

                if(pg.getWeightStandard()==Double.parseDouble("0")){
                    if(pg.isNull()) {
                        listError.add(pg.getRow() + "行：重量进制不能为空");
                    }
                }
            }

            if(pg.getPrice()==Double.parseDouble("0")){
                if(pg.isNull()) {
                    listError.add(pg.getRow() + "行：金额不能为空");
                }
            }

            //判断区间末尾是否大于开始
            if(pg.getAreaEnd()<=pg.getAreaBegin()){
                City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pg.getCityId()));
                listError.add(city.getProvinceName()+":定价组区间["+pg.getAreaBegin()+","+pg.getAreaEnd()+"]区间的末尾必须大于开始");

            }
        }

        //返回表格空值
        if(listError.size()!=0){
            return Result.error(InfoEnums.TABLE_FORMAT_ERROR,listError);
        }


        //判断表格无法识别的地址和首重续重重复
        for (Integer key:map.keySet()) {

            //删除数据库原有的数据
            List<PricingGroup> pricingGroupss = totalMapper.selectList(new QueryWrapper<PricingGroup>().eq("user_id",userId).eq("city_id", key));
            if(pricingGroupss.size()!=0){
                totalMapper.delete(new UpdateWrapper<PricingGroup>().eq("user_id", userId).eq("city_id", key));
            }

            //获取当前省份数据
            List<PGVo> pricingGroups = map.get(key);

            List<PGVo> pricingGroupsOne=new ArrayList<>();

            List<PGVo> pricingGroupsTwo=new ArrayList<>();

            for(PGVo pv:pricingGroups){
                if(pv.getFirstOrContinued()==1){
                    pricingGroupsOne.add(pv);
                    continue;
                }

                if(pv.getFirstOrContinued()==2){
                    pricingGroupsTwo.add(pv);
                }
            }

            //获取读取不到的省份数据
            if(key==0){
                for(PGVo pg:pricingGroups){
                    if(pg.getRow()!=null && pg.isRowNo()){
                        listError.add("表格第"+pg.getRow()+"行地址无法识别（检查是否有错字，或者本账单已存在该省份定价）");
                    }
                }
                continue;
            }

            Collections.sort(pricingGroupsTwo, new Comparator<PricingGroup>(){

                public int compare(PricingGroup o1, PricingGroup o2) {
                    if(o1.getAreaBegin() < o2.getAreaBegin()){
                        return -1;
                    }
                    if(o1.getAreaBegin() == o2.getAreaBegin()){
                        return 0;
                    }
                    return 1;
                }});

            Collections.sort(pricingGroupsOne, new Comparator<PricingGroup>(){

                public int compare(PricingGroup o3, PricingGroup o4) {
                    if(o3.getAreaBegin() > o4.getAreaBegin()){
                        return -1;
                    }
                    if(o3.getAreaBegin() == o4.getAreaBegin()){
                        return 0;
                    }
                    return 1;
                }});

            //判断最大首重值和最小续重值是否相差0.01
            BigDecimal begin=new BigDecimal(pricingGroupsTwo.get(0).getAreaBegin().toString());
            BigDecimal end =new BigDecimal(pricingGroupsOne.get(0).getAreaEnd().toString());
            boolean b = begin.subtract(end).compareTo(new BigDecimal("0.01"))==0;
            if(!b && end.compareTo(new BigDecimal("0"))!=0){
                City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", key));
                listError.add(city.getProvinceName()+"：最大首重的值和最小续重的值大小差距只能为0.01");
            }
        }

        //返回表格首重和续重重复的数据
        if(listError.size()!=0){
            return Result.error(InfoEnums.TABLE_FORMAT_ERROR,listError);
        }

        //判断连续的区间是否合法
        for (Integer key:map.keySet()) {

            //获取当前省份数据
            List<PGVo> pricingGroups = map.get(key);

            Collections.sort(pricingGroups, new Comparator<PricingGroup>(){

                public int compare(PricingGroup o1, PricingGroup o2) {
                    if(o1.getAreaBegin() < o2.getAreaBegin()){
                        return -1;
                    }
                    if(o1.getAreaBegin() == o2.getAreaBegin()){
                        return 0;
                    }
                    return 1;
                }});

            //判断定价组是否前后衔接
            for (int i = 0; i <pricingGroups.size() ; i++) {
                PricingGroup pricingGroup = pricingGroups.get(i);

                if(pricingGroup.getWeightStandard()==null || pricingGroup.getWeightStandard()==0){
                    if(pricingGroup.getFirstOrContinued()==2){
                        City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pricingGroup.getCityId()));
                        listError.add(city.getProvinceName()+":续重的重量进制不能为0");
                    }
                }

                //判断初始区间
                if(i==0){
                    if(pricingGroup.getAreaBegin()!=0){
                        City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pricingGroup.getCityId()));
                        listError.add(city.getProvinceName()+":定价组区间必须从0开始");
                    }
                }

                //判断末区间
                else if(i==pricingGroups.size()-1){
                    if(pricingGroup.getAreaEnd()!=1000000){
                        City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pricingGroup.getCityId()));
                        listError.add(city.getProvinceName()+":定价组区间必须以英文的“~”结束");
                    }

                    PricingGroup pg = pricingGroups.get(i-1);
                    checkPricingGroup(pricingGroup,pg,listError);
                }

                //判断重量区间
                else{
                    PricingGroup pg = pricingGroups.get(i-1);
                    checkPricingGroup(pricingGroup,pg,listError);
                }
            }
        }

        //返回表格异常数据
        if(listError.size()!=0){
            return Result.error(InfoEnums.TABLE_FORMAT_ERROR,listError);
        }

        //向数据库添加数据
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
     * 校验定价区间
     * @param pricingGroup
     * @param pg
     * @param listError
     */
    public void checkPricingGroup(PricingGroup pricingGroup,PricingGroup pg,List<String> listError){
        BigDecimal AreaBegin=new BigDecimal(pricingGroup.getAreaBegin().toString());
        BigDecimal getAreaEnd=new BigDecimal(pg.getAreaEnd().toString());
        BigDecimal subtract = AreaBegin.subtract(getAreaEnd);

        String str=(pricingGroup.getAreaEnd()==Double.parseDouble("1000000"))?"~":pricingGroup.getAreaEnd().toString();

        if( AreaBegin.compareTo(getAreaEnd)<=0){
            City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pricingGroup.getCityId()));
            listError.add(city.getProvinceName()+":定价组区间["+pg.getAreaBegin()+","+pg.getAreaEnd()+"]和["+pricingGroup.getAreaBegin()+","+str+"]之间内容重复");
        }else{
            if(subtract.compareTo(new BigDecimal("0.01"))<0){
                City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pricingGroup.getCityId()));
                listError.add(city.getProvinceName()+":定价组区间["+pg.getAreaBegin()+","+pg.getAreaEnd()+"]和["+pricingGroup.getAreaBegin()+","+str+"]之间精度最低为0.01");
            }
            else if(subtract.compareTo(new BigDecimal("0.01"))>0){
                City city = cityMapper.selectOne(new QueryWrapper<City>().eq("id", pricingGroup.getCityId()));
                listError.add(city.getProvinceName()+":定价组区间["+pg.getAreaBegin()+","+pg.getAreaEnd()+"]和["+pricingGroup.getAreaBegin()+","+str+"]之间无定价");
            }
        }
    }

    /**
     * 获取定价表数据
     * @param file
     * @param userId
     * @return
     * @throws IOException
     */
    public Map<String,List> getList(MultipartFile file, Integer userId) throws IOException {
        InputStream inputStream = file.getInputStream();
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);

        //初始化数据
        List<String> listError = new ArrayList<String>();
        List<PGVo> list = new ArrayList<PGVo>();
        Map<String,List> map=new HashMap<>();
        Integer CityId=0;
        Integer CityNum=0;

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
                try {
                    String check = check1(xssfRow.getCell(0));
                    String check3 = check1(xssfRow.getCell(4));
                    String check44 = check1(xssfRow.getCell(1));
                    if ("".equals(check) && "".equals(check3) && "".equals(check44)) {
                        break;
                    }

                    if (!"".equals(check) && check != null) {
                        Integer CityIdTwo = CityId;
                        for (City c : cities) {
                            if (check.startsWith(c.getProvinceName())) {
                                CityId = c.getId();
                                break;
                            }
                        }
                        if (CityIdTwo == CityId) {
                            CityId = 0;
                        }
                        CityNum = 0;
                    }

                    //添加首重
                    if (!"".equals(check44)) {
                        PGVo pg = new PGVo();
                        if (CityId == 0 && CityNum == 0) {
                            CityNum++;
                            pg.setRowNo(true);
                        } else {
                            pg.setRowNo(false);
                        }
                        pg.setRow(rowNum + 1);
                        pg.setCityId(CityId);
                        pg.setUserId(userId);
                        pg.setAreaBegin(Double.parseDouble(check(xssfRow.getCell(1), pg)));
                        pg.setAreaEnd(Double.parseDouble(check(xssfRow.getCell(2), pg)));
                        pg.setPrice(Double.parseDouble(check(xssfRow.getCell(3), pg)));
                        pg.setFirstOrContinued(1);
                        list.add(pg);
                    }

                    //添加续重
                    String check1 = check1(xssfRow.getCell(4));
                    if (!"".equals(check1)) {
                        PGVo pg1 = new PGVo();
                        pg1.setCityId(CityId);
                        pg1.setUserId(userId);
                        pg1.setAreaBegin(Double.parseDouble(check(xssfRow.getCell(4), pg1)));

                        //判断是否是“~”
                        String check2 = check(xssfRow.getCell(5), pg1);
                        if ("~".equals(check2)) {
                            pg1.setAreaEnd(Double.parseDouble("1000000"));
                        } else {
                            pg1.setAreaEnd(Double.parseDouble(check2));
                        }

                        pg1.setFirstWeight(Double.parseDouble(check(xssfRow.getCell(6), pg1)));
                        pg1.setFirstWeightPrice(Double.parseDouble(check(xssfRow.getCell(7), pg1)));
                        pg1.setWeightStandard(Double.parseDouble(check(xssfRow.getCell(8), pg1)));
                        pg1.setPrice(Double.parseDouble(check(xssfRow.getCell(9), pg1)));
                        pg1.setFirstOrContinued(2);
                        list.add(pg1);
                    }
                }catch (Exception e){
                    listError.add("第"+(rowNum+1)+"行：有无法识别的数据，请检查是否存在特殊字符或者汉字");
                }
            }
        }
        map.put("list",list);
        map.put("listError",listError);
        return map;
    }

    /**
     * 校验数据，去除空格
     * @return
     */
    public String check(XSSFCell xssfCell,PGVo pg){
        xssfCell.setCellType(CellType.STRING);
        String s="";
        if(xssfCell!=null){
            s = xssfCell.toString().replaceAll("\u00A0", "");
        }

        if("".equals(s)){
            s="0";
            pg.setRow(xssfCell.getRowIndex()+1);
            pg.setNull(true);
        }
        return s;
    }

    public String check1(XSSFCell xssfCell){
        xssfCell.setCellType(CellType.STRING);
        String s="";
        if(xssfCell==null){
            s="";
        }else{
            s = xssfCell.toString().replaceAll("\u00A0", "");
        }
        return s;
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
