package com.hoioy.diamond.web;

import com.hoioy.diamond.common.dto.PageDTO;
import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsDTO;
import com.hoioy.diamond.oauth2.dto.OauthClientDetailsWithRoleDTO;
import com.hoioy.diamond.oauth2.dto.RoleClientJoinDTO;
import com.hoioy.diamond.oauth2.service.IOauthClientDetailsService;
import com.hoioy.diamond.oauth2.service.IRoleClientService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.ListUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/oauth2")
public class OauthClientDetailsWebController {
    private static final Logger log = LoggerFactory.getLogger(OauthClientDetailsWebController.class);
    @Autowired
    private IOauthClientDetailsService oauthClientDetailsService;

    @Autowired
    private IRoleClientService iRoleClientService;

    private String salt = "salt";

    @Autowired
    private ObjectMapper objectMapper;

    // 跳转到应用管理列表
    @GetMapping("/appManager")
    public String goAppManager() {
        log.info("coming");
        return "oauth2/oauthClientDetails-list";
    }

    // 跳转到应用添加
    @GetMapping("/oauthClientDetailsAddPage")
    public String addClient(Model model) {
        OauthClientDetailsDTO dto = new OauthClientDetailsDTO();
        //放默认值
        dto.setAccessTokenValidity(7200);
        dto.setRefreshTokenValidity(72000);
        model.addAttribute("OauthClientDetailsDTO", dto);
        return "oauth2/oauthClientDetails-edit";
    }

    // 跳转到应用编辑
    @GetMapping("/oauthClientDetailsEdit")
    public String updateClient(Model model, @RequestParam(value = "id") String id) throws IOException {
        OauthClientDetailsDTO dto = new OauthClientDetailsDTO();
        if (!StrUtil.isEmpty(id)) {
            dto = (OauthClientDetailsDTO) this.oauthClientDetailsService.findById(id);
            //密钥设置为空字符串，强制用户修改任何信息都要修改密钥
            dto.setClientSecret("");
        }
        model.addAttribute("OauthClientDetailsDTO", dto);
        return "oauth2/oauthClientDetails-edit";
    }

    // 应用前台
    @GetMapping("/oauthClientDetailsList")
    public String index(Model model) throws IOException {
        List<OauthClientDetailsDTO> result = oauthClientDetailsService.findAll();
        model.addAttribute("result", result);
        return "index";
    }

    /**
     * 组织机构页面右侧人员列表页面
     *
     * @param model
     * @return
     */
    @GetMapping(value = "/role-client-list")
    public String roleClientList(Model model, String roleName) {
        model.addAttribute("roleName", roleName);
        return "oauth2/role-client-list";
    }

    /**
     * @Description 角色下分配client弹窗
     */
    @GetMapping(value = "/role-client-dialog")
    public String deptUserPage(String roleName, Model model) {
        StringBuilder clientIds = new StringBuilder();
        if (StrUtil.isNotEmpty(roleName)) {
            List<String> clientIdList = iRoleClientService.findFirstIdsBySecondId(roleName);
            if (!ListUtils.isEmpty(clientIdList)) {
                clientIdList.forEach(clientId -> {
                    clientIds.append(",").append(clientId);
                });
                //删除第一个逗号
                clientIds.deleteCharAt(0);
            }
        }

        model.addAttribute("roleName", roleName);
        model.addAttribute("clientIds", clientIds);
        return "oauth2/role-client-dialog";
    }

    @PostMapping("/oauthClientDetails-list")
    @ResponseBody
    public ResultDTO clientList(HttpServletRequest request, Model model,
                                @RequestParam(value = "models", required = false) String models) {
        Map searchParameters = new HashMap();
        if (models != null && models.length() > 0) {
            try {
                searchParameters = objectMapper.readValue(models, new TypeReference<Map>() {
                });
            } catch (JsonParseException e) {
                log.error("JsonParseException{}:", e.getMessage());
            } catch (JsonMappingException e) {
                log.error("JsonMappingException{}:", e.getMessage());
            } catch (IOException e) {
                log.error("IOException{}:", e.getMessage());
            }
        }

        PageDTO<OauthClientDetailsWithRoleDTO> pageDTO = searchParamToPageDTO(searchParameters);
        pageDTO = iRoleClientService.findOauthClientDetailsWithRole(pageDTO);
        return new ResultDTO(pageDTO);
    }

    /**
     * @Description 角色下的所有client
     */
    @PostMapping("/role-client-list")
    @ResponseBody
    public ResultDTO roleClientList(HttpServletRequest request,
                                    @RequestParam(value = "models", required = false) String models) {
        Map searchParameters = new HashMap();
        if (models != null && models.length() > 0) {
            try {
                searchParameters = objectMapper.readValue(models, new TypeReference<Map>() {
                });
            } catch (JsonParseException e) {
                log.error("JsonParseException{}:", e.getMessage());
            } catch (JsonMappingException e) {
                log.error("JsonMappingException{}:", e.getMessage());
            } catch (IOException e) {
                log.error("IOException{}:", e.getMessage());
            }
        }

        PageDTO<OauthClientDetailsWithRoleDTO> pageDTO = searchParamToPageDTO(searchParameters);
        Boolean isSelectAll = (Boolean) searchParameters.get("isSelectAll");
        String roleName = (String) searchParameters.get("roleName");

        if (isSelectAll) {
            pageDTO = oauthClientDetailsService.getPage(pageDTO);
            Map map = BeanUtil.beanToMap(pageDTO);
            StringBuilder clientIds = new StringBuilder();
            if (StrUtil.isNotEmpty(roleName)) {
                List<String> clientIdList = iRoleClientService.findFirstIdsBySecondId(roleName);
                if (!ListUtils.isEmpty(clientIdList)) {
                    clientIdList.forEach(clientId -> {
                        clientIds.append(",").append(clientId);
                    });
                    //删除第一个逗号
                    clientIds.deleteCharAt(0);
                }
            }

            map.put("clientIds", clientIds.toString());
            map.put("roleName", roleName);
            return new ResultDTO(map);
        }else{
            if(StrUtil.isNotBlank(roleName)){
                //如果不是查询全量，则不筛选
                pageDTO = iRoleClientService.findOauthClientDetailsWithRole(pageDTO);
            }

            return new ResultDTO(pageDTO);
        }
    }

