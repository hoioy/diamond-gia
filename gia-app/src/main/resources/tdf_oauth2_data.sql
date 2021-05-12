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

-- ----------------------------
--  Records of `oauth_client_details`
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES
('tdfOauth2SSO1v2', '', '$2a$10$CkHnbLTFbc20eSLX55hlp.EPzgf.yJMJZ145A/MBZgq/tkr.EalMu', 'all', 'authorization_code', 'http://localhost:8081/login', '', 7200, 72000, '{\"customSubName\":\"tdfOauth2SSO1v2\",\"icon\":\"http://tech.hoioy.com/upload/c07dc45d79084393bd62a2f899e20079.png\",\"customName\":\"TDF-oauth-client测试客户端1\"}', 'user', 'TDF-oauth-cclient测试客户端1'),
('tdfOauth2SSO2v2', '', '$2a$10$CkHnbLTFbc20eSLX55hlp.EPzgf.yJMJZ145A/MBZgq/tkr.EalMu', 'all', 'authorization_code', 'http://localhost:8082/login', '', 7200, 72000, '{\"customSubName\":\"tdfOauth2SSO2v2\",\"icon\":\"http://tech.hoioy.com/upload/c07dc45d79084393bd62a2f899e20079.png\",\"customName\":\"TDF-oauth-client测试客户端2\"}', 'user', 'TDF-oauth-cclient测试客户端2'),
('tdfOauth2SSO213', '', '$2a$10$DKTbSeyYO0De4e1wOveacemuz9s8uBBD8f.CXry50kY26kqeLbjC.', 'all', 'authorization_code', 'http://localhost:8082/login/oauth2/code/tdf', '', 7200, 72000, '{\"customSubName\":\"tdfOauth2SSO2v2\",\"icon\":\"http://tech.hoioy.com/upload/d793cbe3e60a4b199abbd1cf5f11a8fc.png\",\"customName\":\"TDF-oauth-client测试客户端2\"}', 'user', 'TDF-oauth-cclient测试客户端2'),
('dev', '', '$2a$10$y4fvvmetamjVUIVBDVzvI.hYoWnWbwaQ9LDgTo1cW5.YWp90mOdXK', 'all', 'authorization_code', 'http://localhost:7777/,http://localhost:7777/login,http://localhost:7777/**,http://10.0.50.78:7777/login,http://192.168.70.42:8080/login', '', 7200, 72000, '{"customSubName":"","icon":"","customName":"开源社区"}', '开源社区', '开源社区'),
('cloudoauth2samplejwt', '', '$2a$10$cWaq9fBJ64HFFhjAQcRDQeg4SOfvpTDrfGad3CCkqAiqxjMMx8XIO', 'all', 'authorization_code', 'http://localhost:8080/login/oauth2/code/tdf', '', 7200, 72000, '{"customSubName":"cloud-oauth-sample-jwt","icon":"http://tech.hoioy.com/upload/d793cbe3e60a4b199abbd1cf5f11a8fc.png","customName":"cloud-oauth-sample-jwt"}', 'user', 'cloudoauth2samplejwt');
COMMIT;

-- ----------------------------
--  Records of `oauth_client_details`
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` (client_id , resource_ids , client_secret , scope , authorized_grant_types , web_server_redirect_uri , authorities , access_token_validity , refresh_token_validity , additional_information , autoapprove, custom_name)  VALUES
('tdf-cloud-ui-vue', '', '$2a$10$QN4YOVP0W3kOEcnSoIU4.O70rkaJt6LZZKolMglVO6XTeZzhkktne', 'user_info', 'authorization_code,password,refresh_token',
    'http://localhost:8801/ssologin,http://192.168.70.40:8801/ssologin,http://192.168.70.44:8801/ssologin',
    '', '7200', '72000', '{\"customSubName\":\"TDF Cloud微服务大前端，基于vue技术栈\",\"icon\":\"http://tech.hoioy.com/ui/api/upload/80d22ff4d709415b8ffadf0843af8c5e_-_image.png\",\"customName\":\"TDF Cloud前端\"}', 'true','TDF Cloud前端');
COMMIT;

-- ----------------------------
--  Records of `role_client`
-- ----------------------------
BEGIN;
INSERT INTO `role_client` VALUES
('tdf-cloud-ui-vue', '5b66ecf45d634159a08468898b1b3217'),
('tdf-cloud-ui-vue', 'cc377e1b32e74e71953ddcd595d5498b'),
('tdfOauth2SSO1v2', '5b66ecf45d634159a08468898b1b3217'),
('tdfOauth2SSO2v2', '5b66ecf45d634159a08468898b1b3217'),
('dev', '5b66ecf45d634159a08468898b1b3217'),
('tdfOauth2SSO213', '5b66ecf45d634159a08468898b1b3217'),
('cloudoauth2samplejwt', '5b66ecf45d634159a08468898b1b3217'),
('tdfOauth2SSO1v2', 'cc377e1b32e74e71953ddcd595d5498b'),
('tdfOauth2SSO2v2', 'cc377e1b32e74e71953ddcd595d5498b');
COMMIT;

