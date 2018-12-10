/*
Navicat MySQL Data Transfer

Source Server         : funwl
Source Server Version : 50616
Source Host           : rm-bp10p278e99hn4o6oeo.mysql.rds.aliyuncs.com:3306
Source Database       : ed-shop

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2018-11-22 14:11:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bill_keyword`
-- ----------------------------
DROP TABLE IF EXISTS `bill_keyword`;
CREATE TABLE `bill_keyword` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `keyword` varchar(64) NOT NULL DEFAULT '' COMMENT '关键字',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '该记录是否有效1：有效、0：无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='省份关键字';

-- ----------------------------
-- Table structure for `city`
-- ----------------------------
DROP TABLE IF EXISTS `city`;
CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `province_name` varchar(64) NOT NULL DEFAULT '' COMMENT '省份名',
  `province_key` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COMMENT='省份';

-- ----------------------------
-- Records of city
-- ----------------------------
INSERT INTO `city` VALUES ('1', '上海', 'shanghai');
INSERT INTO `city` VALUES ('2', '浙江', 'zhejaing');
INSERT INTO `city` VALUES ('3', '江苏', 'jiangsu');
INSERT INTO `city` VALUES ('4', '安徽', 'anhui');
INSERT INTO `city` VALUES ('5', '山东', 'shandong');
INSERT INTO `city` VALUES ('6', '北京', 'beijing');
INSERT INTO `city` VALUES ('7', '天津', 'tianjing');
INSERT INTO `city` VALUES ('8', '福建', 'fujian');
INSERT INTO `city` VALUES ('9', '广东', 'guangdong');
INSERT INTO `city` VALUES ('10', '江西', 'jaingxi');
INSERT INTO `city` VALUES ('11', '河北', 'hebei');
INSERT INTO `city` VALUES ('12', '河南', 'henan');
INSERT INTO `city` VALUES ('13', '湖北', 'hubei');
INSERT INTO `city` VALUES ('14', '湖南', 'hunan');
INSERT INTO `city` VALUES ('15', '广西', 'guangxi');
INSERT INTO `city` VALUES ('16', '四川', 'sichuan');
INSERT INTO `city` VALUES ('17', '重庆', 'chongqing');
INSERT INTO `city` VALUES ('18', '贵州', 'guizhou');
INSERT INTO `city` VALUES ('19', '云南', 'yunnan');
INSERT INTO `city` VALUES ('20', '山西', 'shanxi');
INSERT INTO `city` VALUES ('21', '陕西', 'shaanxi');
INSERT INTO `city` VALUES ('22', '辽宁', 'liaoning');
INSERT INTO `city` VALUES ('23', '吉林', 'jiling');
INSERT INTO `city` VALUES ('24', '黑龙江', 'heilongjiang');
INSERT INTO `city` VALUES ('25', '甘肃', 'gansu');
INSERT INTO `city` VALUES ('26', '宁夏', 'ningxia');
INSERT INTO `city` VALUES ('27', '青海', 'qinghai');
INSERT INTO `city` VALUES ('28', '海南', 'hainan');
INSERT INTO `city` VALUES ('29', '内蒙古', 'neimenggu');
INSERT INTO `city` VALUES ('30', '新疆', 'xinjang');
INSERT INTO `city` VALUES ('31', '西藏', 'xizang');
INSERT INTO `city` VALUES ('32', '台湾', 'taiwan');
INSERT INTO `city` VALUES ('33', '香港', 'xianggang');
INSERT INTO `city` VALUES ('34', '澳门', 'aomen');

-- ----------------------------
-- Table structure for `code_dbinfo`
-- ----------------------------
DROP TABLE IF EXISTS `code_dbinfo`;
CREATE TABLE `code_dbinfo` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL COMMENT '别名',
  `db_driver` varchar(100) NOT NULL COMMENT '数据库驱动',
  `db_url` varchar(200) NOT NULL COMMENT '数据库地址',
  `db_user_name` varchar(100) NOT NULL COMMENT '数据库账户',
  `db_password` varchar(100) NOT NULL COMMENT '连接密码',
  `db_type` varchar(10) DEFAULT NULL COMMENT '数据库类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='数据库链接信息';

-- ----------------------------
-- Records of code_dbinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `code_record`
-- ----------------------------
DROP TABLE IF EXISTS `code_record`;
CREATE TABLE `code_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '电话号码',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '验证码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of code_record
-- ----------------------------

-- ----------------------------
-- Table structure for `courier_company`
-- ----------------------------
DROP TABLE IF EXISTS `courier_company`;
CREATE TABLE `courier_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `color` varchar(20) NOT NULL DEFAULT '' COMMENT '颜色',
  `background_color` varchar(20) NOT NULL DEFAULT '' COMMENT '主题背景色',
  `select_color` varchar(20) NOT NULL DEFAULT '' COMMENT '选中后的字体颜色',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '快递公司名称',
  `logo_url` varchar(200) NOT NULL DEFAULT '' COMMENT 'logo地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of courier_company
-- ----------------------------
INSERT INTO `courier_company` VALUES ('1', '#555555', '#ffffff', '#4091ff', '弗恩', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/stl8afunlogo.png');
INSERT INTO `courier_company` VALUES ('2', '#ffffff', '#939292', '#eb6700', '申通', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/pag8rshentong.png');
INSERT INTO `courier_company` VALUES ('3', '#ffffff', '#415974', '#d82316', '百世', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/2n7rbbaishi.png');
INSERT INTO `courier_company` VALUES ('4', '#ffffff', '#ecab1b', '#000000', '韵达', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/vzsgyyunda.png');
INSERT INTO `courier_company` VALUES ('5', '#555555', '#ffffff', '#4091ff', '驿淘', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/85km7yt.png');
INSERT INTO `courier_company` VALUES ('6', '#ffffff', '#74a1c8', '#1862a3', '中通', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/9h5vgzhongtong.png');
INSERT INTO `courier_company` VALUES ('7', '#ffffff', '#65537c', '#eb6700', '圆通', 'https://funwl.oss-cn-hangzhou.aliyuncs.com/images/qgbtpyuantong.png');

-- ----------------------------
-- Table structure for `customer_service`
-- ----------------------------
DROP TABLE IF EXISTS `customer_service`;
CREATE TABLE `customer_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `express_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属快递公司id',
  `handle_id` int(11) NOT NULL DEFAULT '0' COMMENT '处理人ID',
  `handle_name` varchar(20) NOT NULL DEFAULT '' COMMENT '处理人昵称',
  `waybill_number` varchar(30) NOT NULL DEFAULT '' COMMENT '运单号',
  `content` varchar(2000) NOT NULL DEFAULT '' COMMENT '问题描述',
  `contacts` varchar(20) NOT NULL DEFAULT '' COMMENT '联络人',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '联系电话',
  `enclosure` varchar(255) NOT NULL DEFAULT '' COMMENT '附件地址',
  `receive_time_solt` int(20) NOT NULL DEFAULT '0' COMMENT '接单耗时',
  `receive_time` varchar(255) NOT NULL DEFAULT '' COMMENT '接单时间',
  `end_time_solt` int(20) NOT NULL DEFAULT '0' COMMENT '完结耗时',
  `end_time` varchar(55) NOT NULL DEFAULT '' COMMENT '结束时间',
  `type_id` int(20) NOT NULL DEFAULT '3' COMMENT '类型（1=破损，2=丢失，3=其他）',
  `type_name` varchar(20) NOT NULL DEFAULT '' COMMENT '类型名称',
  `status` int(20) NOT NULL DEFAULT '1' COMMENT '状态（1=未处理，2=处理中，3=处理完毕）',
  `remarks` varchar(20) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for `daily_total`
-- ----------------------------
DROP TABLE IF EXISTS `daily_total`;
CREATE TABLE `daily_total` (
  `daily_id` int(11) NOT NULL AUTO_INCREMENT,
  `total_id` int(11) DEFAULT '0' COMMENT '账单id',
  `daily_time` varchar(20) DEFAULT NULL COMMENT '年月',
  `daily_text` varchar(255) DEFAULT '' COMMENT '每日单量的记录，逗号隔开',
  PRIMARY KEY (`daily_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='每日单量';





-- ----------------------------
-- Table structure for `fn_and_user`
-- ----------------------------
DROP TABLE IF EXISTS `fn_and_user`;
CREATE TABLE `fn_and_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT 'userID',
  `fn_id` int(11) NOT NULL DEFAULT '0' COMMENT '负责人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='弗恩客服负责人与客户关联表';


-- ----------------------------
-- Table structure for `fn_contacts`
-- ----------------------------
DROP TABLE IF EXISTS `fn_contacts`;
CREATE TABLE `fn_contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '负责人',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '电话/手机',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='弗恩快递负责人';

-- ----------------------------
-- Records of fn_contacts
-- ----------------------------
INSERT INTO `fn_contacts` VALUES ('1', '黄益财', '18069000780');

-- ----------------------------
-- Table structure for `handle_type`
-- ----------------------------
DROP TABLE IF EXISTS `handle_type`;
CREATE TABLE `handle_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type_name` varchar(20) NOT NULL DEFAULT '' COMMENT '处理类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of handle_type
-- ----------------------------
INSERT INTO `handle_type` VALUES ('1', '破损');
INSERT INTO `handle_type` VALUES ('2', '丢失');
INSERT INTO `handle_type` VALUES ('3', '短少');
INSERT INTO `handle_type` VALUES ('4', '其他');

-- ----------------------------
-- Table structure for `notice`
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `title` varchar(50) NOT NULL,
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT '内容',
  `status` int(20) NOT NULL DEFAULT '1' COMMENT '状态（1=系统，2=快递公司）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='公告';

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(20) NOT NULL COMMENT '用户id',
  `jd_waybill_number` varchar(100) DEFAULT NULL COMMENT '京东运单号',
  `fn_order_number` varchar(100) DEFAULT NULL COMMENT '弗恩订单号',
  `place_type` int(10) DEFAULT '0' COMMENT '快递类型',
  `waybill_status` int(11) DEFAULT '1' COMMENT '运单状态',
  `place_order` int(11) NOT NULL DEFAULT '0' COMMENT '是否已提交此订单（0=已提交，1=未提交）',
  `sender` varchar(100) DEFAULT NULL COMMENT '寄件人',
  `sender_phone_one` varchar(100) DEFAULT NULL COMMENT '寄件人手机',
  `sender_phone_two` varchar(100) DEFAULT NULL COMMENT '寄件人座机（手机与座机必须至少填一个）',
  `sender_pro` varchar(50) DEFAULT NULL,
  `sender_city` varchar(50) DEFAULT NULL,
  `sender_country` varchar(50) DEFAULT NULL,
  `sender_address` varchar(500) DEFAULT NULL COMMENT '寄件人地址',
  `consignee` varchar(100) DEFAULT NULL COMMENT '收件人',
  `consignee_phone_one` varchar(100) DEFAULT NULL COMMENT '收件人手机',
  `consignee_phone_two` varchar(100) DEFAULT NULL COMMENT '收件人座机（手机与座机必须至少填一个）',
  `consignee_address` varchar(500) DEFAULT NULL COMMENT '收件人地址',
  `goods_information` varchar(100) DEFAULT NULL COMMENT '物品类型',
  `temperature_layer` varchar(250) DEFAULT '普通' COMMENT '生鲜温层',
  `estimate_weight` double DEFAULT '1' COMMENT '预估重量',
  `actual_weight` double DEFAULT NULL COMMENT '实际重量（由京东快递员收取货物后确定）',
  `volume` double DEFAULT '10' COMMENT '体积',
  `package_number` int(11) DEFAULT NULL COMMENT '包裹数量',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注',
  `cancel_id` int(11) DEFAULT NULL COMMENT '取消人ID',
  `guarantee` int(11) DEFAULT NULL COMMENT '是否保价（1:是 0:否）',
  `guarantee_value` double(100,2) DEFAULT '0.00' COMMENT '保价金额',
  `collection` int(11) DEFAULT NULL COMMENT '是否代收货款（1:是 0:否）',
  `collection_value` double(100,2) DEFAULT '0.00' COMMENT '代收金额',
  `begin_place` varchar(40) DEFAULT NULL COMMENT '始发地',
  `begin_place_no` varchar(20) DEFAULT NULL COMMENT '始发地编号',
  `end_place` varchar(40) DEFAULT NULL COMMENT '目的地',
  `end_place_no` varchar(20) DEFAULT NULL COMMENT '目的地编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='物流订单表';

-- ----------------------------
-- Records of order
-- ----------------------------

-- ----------------------------
-- Table structure for `payment_method`
-- ----------------------------
DROP TABLE IF EXISTS `payment_method`;
CREATE TABLE `payment_method` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT 'userID',
  `type_name` varchar(200) NOT NULL DEFAULT '' COMMENT '付款方式',
  `payee` varchar(20) NOT NULL DEFAULT '' COMMENT '收款人',
  `payment_account` varchar(200) NOT NULL DEFAULT '' COMMENT '付款账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='付款信息';

-- ----------------------------
-- Table structure for `platform`
-- ----------------------------
DROP TABLE IF EXISTS `platform`;
CREATE TABLE `platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `platform_name` varchar(20) NOT NULL DEFAULT '' COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of platform
-- ----------------------------
INSERT INTO `platform` VALUES ('1', '弗恩平台');
INSERT INTO `platform` VALUES ('2', '快递平台');
INSERT INTO `platform` VALUES ('3', '客户平台');

-- ----------------------------
-- Table structure for `pricing_group`
-- ----------------------------
DROP TABLE IF EXISTS `pricing_group`;
CREATE TABLE `pricing_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `city_id` int(11) NOT NULL DEFAULT '0' COMMENT '省份id',
  `area_begin` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '开始区间',
  `area_end` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '结束区间',
  `weight_standard` double(11,2) NOT NULL DEFAULT '1.00' COMMENT '重量标准',
  `first_weight_price` double(11,3) NOT NULL DEFAULT '0.000',
  `first_weight` double(11,2) NOT NULL DEFAULT '0.00',
  `price` double(11,3) NOT NULL DEFAULT '0.000' COMMENT '价格',
  `first_or_continued` int(11) NOT NULL DEFAULT '1' COMMENT '1=首重，2=续重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='定价表';


-- ----------------------------
-- Table structure for `province_calculate`
-- ----------------------------
DROP TABLE IF EXISTS `province_calculate`;
CREATE TABLE `province_calculate` (
  `province_id` int(11) NOT NULL AUTO_INCREMENT,
  `total_id` int(11) DEFAULT NULL COMMENT '月计表',
  `beijing` int(11) DEFAULT '0',
  `tianjing` int(11) DEFAULT '0',
  `hebei` int(11) DEFAULT '0',
  `shanxi` int(11) DEFAULT '0',
  `neimenggu` int(11) DEFAULT '0',
  `liaoning` int(11) DEFAULT '0',
  `jiling` int(11) DEFAULT '0',
  `heilongjiang` int(11) DEFAULT '0',
  `shanghai` int(11) DEFAULT '0',
  `jiangsu` int(11) DEFAULT '0',
  `zhejaing` int(11) DEFAULT '0',
  `anhui` int(11) DEFAULT '0',
  `fujian` int(11) DEFAULT '0',
  `jaingxi` int(11) DEFAULT '0',
  `shandong` int(11) DEFAULT '0',
  `henan` int(11) DEFAULT '0',
  `hubei` int(11) DEFAULT '0',
  `hunan` int(11) DEFAULT '0',
  `guangdong` int(11) DEFAULT '0',
  `guangxi` int(11) DEFAULT '0',
  `hainan` int(11) DEFAULT '0',
  `chongqing` int(11) DEFAULT '0',
  `sichuan` int(11) DEFAULT '0',
  `guizhou` int(11) DEFAULT '0',
  `yunnan` int(11) DEFAULT '0',
  `xizang` int(11) DEFAULT '0',
  `shaanxi` int(11) DEFAULT '0',
  `gansu` int(11) DEFAULT '0',
  `qinghai` int(11) DEFAULT '0',
  `ningxia` int(11) DEFAULT '0',
  `xinjang` int(11) DEFAULT '0',
  `taiwan` int(11) DEFAULT '0',
  `xianggang` int(11) DEFAULT '0',
  `aomen` int(11) DEFAULT '0',
  PRIMARY KEY (`province_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='省计表';

-- ----------------------------
-- Table structure for `provincial_meter`
-- ----------------------------
DROP TABLE IF EXISTS `provincial_meter`;
CREATE TABLE `provincial_meter` (
  `meter_id` int(11) NOT NULL AUTO_INCREMENT,
  `total_id` int(11) DEFAULT NULL COMMENT '总账单',
  `meter_text` varchar(255) NOT NULL DEFAULT '' COMMENT '数据',
  PRIMARY KEY (`meter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='省计表（新）';

-- ----------------------------
-- Table structure for `purchase_record`
-- ----------------------------
DROP TABLE IF EXISTS `purchase_record`;
CREATE TABLE `purchase_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_no` int(30) NOT NULL DEFAULT '0' COMMENT '用户id',
  `user_id` int(30) NOT NULL DEFAULT '0' COMMENT '用户id',
  `sp_id` int(30) NOT NULL DEFAULT '0' COMMENT '价格系统ID',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0=未付款，1=已付款',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='快递企业购买记录';

-- ----------------------------
-- Records of purchase_record
-- ----------------------------

-- ----------------------------
-- Table structure for `special_pricing_group`
-- ----------------------------
DROP TABLE IF EXISTS `special_pricing_group`;
CREATE TABLE `special_pricing_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `key_id` int(11) NOT NULL DEFAULT '0' COMMENT '关键字',
  `area_begin` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '开始区间',
  `area_end` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '结束区间',
  `weight_standard` double(11,2) NOT NULL DEFAULT '1.00' COMMENT '重量标准',
  `first_weight_price` double(11,2) NOT NULL DEFAULT '1.00' COMMENT '续重的首重价格',
  `first_weight` double(11,2) NOT NULL DEFAULT '1.00' COMMENT '续重的首重',
  `price` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `first_or_continued` int(11) NOT NULL DEFAULT '1' COMMENT '1=首重，2=续重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='特殊定价表';


-- ----------------------------
-- Table structure for `special_pricing_group_key`
-- ----------------------------
DROP TABLE IF EXISTS `special_pricing_group_key`;
CREATE TABLE `special_pricing_group_key` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(30) NOT NULL DEFAULT '0' COMMENT '用户id',
  `key_name` varchar(255) NOT NULL DEFAULT '' COMMENT '特殊定价关键字',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1=取代定价，2=追加定价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COMMENT='特殊定价关键字';


-- ----------------------------
-- Table structure for `sum_tatal`
-- ----------------------------
DROP TABLE IF EXISTS `sum_tatal`;
CREATE TABLE `sum_tatal` (
  `sum_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT '0' COMMENT '用户id（老板号）',
  `sum_name` varchar(50) DEFAULT '' COMMENT '总账单名字',
  `sum_time` varchar(50) DEFAULT '' COMMENT '上传时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`sum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=942 DEFAULT CHARSET=utf8 COMMENT='总账单';


-- ----------------------------
-- Table structure for `system_price`
-- ----------------------------
DROP TABLE IF EXISTS `system_price`;
CREATE TABLE `system_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `month_num` int(11) NOT NULL DEFAULT '1' COMMENT '月数',
  `original_price` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '原价',
  `actual_name` varchar(35) NOT NULL DEFAULT '1.00' COMMENT '优惠名称',
  `actual_price` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '实际价格',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1=可用，2=不可用，3=删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='百里百里系统价格';

-- ----------------------------
-- Records of system_price
-- ----------------------------
INSERT INTO `system_price` VALUES ('1', '6', '5388.00', '公测', '4300.00', '1');
INSERT INTO `system_price` VALUES ('2', '6', '5388.00', '会员价', '4680.00', '2');
INSERT INTO `system_price` VALUES ('3', '12', '10776.00', '公测', '8000.00', '1');
INSERT INTO `system_price` VALUES ('4', '12', '10776.00', '会员价', '9600.00', '2');

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `seq` int(11) NOT NULL DEFAULT '0',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `icon` varchar(255) NOT NULL COMMENT '图标',
  `title` varchar(64) NOT NULL DEFAULT '' COMMENT '标题',
  `level` varchar(64) NOT NULL DEFAULT '0' COMMENT '等级',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级菜单ID',
  `url` varchar(1000) NOT NULL DEFAULT '' COMMENT '路径',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '该记录是否有效1：有效、0：无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('5', '1', 'el-icon-setting', '数据管理', '0', '0', '/kdData', '1');
INSERT INTO `sys_menu` VALUES ('3', '2', 'el-icon-document', '账单管理', '0', '0', '/kdBills', '1');
INSERT INTO `sys_menu` VALUES ('2', '3', 'el-icon-service', '客服/工单', '0', '0', '/kdCustomerServices', '1');
INSERT INTO `sys_menu` VALUES ('4', '4', 'el-icon-bell', '通知发布', '0', '0', '/kdNotices', '1');
INSERT INTO `sys_menu` VALUES ('1', '5', 'el-icon-news', '用户管理', '0', '0', '/kdUsers', '1');
INSERT INTO `sys_menu` VALUES ('6', '6', 'el-icon-setting', '利润分析', '0.1', '1', '/kdProfits', '1');
INSERT INTO `sys_menu` VALUES ('7', '7', 'el-icon-setting', '数据分析', '0.1', '1', '/kdData', '1');
INSERT INTO `sys_menu` VALUES ('8', '8', 'iconfont icon-lirunbiao-normal', '账单详情', '0.1', '1', '/kdBillAnalysis', '1');
INSERT INTO `sys_menu` VALUES ('0', '9', 'el-icon-news', 'fn用户管理', '0', '0', '/funUsers', '1');
INSERT INTO `sys_menu` VALUES ('1', '10', 'el-icon-setting', 'fn数据管理', '0', '0', '/funMath', '1');
INSERT INTO `sys_menu` VALUES ('5', '11', 'el-icon-service', 'fn客服/工单', '0', '0', '/funCustomers', '1');
INSERT INTO `sys_menu` VALUES ('3', '12', 'el-icon-setting', 'fn利润分析', '0.10', '10', '/funMathbillAnalysis', '1');
INSERT INTO `sys_menu` VALUES ('2', '13', 'el-icon-setting', 'fn数据分析', '0.10', '10', '/funMathDataAnalysis', '1');
INSERT INTO `sys_menu` VALUES ('14', '14', 'el-icon-service', '客服/工单', '0', '0', '/khCustomerServices', '1');
INSERT INTO `sys_menu` VALUES ('15', '15', 'el-icon-document', '账单管理', '0', '0', '/khBills', '1');
INSERT INTO `sys_menu` VALUES ('16', '16', 'el-icon-setting', 'kh运单管理', '0', '0', '/khWayBills', '1');
INSERT INTO `sys_menu` VALUES ('0', '17', 'el-icon-menu', '大数据分析', '0', '0', '/kdIndexs', '1');
INSERT INTO `sys_menu` VALUES ('0', '18', 'el-icon-menu', '充值', '0', '0', '/pay', '1');
INSERT INTO `sys_menu` VALUES ('0', '19', 'el-icon-phone-outline', '客服管理', '0', '0', '/funServisce', '1');

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `name` varchar(64) NOT NULL COMMENT '角色名',
  `role` varchar(64) NOT NULL DEFAULT '' COMMENT '角色',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '该记录是否有效1：有效、0：无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'SUPER', '1');
INSERT INTO `sys_role` VALUES ('2', '老板号', 'ADMIN', '1');
INSERT INTO `sys_role` VALUES ('3', '运营号', 'OPERATE', '1');
INSERT INTO `sys_role` VALUES ('4', '客服', 'SERVICE', '1');
INSERT INTO `sys_role` VALUES ('5', '客户', 'CUSTOMER', '1');

-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  `status` int(11) DEFAULT '1',
  `seq` int(11) NOT NULL COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1', '9', '1', '0');
INSERT INTO `sys_role_menu` VALUES ('2', '1', '10', '0', '2');
INSERT INTO `sys_role_menu` VALUES ('3', '1', '11', '0', '4');
INSERT INTO `sys_role_menu` VALUES ('4', '1', '12', '0', '3');
INSERT INTO `sys_role_menu` VALUES ('5', '1', '13', '0', '5');
INSERT INTO `sys_role_menu` VALUES ('6', '2', '1', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('7', '2', '2', '1', '4');
INSERT INTO `sys_role_menu` VALUES ('8', '2', '3', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('9', '2', '4', '1', '5');
INSERT INTO `sys_role_menu` VALUES ('10', '2', '5', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('11', '2', '6', '0', '7');
INSERT INTO `sys_role_menu` VALUES ('12', '2', '7', '0', '8');
INSERT INTO `sys_role_menu` VALUES ('13', '2', '8', '0', '9');
INSERT INTO `sys_role_menu` VALUES ('14', '3', '1', '1', '6');
INSERT INTO `sys_role_menu` VALUES ('15', '3', '2', '1', '4');
INSERT INTO `sys_role_menu` VALUES ('16', '3', '3', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('17', '3', '4', '1', '5');
INSERT INTO `sys_role_menu` VALUES ('18', '3', '5', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('19', '3', '6', '0', '7');
INSERT INTO `sys_role_menu` VALUES ('20', '3', '7', '0', '8');
INSERT INTO `sys_role_menu` VALUES ('21', '3', '8', '0', '9');
INSERT INTO `sys_role_menu` VALUES ('22', '4', '3', '1', '0');
INSERT INTO `sys_role_menu` VALUES ('23', '5', '14', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('24', '5', '15', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('25', '5', '16', '0', '4');
INSERT INTO `sys_role_menu` VALUES ('26', '3', '17', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('27', '2', '17', '1', '6');
INSERT INTO `sys_role_menu` VALUES ('28', '1', '4', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('29', '5', '17', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('30', '1', '19', '1', '6');

-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(36) NOT NULL DEFAULT '' COMMENT '密码',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '1:有效、0：无效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'root', 'b1ba853525d0f30afe59d2d005aad96c', '0', '2018-09-18 19:30:26', '2018-09-29 12:01:01');


-- ----------------------------
-- Table structure for `sys_user_info`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_info`;
CREATE TABLE `sys_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `platform_id` int(11) NOT NULL COMMENT '1:弗恩平台 2:快递平台 3:客户平台',
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `email` varchar(36) DEFAULT '' COMMENT '用户邮箱',
  `display` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '该条记录是否有效1:有效、0：无效,-1:删除',
  `company_name` varchar(50) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `person_in_charge` varchar(50) DEFAULT '' COMMENT '负责人',
  `parent_id` int(11) NOT NULL DEFAULT '0',
  `level` varchar(20) NOT NULL DEFAULT '0',
  `telephone` varchar(50) DEFAULT '',
  `courier_id` int(11) NOT NULL DEFAULT '2' COMMENT '快递公司ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户详情';

-- ----------------------------
-- Records of sys_user_info
-- ----------------------------
INSERT INTO `sys_user_info` VALUES ('1', '1', '1', '弗恩', '89282827@qq.com', '0', '1', '弗恩信息技术有限公司', '浙江省', ' 宁波市', '江东区', '宁穿路1678号盛世方舟5楼', '常先生', '0', '0', '18069000780', '1', '2018-09-19 20:30:53', '2018-10-18 10:33:29');

-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `seq` int(11) DEFAULT '1' COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1', '1');


-- ----------------------------
-- Table structure for `total`
-- ----------------------------
DROP TABLE IF EXISTS `total`;
CREATE TABLE `total` (
  `total_id` int(11) NOT NULL AUTO_INCREMENT,
  `send_id` int(11) DEFAULT NULL COMMENT '发送者id',
  `user_id` int(11) DEFAULT '0' COMMENT '用户id',
  `sum_id` int(11) NOT NULL DEFAULT '0' COMMENT '总账单id',
  `order_no` varchar(30) DEFAULT '' COMMENT '单号',
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `total_time` varchar(30) DEFAULT NULL COMMENT '时间',
  `total_number` int(11) DEFAULT '0' COMMENT '总件数',
  `total_weight` decimal(10,2) DEFAULT '0.00' COMMENT '总重量',
  `total_cost` decimal(10,2) DEFAULT '0.00' COMMENT '成本',
  `total_offer` decimal(10,2) DEFAULT '0.00' COMMENT '报价',
  `total_additional` decimal(10,2) DEFAULT '0.00' COMMENT '额外收费',
  `total_paid` decimal(10,2) DEFAULT '0.00' COMMENT '实收',
  `total_credentials_url` varchar(255) DEFAULT '' COMMENT '凭证路径，逗号隔开',
  `cd_url` varchar(255) DEFAULT '' COMMENT '生成路径',
  `total_url` varchar(255) DEFAULT NULL COMMENT '账单访问地址',
  `total_remark` varchar(255) DEFAULT '' COMMENT '备注',
  `total_state` int(10) DEFAULT '-1' COMMENT '状态：-1-未定价，1-未发送，2-待确认，3-已付款（未确认，这里不做展示），4-已收款',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `as_of_time` datetime DEFAULT NULL COMMENT '截止时间',
  `create_ip` varchar(50) DEFAULT NULL COMMENT '创建ip',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_ip` varchar(50) DEFAULT NULL COMMENT '修改ip',
  PRIMARY KEY (`total_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='月计表(账单表)';

-- ----------------------------
-- Table structure for `use_term`
-- ----------------------------
DROP TABLE IF EXISTS `use_term`;
CREATE TABLE `use_term` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(30) NOT NULL DEFAULT '0' COMMENT '用户id',
  `closing_date` varchar(35) NOT NULL DEFAULT '0' COMMENT '截止日期时间戳',
  `start_date` varchar(35) NOT NULL DEFAULT '0' COMMENT '开始日期时间戳',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;



-- ----------------------------
-- Table structure for `weight_calculate`
-- ----------------------------
DROP TABLE IF EXISTS `weight_calculate`;
CREATE TABLE `weight_calculate` (
  `weight_id` int(11) NOT NULL AUTO_INCREMENT,
  `total_id` int(11) DEFAULT NULL COMMENT '月计表',
  `zero` decimal(10,2) DEFAULT '0.00' COMMENT '其他',
  `one` decimal(10,2) DEFAULT '0.00' COMMENT '0.01-0.5',
  `two` decimal(10,2) DEFAULT '0.00' COMMENT '0.5-1',
  `three` decimal(10,2) DEFAULT '0.00' COMMENT '1-2',
  `four` decimal(10,2) DEFAULT '0.00' COMMENT '2-3',
  `five` decimal(10,2) DEFAULT '0.00' COMMENT '3-4',
  `six` decimal(10,2) DEFAULT '0.00' COMMENT '4-5',
  `seven` decimal(10,2) DEFAULT '0.00' COMMENT '5-6',
  `eight` decimal(10,2) DEFAULT '0.00' COMMENT '6-7',
  `nine` decimal(10,2) DEFAULT '0.00' COMMENT '7-8',
  `ten` decimal(10,2) DEFAULT '0.00' COMMENT '8-9',
  `eleven` decimal(10,2) DEFAULT '0.00' COMMENT '9-10',
  `twelve` decimal(10,2) DEFAULT '0.00' COMMENT '10-11',
  `thirteen` decimal(10,2) DEFAULT '0.00' COMMENT '11-12',
  `fourteen` decimal(10,2) DEFAULT '0.00' COMMENT '12-13',
  `fifteen` decimal(10,2) DEFAULT '0.00' COMMENT '13-14',
  `sixteen` decimal(10,2) DEFAULT '0.00' COMMENT '14-15',
  `seventeen` decimal(10,2) DEFAULT '0.00' COMMENT '15-16',
  `eighteen` decimal(10,2) DEFAULT '0.00' COMMENT '16-17',
  `nineteen` decimal(10,2) DEFAULT '0.00' COMMENT '17-18',
  `twenty` decimal(10,2) DEFAULT '0.00' COMMENT '18-19',
  `twenty_one` decimal(10,2) DEFAULT '0.00' COMMENT '19-20',
  PRIMARY KEY (`weight_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='重量区间计算表';

-- ----------------------------
-- Table structure for `work_reply`
-- ----------------------------
DROP TABLE IF EXISTS `work_reply`;
CREATE TABLE `work_reply` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(30) NOT NULL DEFAULT '0' COMMENT '回复的用户id',
  `service_id` int(30) NOT NULL DEFAULT '0' COMMENT '工单id',
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT '回复内容',
  `service_type` int(11) NOT NULL DEFAULT '0' COMMENT '客服是否已读',
  `status` int(30) NOT NULL DEFAULT '0' COMMENT '客户是否已读',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for `customer_user`
-- ----------------------------
DROP TABLE IF EXISTS `customer_user`;
CREATE TABLE `customer_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `customer_id` int(11) NOT NULL DEFAULT '0' COMMENT '客服id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '客户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='客服绑定客户表';

-- ----------------------------
-- Records of customer_user
-- ----------------------------

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `send_id` int(11) NOT NULL COMMENT '发送者的id',
  `title` varchar(50) NOT NULL,
  `content` varchar(1000) NOT NULL DEFAULT '' COMMENT '内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- ----------------------------
-- Table structure for `user_message`
-- ----------------------------
DROP TABLE IF EXISTS `user_message`;
CREATE TABLE `user_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `message_id` int(11) NOT NULL DEFAULT '0' COMMENT '信息id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '接收者id',
  `status` int(20) NOT NULL DEFAULT '1' COMMENT '状态：0-已读，1-未读',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户消息关联表';

-- ----------------------------
-- Records of user_message
-- ----------------------------

