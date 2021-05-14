-- ----------------------------
--  Records of `user_info`
-- ----------------------------
BEGIN;
INSERT INTO `user_info` VALUES
('6613831cac9e4597abbd0138116a8f3c', null, null, '1', null, '2018-04-26 13:18:59', '', '', '2012-06-15 14:45', '', 'admin@mail.com', '', '', null, 'admin', '', '$2a$10$aZDOWYEvK06TrxN6g0Mta.X3gtnj1sHPReRic5YRcOiXl4yMctwS6', '', '', '1', '', '1', '管理员', null),
('7713831cac9e4597abbd0138116a8f3c', null, null, '1', null, '2018-04-26 13:18:59', '', '', '2012-06-15 14:45', '', 'user@mail.com', '', '', null, 'user', '', '$2a$10$aZDOWYEvK06TrxN6g0Mta.X3gtnj1sHPReRic5YRcOiXl4yMctwS6', '', '', '1', '', '1', '管理员', null);
COMMIT;

-- ----------------------------
--  Records of `other_user_info`
-- ----------------------------
BEGIN;
INSERT INTO `other_user_info` VALUES ('13694331efcb4c3590bc676e56171b8c', null, '2018-08-09 17:14:50', '0', null, '2018-08-09 17:14:50', '', '', '2012-06-15 14:45', '', '', '', '', null, 'test2', '', '123456', '', '', '1', '', '1', 'testtwo', null);
COMMIT;

-- ----------------------------
--  Records of `role`
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('5b66ecf45d634159a08468898b1b3217', null, '2018-03-02 16:12:07', '1', null, null, null, null, null, 'ROLE_ADMIN', null),
('c6d6218f0b1245358f5fcb5df1066687', null, '2018-08-10 07:32:02', '0', null, '2018-08-10 07:32:02', null, 'adf', '1', 'ROLE_NEW', null),
('cc377e1b32e74e71953ddcd595d5498b', null, '2018-03-02 16:12:07', '1', null, null, null, null, null, 'ROLE_USER', null);
COMMIT;

-- ----------------------------
--  Records of `role_user`
-- ----------------------------
BEGIN;
INSERT INTO `role_user` VALUES
 ('5b66ecf45d634159a08468898b1b3217', '6613831cac9e4597abbd0138116a8f3c'),
('cc377e1b32e74e71953ddcd595d5498b', '7713831cac9e4597abbd0138116a8f3c');
COMMIT;

