package com.hoioy.diamond.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

//@MappedSuperclass标识的类表示其不能映射到数据库表
@MappedSuperclass
@Data
@NoArgsConstructor
class BaseCommonDomain implements Serializable {

    @Id
    @Column(name = "id")
    private String id;
}
