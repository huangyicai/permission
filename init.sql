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
