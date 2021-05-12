-- 删除外键
ALTER TABLE `role_client` DROP FOREIGN KEY `FKgpdj1pt1i8eww5p0widpsy770`;
ALTER TABLE `role_client` DROP FOREIGN KEY `FKl3gw82jtg8257lsmrgryb2d6x`;
ALTER TABLE `role_client` DROP FOREIGN KEY `role_client_ibfk_1`;
ALTER TABLE `role_client` DROP FOREIGN KEY `role_client_ibfk_2`;
ALTER TABLE `role_client` DROP FOREIGN KEY `role_client_ibfk_3`;
ALTER TABLE `role_client` DROP FOREIGN KEY `role_client_ibfk_4`;


-- 增加BaseDomain中的字段
ALTER TABLE `oauth_client_details`
ADD COLUMN `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `custom_name`,
ADD COLUMN `created_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `id`,
ADD COLUMN `created_date` datetime(6) NULL DEFAULT NULL AFTER `created_by`,
ADD COLUMN `flag` int(0) NULL DEFAULT NULL AFTER `created_date`,
ADD COLUMN `modified_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `flag`,
ADD COLUMN `modified_date` datetime(6) NULL DEFAULT NULL AFTER `modified_by`,
ADD COLUMN `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `modified_date`;

-- 设置id数据
update `oauth_client_details` a set a.id=a.client_id,a.flag=1


-- 更改主键
ALTER TABLE `oauth_client_details`
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE;




-- ROlE_Client
ALTER TABLE `role_client`
ADD COLUMN `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL AFTER `role_id`,
ADD COLUMN `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `id`;

update `role_client` r set r.role_name='ROLE_ADMIN' WHERE r.role_id='5b66ecf45d634159a08468898b1b3217';
update `role_client` r set r.role_name='ROLE_USER' WHERE r.role_id='cc377e1b32e74e71953ddcd595d5498b';
update `role_client` r set r.id=CONCAT(r.client_id,r.role_name);

ALTER TABLE `role_client`
DROP PRIMARY KEY;

ALTER TABLE `role_client`
DROP INDEX `FKqyvxw2gg2qk0wld3bqfcb58vq`;

ALTER TABLE `role_client`
ADD PRIMARY KEY (`id`) USING BTREE;

ALTER TABLE `tdf_oauth2`.`role_client`
DROP COLUMN `role_id`;