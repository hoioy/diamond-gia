//package com.hoioy.diamond.oauth2.domain;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.hoioy.diamond.common.base.IBaseJoinRepository;
//import com.hoioy.diamond.oauth2.dto.OauthClientDetailsWithRoleDTO;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Set;
//
//@Repository
//public interface RoleClientMapper extends IBaseJoinRepository<RoleClient> {
//
//    // 注意更新和删除是需要加事务的,并且要加上 @Modify的注解
//    @Modifying
//    @Query("delete from RoleClient k where k.clientId in (?1)")
//    void deleteByClientIds(@Param("clientIds") Set<String> clientIds);
//
//    String jpql = " from OauthClientDetails o " +
//            " left join RoleClient r on r.clientId=o.clientId " +
//            " where (:#{#oauthClientDetailsWithRoleDTO.clientId} is null or o.clientId LIKE CONCAT('%',:#{#oauthClientDetailsWithRoleDTO.clientId},'%')) " +
//            " and (:#{#oauthClientDetailsWithRoleDTO.authorizedGrantTypes} is null or o.authorizedGrantTypes LIKE CONCAT('%',:#{#oauthClientDetailsWithRoleDTO.authorizedGrantTypes},'%')) " +
//            " and (:#{#oauthClientDetailsWithRoleDTO.customName} is null or o.customName LIKE CONCAT('%',:#{#oauthClientDetailsWithRoleDTO.customName},'%')) " +
//            " and (:#{#oauthClientDetailsWithRoleDTO.roleName} is null or r.roleName LIKE CONCAT('%',:#{#oauthClientDetailsWithRoleDTO.roleName},'%')) " ;
//
//    @Query(value = "select new com.hoioy.diamond.oauth2.dto.OauthClientDetailsWithRoleDTO(" +
//            "o.id," +
//            "o.clientId," +
//            "o.clientSecret," +
//            "o.authorizedGrantTypes," +
//            "o.accessTokenValidity," +
//            "o.refreshTokenValidity," +
//            "o.webServerRedirectUri," +
//            "o.scope," +
//            "o.autoapprove," +
//            "o.resourceIds," +
//            "o.authorities," +
//            "o.customName," +
////            "r.roleName," +
//            "''," +//设置为null防止笛卡尔积
//            "o.additionalInformation" +
//            ") " + jpql,
//            countQuery = " select count(o.id) " + jpql)
//    Page<OauthClientDetailsWithRoleDTO> findOauthClientDetailsWithRole(
//            @Param("oauthClientDetailsWithRoleDTO") OauthClientDetailsWithRoleDTO oauthClientDetailsWithRoleDTO, Pageable pageable);
//
//
//}
