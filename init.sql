CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `username` varchar(20) NOT NULL DEFAULT '''' COMMENT ''用户名'',
  `password` varchar(36) NOT NULL DEFAULT '''' COMMENT ''用户密码'',
  `status` int(1) NOT NULL DEFAULT ''1'' COMMENT ''该条记录是否有效1:有效、0：无效'',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''更新时间'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;



CREATE TABLE `sys_user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `user_id` int(1) NOT NULL DEFAULT ''0'' COMMENT ''用户id'',
  `platform_id` int(1) NOT NULL DEFAULT ''1'' COMMENT ''1:弗恩平台 2:快递平台 3:客户平台'',
  `name` varchar(20) NOT NULL DEFAULT '''' COMMENT ''用户名'',
  `email` varchar(36) NOT NULL DEFAULT '''' COMMENT ''用户邮箱'',
  `status` int(1) NOT NULL DEFAULT ''1'' COMMENT ''该条记录是否有效1:有效、0：无效'',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT ''更新时间'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''主键'',
  `name` varchar(20) NOT NULL DEFAULT '''' COMMENT ''名称'',
  `url` varchar(20) NOT NULL DEFAULT '''' COMMENT ''平台地址'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''角色id'',
  `role_name` varchar(64) NOT NULL DEFAULT '''' COMMENT ''角色名'',
  `status` int(11) NOT NULL DEFAULT ''1'' COMMENT ''该记录是否有效1：有效、0：无效'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT ''角色id'',
  `user_id` int(11) NOT NULL COMMENT ''用户id'',
  `seq` int(11) NOT NULL COMMENT ''顺序'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT ''角色id'',
  `menu_id` int(11) NOT NULL COMMENT ''菜单id'',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;



CREATE TABLE `menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT ''菜单id'',
  `title` varchar(64) NOT NULL DEFAULT '''' COMMENT ''标题'',
  `level` varchar(64) NOT NULL DEFAULT ''0'' COMMENT ''等级'',
  `parent_id` int (11)  DEFAULT '''' COMMENT ''上级菜单ID'',
  `url` varchar(1000) NOT NULL DEFAULT '''' COMMENT ''路径'',
  `status` int(11) NOT NULL DEFAULT ''1'' COMMENT ''该记录是否有效1：有效、0：无效''
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `bill_keyword` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `keyword` varchar(64) NOT NULL DEFAULT '' COMMENT '关键字',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '该记录是否有效1：有效、0：无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `pricing_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `city_id` int(11) NOT NULL DEFAULT '0' COMMENT '省份id',
  `area_begin` int(11) NOT NULL DEFAULT '0' COMMENT '开始区间',
  `area_end` int(11) NOT NULL DEFAULT '0' COMMENT '结束区间',
  `weight_standard` int(11) NOT NULL DEFAULT '1' COMMENT '重量标准',
  `price` int(11) NOT NULL DEFAULT '0' COMMENT '价格',
  `first_or_continued ` int(11) NOT NULL DEFAULT '1' COMMENT '1=首重，2=续重',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `city` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `province_name` varchar(64) NOT NULL DEFAULT '' COMMENT '省份名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT into city(province_name) value ('北京市');INSERT into city(province_name) value ('天津市');
INSERT into city(province_name) value ('上海市');INSERT into city(province_name) value ('重庆市');
INSERT into city(province_name) value ('河北省');INSERT into city(province_name) value ('河南省');
INSERT into city(province_name) value ('云南省');INSERT into city(province_name) value ('辽宁省');
INSERT into city(province_name) value ('黑龙江省');INSERT into city(province_name) value ('湖南省');
INSERT into city(province_name) value ('安徽省');INSERT into city(province_name) value ('山东省');
INSERT into city(province_name) value ('新疆维吾尔');INSERT into city(province_name) value ('江苏省');
INSERT into city(province_name) value ('浙江省');INSERT into city(province_name) value ('江西省');
INSERT into city(province_name) value ('湖北省');INSERT into city(province_name) value ('广西壮族');
INSERT into city(province_name) value ('甘肃省');INSERT into city(province_name) value ('山西省');
INSERT into city(province_name) value ('内蒙古');INSERT into city(province_name) value ('陕西省');
INSERT into city(province_name) value ('吉林省');INSERT into city(province_name) value ('福建省');
INSERT into city(province_name) value ('贵州省');INSERT into city(province_name) value ('广东省');
INSERT into city(province_name) value ('青海省');INSERT into city(province_name) value ('西藏');
INSERT into city(province_name) value ('四川省');INSERT into city(province_name) value ('宁夏回族');
INSERT into city(province_name) value ('海南省');INSERT into city(province_name) value ('台湾省');
INSERT into city(province_name) value ('香港特别行政区');INSERT into city(province_name) value ('澳门特别行政区');

CREATE TABLE `code_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `phone` varchar(20) NOT NULL DEFAULT '' COMMENT '电话号码',
  `code` varchar(20) NOT NULL DEFAULT '' COMMENT '验证码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `content` varchar(20) NOT NULL DEFAULT '' COMMENT '内容',
  `status` varchar(20) NOT NULL DEFAULT '1' COMMENT '状态（1=系统，2=快递公司）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
