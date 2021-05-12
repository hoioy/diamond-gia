package com.hoioy.diamond.oauth2.webapi;

import com.hoioy.diamond.common.dto.PageDTO;
import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.common.exception.BaseException;
import com.hoioy.diamond.common.service.CommonSecurityService;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsDTO;
import com.hoioy.diamond.oauth2.exception.GIAParameterException;
import com.hoioy.diamond.oauth2.service.IOauthClientDetailsService;
import com.hoioy.diamond.oauth2.service.IRoleClientService;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@Api(tags = {"OAuth2客户端相关接口"})
public class OauthClientDetailsAPIController {
    private static final Logger log = LoggerFactory.getLogger(OauthClientDetailsAPIController.class);

    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;

    @Autowired
    private IRoleClientService roleClientService;

    @ApiOperation(value = "分页查询OAuth2客户端列表", notes = "分页查询OAuth2客户端列表")
    @PostMapping("/oauthClientList")
    @Valid
    public ResultDTO oauth2Clients(@RequestBody PageDTO<OauthClientDetailsDTO> searchRestDto) throws BaseException {
        PageDTO<OauthClientDetailsDTO> page = oauthClientDetailsService.getPage(searchRestDto);
        return new ResultDTO(page);
    }

    @ApiOperation(value = "根据ClientId查询OAuth2客户端", notes = "根据ClientId查询OAuth2客户端")
    @ApiImplicitParam(name = "clientId", value = "OAuth2客户端clientId", required = true, dataType = "string", paramType = "path")
    @GetMapping("/oauthClients/{clientId}")
    public ResultDTO queryByClientId(@PathVariable(value = "clientId") String clientId) throws IOException {
        OauthClientDetailsDTO byId = (OauthClientDetailsDTO) oauthClientDetailsService.findByClientId(clientId);
        return new ResultDTO(byId);
    }

    @ApiOperation(value = "查询某角色下所有关联的此客户端", notes = "查询某角色下所有关联的此客户端")
    @GetMapping("/oauthClientList/{roleName}")
    public ResultDTO findClientsByRoleName(@PathVariable(value = "roleName") String roleName) throws IOException {
        log.info("findClientsByRoleName,roleName={}", roleName);
        List<String> clientIds = roleClientService.findFirstIdsBySecondId(roleName);
        List<OauthClientDetailsDTO> dtos = oauthClientDetailsService.findAllByClientIdIn(clientIds);
        return new ResultDTO(dtos);
    }

    @ApiOperation(value = "添加OAuth2客户端", notes = "添加OAuth2客户端")
    @PostMapping("/oauthClients")
    public ResultDTO addClient(@RequestBody OauthClientDetailsDTO oauthClientDTO) throws IOException {
        Set<String> roleIds = CommonSecurityService.instance.getCurrentAuthorities();
        //校验是否重复
        OauthClientDetailsDTO byId = (OauthClientDetailsDTO) oauthClientDetailsService.findByClientId(oauthClientDTO.getClientId());
        if (byId != null) {
            throw new GIAParameterException("此clientId已存在");
        }
        this.oauthClientDetailsService.add(oauthClientDTO, roleIds);
        return new ResultDTO(true);
    }

    @ApiOperation(value = "更新OAuth2客户端", notes = "更新OAuth2客户端")
    @PutMapping("/oauthClients")
    public ResultDTO updateClient(@RequestBody OauthClientDetailsDTO oauthClientDTO) throws JsonProcessingException {
        return new ResultDTO(this.oauthClientDetailsService.update(oauthClientDTO));
    }

    @ApiOperation(value = "根据id删除OAuth2客户端", notes = "根据id删除OAuth2客户端 ")
    @ApiImplicitParam(name = "clientId", value = "OAuth2客户端clientId", required = true, dataType = "string", paramType = "path")
    @DeleteMapping(value = "oauthClients/{clientId}", produces = "application/json;charset=UTF-8")
    public ResultDTO deleteClient(@PathVariable(value = "clientId") String clientId) {
        if (StrUtil.isNotBlank(clientId)) {
            this.oauthClientDetailsService.removeById(clientId);
        }
        return new ResultDTO(true);
    }


    @ApiOperation(value = "批量删除OAuth2客户端", notes = "批量删除OAuth2客户端 ")
    @DeleteMapping(value = "oauthClients", produces = "application/json;charset=UTF-8")
    public ResultDTO deleteClientBatch(@RequestParam(value = "ids") List<String> ids) {
        this.oauthClientDetailsService.removeByIds(ids);
        return new ResultDTO(true);
    }


    //TODO 以下是单体中用的方法
    // 应用添加
    @PostMapping("/oauthClientDetailsAdd")
    public String addClient(HttpServletRequest request, OauthClientDetailsDTO dto) throws JsonProcessingException {
        Set<String> roleIds = CommonSecurityService.instance.getCurrentAuthorities();
        //校验是否重复
        OauthClientDetailsDTO byId = (OauthClientDetailsDTO) oauthClientDetailsService.findByClientId(dto.getClientId());
        if (byId != null) {
            throw new GIAParameterException("此clientId已存在");
        }
        this.oauthClientDetailsService.add(dto, roleIds);
        return "oauth2/oauthClientDetails-list";
    }

    // 应用添加
    @PostMapping("/oauthClientDetailsUpdate")
    public String oauthClientDetailsUpdate(OauthClientDetailsDTO dto) throws JsonProcessingException {
        this.oauthClientDetailsService.update(dto);
        return "oauth2/oauthClientDetails-list";
    }

    // 应用删除
    @PostMapping("/oauthClientDetailsDelete")
    public String deleteClient(Model model, @RequestParam(value = "id") String id) {
        if (!StrUtil.isEmpty(id)) {
            this.oauthClientDetailsService.removeById(id);
        }
        return "oauth2/oauthClientDetails-list";
    }

}