-- ----------------------------
--  Records of `menu`
-- ----------------------------
SET FOREIGN_KEY_CHECKS = 0;
BEGIN;
INSERT INTO `menu` VALUES
('3a6de32d5a3c4381b8808afe836c22e9', null, '2018-03-02 16:12:28', '1', null, null, null, null, 'role', null, '1', '角色管理', 'sys/role-list', null, null, '8f13fa7aa1dd40e181568e870a6a2d03'),
('3a7fd63c81304f5c82a7681fd892fdd6', null, '2018-03-02 16:12:29', '0', null, null, null, null, 'log', null, '5', '日志管理', null, null, null, '67c1bf1eff3444b098dd190865beaf44'),
('67c1bf1eff3444b098dd190865beaf44', null, null, '1', null, '2018-05-09 15:35:43', null, null, 'sys', '', '1', '系统菜单', '/admin', '', null, null),
('73941109dd22490085de183472de9d49', null, null, '1', null, '2018-08-10 09:54:40', null, null, '', '', '1', '应用管理', 'oauth2/appManager', '', null, '760fc5e46e734250b0fcd1886c4cc02a'),
('760fc5e46e734250b0fcd1886c4cc02a', null, '2018-03-02 16:12:28', '1', null, null, null, null, 'resource', 'fa fa-laptop', '4', '资源管理', null, null, null, '67c1bf1eff3444b098dd190865beaf44'),
('8f13fa7aa1dd40e181568e870a6a2d03', null, '2018-03-02 16:12:28', '1', null, null, null, null, 'authority', 'fa fa-lock', '3', '权限', null, null, null, '67c1bf1eff3444b098dd190865beaf44'),
('968e1dd124c94922bdce2294ecd07fdb', null, '2018-03-02 16:12:26', '1', null, null, null, null, 'user', 'fa fa-user', '1', '用户管理', null, null, null, '67c1bf1eff3444b098dd190865beaf44'),
('b09b48cbfd254d0aae5250268e1b73e0', null, '2018-03-02 16:12:26', '1', null, null, null, null, 'diableuser', null, '2', '已注销人员', 'sys/disable-user-list', null, null, '968e1dd124c94922bdce2294ecd07fdb'),
('ba6255a161ef438ba36d1a675ef4480e', null, '2018-05-24 17:09:23', '0', null, '2018-05-24 17:09:23', null, null, '', '', '1', '日志列表', 'webLogsList', '', null, '3a7fd63c81304f5c82a7681fd892fdd6'),
('c91b363634b34d15819f67b319acd6c0', null, null, '1', null, '2018-09-12 16:25:49', null, null, 'role', '', '2', '应用权限管理', 'sys/role-client', '', null, '8f13fa7aa1dd40e181568e870a6a2d03'),
('e26b8a7a87f64133ba15114d05f40ade', null, null, '1', null, '2018-05-09 15:36:54', null, null, 'enableuser', '', '1', '所有人员', 'sys/user-list', '', null, '968e1dd124c94922bdce2294ecd07fdb'),
('f12d9eae0fec46e4816b6baf00ee6ef1', null, null, '1', null, '2019-03-12 10:22:33', null, null, 'menu', '', '1', '菜单管理', 'sys/menu-list', '', null, '760fc5e46e734250b0fcd1886c4cc02a');
COMMIT;
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
--  Records of `role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `role_menu` VALUES
('3a6de32d5a3c4381b8808afe836c22e9', '5b66ecf45d634159a08468898b1b3217'),
('67c1bf1eff3444b098dd190865beaf44', '5b66ecf45d634159a08468898b1b3217'),
('73941109dd22490085de183472de9d49', '5b66ecf45d634159a08468898b1b3217'),
('760fc5e46e734250b0fcd1886c4cc02a', '5b66ecf45d634159a08468898b1b3217'),
('8f13fa7aa1dd40e181568e870a6a2d03', '5b66ecf45d634159a08468898b1b3217'),
('968e1dd124c94922bdce2294ecd07fdb', '5b66ecf45d634159a08468898b1b3217'),
('b09b48cbfd254d0aae5250268e1b73e0', '5b66ecf45d634159a08468898b1b3217'),
('c91b363634b34d15819f67b319acd6c0', '5b66ecf45d634159a08468898b1b3217'),
('e26b8a7a87f64133ba15114d05f40ade', '5b66ecf45d634159a08468898b1b3217'),
('f12d9eae0fec46e4816b6baf00ee6ef1', '5b66ecf45d634159a08468898b1b3217');
COMMIT;


-- GIA Common中的表
INSERT INTO `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `custom_name`, `id`, `created_by`, `created_date`, `flag`, `modified_by`, `modified_date`, `remark`) VALUES ('diamond', '', '$2a$10$DGJqroUgrnuwBHwL4Pqbxu8bheqRfq68HygLsIDQB5dXu7GDPoW22', 'user_info', 'authorization_code', 'http://localhost:9527/ssologin,http://diamond.hoioy.com/ssologin', '', 7200, 72000, '{"customSubName":"diamond","icon":"http://www.hoioy.com/upload/2021/05/dimaondLogo100-3f4d7864c22f44559f10885d12d855a5.png","customName":"Diamond","mainHost":"http://diamond.hoioy.com"}', 'false', 'diamond', 'diamond', NULL, NULL, 1, NULL, NULL, NULL);
INSERT INTO `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`, `custom_name`, `id`, `created_by`, `created_date`, `flag`, `modified_by`, `modified_date`, `remark`) VALUES ('diamondcloud', '', '$2a$10$um6/f1CzLjYWe.mP069yresI45y.VBrictIWS61BefBzRcpm/Bvse', 'user_info', 'authorization_code', 'http://localhost:9527/ssologin,http://cloud.diamond.hoioy.com/ssologin', '', 7200, 72000, '{"customSubName":"diamondcloud","icon":"http://www.hoioy.com/upload/2021/05/dimaondLogo100-3f4d7864c22f44559f10885d12d855a5.png","customName":"Diamond Cloud","mainHost":"http://diamond.hoioy.com"}', 'false', 'diamondcloud', 'diamondcloud', NULL, NULL, 1, NULL, NULL, NULL);


INSERT INTO `role_client`(`client_id`, `id`, `role_name`) VALUES ('diamond', 'diamondROLE_ADMIN', 'ROLE_ADMIN');
INSERT INTO `role_client`(`client_id`, `id`, `role_name`) VALUES ('diamondcloud', 'diamondcloudROLE_ADMIN', 'ROLE_ADMIN');