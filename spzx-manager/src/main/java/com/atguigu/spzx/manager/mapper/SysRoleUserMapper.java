package com.atguigu.spzx.manager.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysRoleUserMapper {

    void deleteByUserId(Long userId);

    void doAssgin(Long userId, Long roleId);

    List<Long> SelectRoleIdByUserId(Long userId);
}
