DROP TABLE IF EXISTS USER;
CREATE TABLE `USER` (
  `id` int(10) AUTO_INCREMENT comment 'id',
  `user_name` VARCHAR (60) comment '用户名',
  `password` VARCHAR(60) comment '密码'
);