    /**
     * @Description 分配应用
     * 老代码，不维护
     */
    @PostMapping("/role-client-save")
    @ResponseBody
    @Deprecated
    public ResultDTO saveRoleClient(@RequestParam(required = false, value = "clientIds[]") String[] clientIds,
                                    String preClientIds, String roleName) {
        ResultDTO json = new ResultDTO();

        String UIDS = StrUtil.join(",", clientIds);
        if (UIDS == null) {
            UIDS = "";
        }
        if (preClientIds == null) {
            preClientIds = "";
        }
        if (clientIds == null) {
            clientIds = new String[0];
        }
        //所有被选中的ID数组转list
        List<String> clientIdList = Arrays.asList(clientIds);
        //旧的被选中的数组转list
        List<String> preClientIdList = Arrays.asList(preClientIds.split(","));
        if (roleName != null && roleName.length() > 0) {
            // 求差集
            List<String> differenceSet = clientIdList.stream().filter(t -> !preClientIdList.contains(t)).collect(Collectors.toList());
            List<String> differenceSet2 = preClientIdList.stream().filter(t -> !clientIdList.contains(t)).collect(Collectors.toList());
            differenceSet.removeAll(differenceSet2);//此处指的是将与l2重复的删除
            differenceSet.addAll(differenceSet2);//此处指加上l2

            //遍历差集
            for (int i = 0; i < differenceSet.size(); i++) {
                String ssd = differenceSet.get(i);
                // 取差集中在旧ID中  进行删除
                if (preClientIds.contains(ssd) && preClientIds != null && !"".equals(preClientIds) && !"".equals(ssd)
                        && ssd != null) {
                    String id = differenceSet.get(i) + roleName;
                    iRoleClientService.removeById(id);
                    // 取差集中在新ID中   进行存储
                } else if (UIDS.contains(differenceSet.get(i)) && differenceSet.get(i) != null && !"".equals(differenceSet.get(i)) && UIDS != null
                        && !"".equals(clientIds)) {
                    RoleClientJoinDTO joinDTO = new RoleClientJoinDTO();
                    joinDTO.setClientId(differenceSet.get(i));
                    joinDTO.setRoleName(roleName);
                    iRoleClientService.create(joinDTO);
                }
            }
        }

        json.setCode(200);
        json.setMessage("成功分配应用！");
        json.setData(roleName);
        return json;
    }

    //为了适配不分离的方法，从老代码copy过来
    private PageDTO<OauthClientDetailsWithRoleDTO> searchParamToPageDTO(Map searchParameters) {
        int page = Integer.parseInt(searchParameters.get("page").toString());

        int pageSize = 10;
        try {
            pageSize = Integer.parseInt(searchParameters.get("pageSize").toString());
        } catch (Exception e) {
        }
        if (pageSize < 1)
            pageSize = 1;
        if (pageSize > 100)
            pageSize = 100;

        PageDTO<OauthClientDetailsWithRoleDTO> pageDTO = new PageDTO<>();
        pageDTO.setPage(page);
        pageDTO.setPageSize(pageSize);

        OauthClientDetailsWithRoleDTO oauthClientDetailsWithRoleDTO = new OauthClientDetailsWithRoleDTO();

        if (searchParameters.get("roleName") != null) {
            oauthClientDetailsWithRoleDTO.setRoleName((String) searchParameters.get("roleName"));
        }

        List<Map> filters = (List<Map>) searchParameters.get("filters");
        if(CollectionUtil.isNotEmpty(filters)){
            for (Map f : filters) {
                String field = ((String) f.get("field")).trim();
                String value = ((String) f.get("value")).trim();
                if (StrUtil.isBlank(value)) {
                    continue;
                }

                if ("clientId".equalsIgnoreCase(field)) {
                    //分页时使用过滤条件查询会查不出来将page置为第一页
//                    pageDTO.setPage(1);
                    oauthClientDetailsWithRoleDTO.setClientId(value);
                } else if ("authorizedGrantTypes".equalsIgnoreCase(field)) {
                    oauthClientDetailsWithRoleDTO.setAuthorizedGrantTypes(value);
                } else if ("custom_name".equalsIgnoreCase(field)) {
                    oauthClientDetailsWithRoleDTO.setCustomName(value);
                }
            }
        }

        pageDTO.setFilters(oauthClientDetailsWithRoleDTO);

        return pageDTO;
    }
}
