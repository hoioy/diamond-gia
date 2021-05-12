package com.hoioy.diamond.oauth2.dto;

import com.hoioy.diamond.common.annotation.BaseJoinId;
import com.hoioy.diamond.common.dto.BaseJoinDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleClientJoinDTO extends BaseJoinDTO {

    @ApiModelProperty(value = "应用ID", required = true, position = 0)
    @BaseJoinId(index = BaseJoinId.Index.first)
    private String clientId;

    @ApiModelProperty(value = "角色唯一名称", required = true, position = 1)
    @BaseJoinId(index = BaseJoinId.Index.second)
    private String roleName;
}