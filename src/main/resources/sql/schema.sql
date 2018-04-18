create table `spike_user` (
  `id` bigint(20) not null comment '用户ID,手机号码',
  `nickname` varchar(255) not null,
  `password` varchar(32) default null comment 'md5(md5(pass + 固定salt) + salt)',
  `salt` varchar(10) default null,
  `head` varchar(128) default null comment '头像,云存储id',
  `register_date` datetime default null comment '注册时间',
  `last_login_date` datetime default null comment '上次登录时间',
  `login_count` int(11) default '0' comment '登录次数',
  primary key (`id`)
)engine=InnoDB DEFAULT charset=utf8;

create table `goods` (
  `id` bigint(20) not null auto_increment comment '商品id',
  `goods_name` varchar(16) default null comment '商品名称',
  `goods_title` varchar(64) default null comment '商品标题',
  `goods_img` varchar(64) default null comment '商品图片',
  `goods_detail` longtext comment '商品的详细介绍',
  `goods_price` decimal(10, 2) default '0.00' comment '商品单价',
  `goods_stock` int(11) default '0' comment '商品库存, -1表示没有限制',
  primary key (`id`)
)engine = InnoDB auto_increment = 3 charset = utf8mb4;

create table spike_goods (
  `id` bigint(20) not null auto_increment comment '秒杀的商品表',
  `goods_id` bigint(20) default null comment '商品id',
  `spike_price` decimal(10, 2) default '0.00' comment '秒杀价',
  `stock_count` int(11) default null comment '库存数量',
  `start_date` datetime default null comment '秒杀开始时间',
  `end_date` datetime default null comment '秒杀结束时间',
  primary key (`id`)
)engine = InnoDB auto_increment = 3 default charset = utf8mb4;

create table `order_info` (
  `id` bigint(20) not null auto_increment,
  `user_id` bigint(20) default null comment '用户id',
  `goods_id` bigint(20) default null comment '商品id',
  `delivery_addr_id` bigint(20) default null comment '收货地址id',
  `goods_name` varchar(16) default null comment '冗余过来的商品名称',
  `goods_count` int(11) default '0' comment '商品数量',
  `goods_price` decimal(10, 2) default '0.00' comment '商品单价',
  `order_channel` tinyint(4) default '0' comment '1 pc 2 android 3 ios',
  `status` tinyint(4) default '0' comment '订单状态 0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime default null comment '订单创建时间',
  `pay_date` datetime default null comment '支付时间',
  primary key (`id`)
)engine = InnoDB auto_increment = 12 default charset = utf8mb4;

create table `spike_order` (
  `id` bigint(20) not null auto_increment,
  `user_id` bigint(20) default null comment '用户id',
  `order_id` bigint(20) default null comment '订单id',
  `goods_id` bigint(20) default null comment '商品id',
  primary key (`id`)
)engine = InnoDB auto_increment = 3 default charset = utf8mb4;

insert into goods values
  (1, 'iphoneX', 'Apple iPhone X(A1865)', '/img/iphonex.png', 'Apple iPhone X', 8765, 100),
  (2, '华为Mate9', '华为 Mate9 4GB+32GB', '/img/mate9.png', '华为Mate9 4GB+32GB', 3212, 10);

insert into spike_goods VALUES
  (1, 1, 0.01, 4, '2018-04-16 00:00:00', '2018-05-01 00:00:00'),
  (2, 2, 0.01, 9, '2018-04-16 00:00:00', '2018-05-01 00:00:00');

