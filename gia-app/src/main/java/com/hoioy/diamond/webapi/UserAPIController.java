package com.hoioy.diamond.webapi;


import com.hoioy.diamond.common.dto.ResultDTO;
import com.hoioy.diamond.dto.UserDTO;
import com.hoioy.diamond.service.UserService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称：UserController 类描述： 创建人：dourl 创建时间：2017年10月26日 上午10:09:55
 */
@RestController
@RequestMapping("sys")
public class UserAPIController {

    private static final Logger log = LoggerFactory.getLogger(UserAPIController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    //@ApiOperation("查询用户信息列表")
    @PostMapping("/user-list")
    public ResultDTO userList(Model model, @RequestParam("models") String models) {
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

        Map map = userService.getPage(searchParameters, "salt");
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setData(map);
        return resultDTO;
    }

    //@ApiOperation("执行用户添加")
    @PostMapping("/user-save")
    public ResultDTO userAdd(Model model, @RequestParam("models") String models,
                             @RequestParam("file") MultipartFile avatar) {
        ResultDTO result = new ResultDTO();
        result.setCode(500);
        UserDTO useDto = new UserDTO();
        if (models != null && models.length() > 0) {
            try {
                useDto = objectMapper.readValue(models, new TypeReference<UserDTO>() {
                });
            } catch (JsonParseException e) {
                log.error("JsonParseException{}:", e.getMessage());
                model.addAttribute("error", e.getMessage());
            } catch (IOException e) {
                log.error("IOException{}:", e.getMessage());
                model.addAttribute("error", e.getMessage());
            }
        }

        if (!model.containsAttribute("error")) {
            try {
                if (avatar != null && avatar.getSize() > 0) {
                    log.info("fileName{}", avatar.getBytes().length);
                    if (avatar.getBytes().length > 1024 * 1024 * 2) {
                        result.setCode(500);
                        result.setMessage("图片不能大于2M");
                        return result;
                    }
                }
                userService.save(useDto, avatar, "salt");
                result.setCode(200);
                result.setMessage("保存成功");


            } catch (Exception e) {
                log.error("Exception{}:", e.getMessage());
                model.addAttribute("error", e.getMessage());
                result.setCode(500);
                result.setMessage(e.getMessage());
            }

        }
        return result;
    }

    //@ApiOperation("执行用户删除")
    @PostMapping("/user-delete")
    public ResultDTO userDelete(@RequestParam("models") String models) {
        List<UserDTO> list = new ArrayList<UserDTO>();
        if (models != null && models.length() > 0) {
            try {
                list = objectMapper.readValue(models, new TypeReference<List<UserDTO>>() {
                });
            } catch (JsonParseException e) {
                log.error("JsonParseException{}:", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.error("IOException{}:", e.getMessage());
                e.printStackTrace();
            }
        }
        ResultDTO resultDTO = new ResultDTO();

        try {
            userService.deleteUser(list);
            resultDTO.setCode(200);
        } catch (Exception e) {
            resultDTO.setCode(500);
            resultDTO.setData(e.getMessage());
        }
        return resultDTO;
    }

    //@ApiOperation("执行用户状态修改")
    @PostMapping("/user-change-state")
    public ResultDTO changeUserState(Model model, @RequestParam("models") String models,
                                     @RequestParam("state") String tag) {
        ResultDTO result = new ResultDTO();
        List<String> list = new ArrayList<String>();
        if (models != null && models.length() > 0) {
            try {
                list = objectMapper.readValue(models, new TypeReference<List<String>>() {
                });
            } catch (JsonParseException e) {
                log.error("JsonParseException{}:", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                log.error("IOException{}:", e.getMessage());
                e.printStackTrace();
            }
        }

        try {
            this.userService.changeUserState(list, tag);
            result.setCode(200);
            String msg = "操作成功";
            result.setMessage(msg);
        } catch (Exception e) {
            result.setCode(500);
        }
        return result;
    }

    //@ApiOperation("执行用户密码设置")
    @PostMapping("/user-pwd-save")
    public ResultDTO userPwdSave(String id, String password) {
        ResultDTO result = new ResultDTO();
        try {
            this.userService.savePwd(id, password);
            result.setCode(200);
            result.setMessage("成功修改密码！");
        } catch (Exception e) {
            result.setCode(500);
        }
        return result;
    }

    /**
     * @param model
     * @param models
     * @return String
     * @throws
     * @Description:加载所有用户
     * @author wanghw
     * @date 2018年2月5日
     */
    @PostMapping("/role-dept-user-list")
    public ResultDTO deptUserAllList(Model model, @RequestParam(value = "models", required = false) String models) {

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
        Map map = userService.getPage(searchParameters, "salt");
        ResultDTO result = new ResultDTO();
        result.setData(map);
        return result;
    }
}