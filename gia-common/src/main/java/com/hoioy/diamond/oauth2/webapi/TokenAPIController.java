package com.hoioy.diamond.oauth2.webapi;

import com.hoioy.diamond.common.dto.PageDTO;
import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.common.util.CommonRedisUtil;
import com.hoioy.diamond.oauth2.config.GIAConfig;
import com.hoioy.diamond.oauth2.dto.OauthTokenDTO;
import com.hoioy.diamond.oauth2.service.IOauthTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/oauthClients")
@Api(tags = {"OAuth2的token管理"})
public class TokenAPIController {
    private static final Logger logger = LoggerFactory.getLogger(TokenAPIController.class);

    @Autowired
    public StringRedisTemplate template;

    @Autowired
    public IOauthTokenService oauthTokenService;

    @Autowired
    public CommonRedisUtil redisUtil;

//    @ApiOperation(value = "查询OAuth2的access token", notes = "查询OAuth2的access token")
//    @GetMapping("/oauthClients/accesstokens")
//    public ResultDTO accesstoken() {
//        logger.info("accesstoken");
//        template.opsForSet().size(RedisKey.ACCESS_TOKEN_REDIS + ":*");
//        List<String> tokens = redisUtil.mgetByPattern(RedisKey.ACCESS_TOKEN_REDIS + ":*");
//        return new ResultSuccessDTO("操作成功", tokens);
//    }

//    @ApiOperation(value = "批量删除access token", notes = "批量删除OAuth2 access token ")
//    @DeleteMapping(value = "oauthClients/accesstokens", produces = "application/json;charset=UTF-8")
//    public ResultDTO deleteAccessTokenBatch(@RequestParam(value = "keys") Set<String> keys) {
//        logger.info("deleteAccessTokenBatch：{}",keys);
//        if (!CollectionUtils.isEmpty(keys)) {
//            redisUtil.mremove(keys);
//        }
//        return new ResultSuccessDTO("操作成功", true);
//    }

//    @ApiOperation(value = "慎用：清理所有access token", notes = "慎用：清理所有access token")
//    @DeleteMapping(value = "oauthClients/accesstokens/clean", produces = "application/json;charset=UTF-8")
//    public ResultDTO clean() {
//        logger.info("access tokens clean");
//        redisUtil.mremoveByPattern(RedisKey.ACCESS_TOKEN_REDIS + ":*");
//        return new ResultSuccessDTO("操作成功", true);
//    }

    @ApiOperation(value = "查询当前在线用户数", notes = "查询当前在线用户数")
    @GetMapping("/onlineUserCount")
    public ResultDTO onLineUserCount() {
        logger.info("onLineUserCount");
        Long count = oauthTokenService.onLineUserCount();
        return new ResultDTO(count);
    }

    @ApiOperation(value = "分页查询Token列表", notes = "分页查询Token列表")
    @PostMapping("/accesstokens")
    @Valid
    public ResultDTO accesstoken(@RequestBody PageDTO<OauthTokenDTO> searchRestDto) throws IOException {
        searchRestDto = oauthTokenService.getPage(searchRestDto);
        return new ResultDTO(searchRestDto);
    }

    @ApiOperation(value = "批量删除access token", notes = "批量删除OAuth2 access token ")
    @DeleteMapping(value = "/accesstokens", produces = "application/json;charset=UTF-8")
    public ResultDTO deleteClientBatch(@RequestParam(value = "jtis") List<String> jtis) {
        if (!CollectionUtils.isEmpty(jtis)) {
            List<OauthTokenDTO> byIds = oauthTokenService.findAllByJtiIn(jtis);
            Set<String> redisKeys = new HashSet<>();
            byIds.forEach(oauthToken -> {
                redisKeys.add(GIAConfig.ACCESS_TOKEN_REDIS + ":" + oauthToken.getSub() + ":" + oauthToken.getAccessToken());
            });
            oauthTokenService.batchRemove(byIds);
            redisUtil.mremove(redisKeys);
        }
        return new ResultDTO(true);
    }

}
