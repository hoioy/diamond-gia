package com.hoioy.diamond.oauth2.webapi;

import com.hoioy.diamond.common.annotation.BaseJoinId;
import com.hoioy.diamond.common.api.BaseJoinController;
import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.oauth2.dto.RoleClientJoinDTO;
import com.hoioy.diamond.oauth2.service.IRoleClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/roles")
@Api(tags = {"角色与OAuth2客户端关联关系"})
public class RoleClientController extends BaseJoinController<IRoleClientService, RoleClientJoinDTO> {
    private static final Logger logger = LoggerFactory.getLogger(RoleClientController.class);

    @Autowired
    private IRoleClientService roleClientService;

    @ApiOperation(value = "查询拥有此客户端相应权限的全部角色", notes = "查询拥有此客户端相应权限的全部角色")
    @GetMapping("/{clientId}")
    public ResultDTO<List<RoleClientJoinDTO>> findByClientId(@PathVariable(value = "clientId") String clientId) {
        List<String> ids = roleClientService.findIdsByJoinId(clientId, BaseJoinId.Index.first);
        List<RoleClientJoinDTO> dto = roleClientService.findByIds(ids);
        return new ResultDTO(dto);
    }

    @ApiOperation(value = "OAuth2客户端分配角色", notes = "OAuth2客户端分配角色")
    @PostMapping("/oauthClients")
    public ResultDTO<Boolean> roleClientSave(@RequestParam(value = "roleNames") Set<String> roleNames,
                                             @RequestParam(value = "clientIds") Set<String> clientIds) {
        logger.info("roleClientSave,roleNames={},clientIds={}", roleNames, clientIds);
        return new ResultDTO(roleClientService.batchCreate(dtosFromIds(roleNames, clientIds)));
    }

    @ApiOperation(value = "删除OAuth2客户端分配的角色", notes = "删除OAuth2客户端分配的角色")
    @DeleteMapping("/oauthClients")
    public ResultDTO<Boolean> roleClientDelete(@RequestParam(value = "roleNames") Set<String> roleNames,
                                               @RequestParam(value = "clientIds") Set<String> clientIds) {
        logger.info("roleClientSave,roleNames={},clientIds={}", roleNames, clientIds);
        return new ResultDTO( roleClientService.batchRemove(dtosFromIds(roleNames,clientIds)));
    }

    private List<RoleClientJoinDTO> dtosFromIds(Set<String> roleNames, Set<String> clientIds) {
        logger.info("dtosFromIds");
        List<RoleClientJoinDTO> roleClients = new ArrayList<>();
        if (!CollectionUtils.isEmpty(clientIds) && !CollectionUtils.isEmpty(roleNames)) {
            clientIds.forEach(clientId ->
                    roleNames.forEach(roleName -> {
                        RoleClientJoinDTO dto = new RoleClientJoinDTO();
                        dto.setClientId(clientId);
                        dto.setRoleName(roleName);
                        roleClients.add(dto);
                    }));
        }
        return roleClients;
    }
}