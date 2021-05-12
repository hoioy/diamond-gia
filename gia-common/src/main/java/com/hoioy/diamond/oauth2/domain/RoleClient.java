package com.hoioy.diamond.oauth2.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hoioy.diamond.common.annotation.BaseJoinId;
import com.hoioy.diamond.common.base.BaseJoinDomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("role_client")
public class RoleClient extends BaseJoinDomain {

    @TableField("client_id")
    @BaseJoinId(index = BaseJoinId.Index.first)
    private String clientId;

    @TableField("role_name")
    // 之所以没有使用roleId，是因为Spring Security目前更容易支持ROLE_ADMIN这种形式
    @BaseJoinId(index = BaseJoinId.Index.second)
    private String roleName;

}