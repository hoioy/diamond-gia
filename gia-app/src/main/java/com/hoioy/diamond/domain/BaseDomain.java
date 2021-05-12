package com.hoioy.diamond.domain;

import cn.hutool.core.util.IdUtil;
import com.hoioy.diamond.common.service.CommonSecurityService;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

//@MappedSuperclass标识的类，表示其不能单独映射为独立的数据库表
@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseDomain extends BaseCommonDomain {

    @CreatedBy
    @Column(name = "created_by")
    protected String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    protected LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "modified_by")
    protected String modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date")
    protected LocalDateTime modifiedDate;

    //备注
    @Column(name = "remark")
    private String remark;

    //删除标志
    @Column(name = "flag")
    public Integer flag;

//    //乐观锁
//    @Version
//    private Integer version=0;

    @PrePersist
    public void createAuditInfo() {
        String loginName = CommonSecurityService.instance.getCurrentLoginName();
        setCreatedBy(loginName);
        setCreatedDate(LocalDateTime.now());
        this.setId(IdUtil.simpleUUID());
        this.setFlag(1);// 默认初始化
    }
}
