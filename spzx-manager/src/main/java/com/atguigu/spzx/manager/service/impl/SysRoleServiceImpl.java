package com.atguigu.spzx.manager.service.impl;

import com.atguigu.spzx.manager.mapper.SysRoleMapper;
import com.atguigu.spzx.manager.mapper.SysRoleUserMapper;
import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Override
    public PageInfo<SysRole> findByPage(SysRoleDto sysRoleDto, Integer current, Integer limit) {
       //设置分页参数
        PageHelper.startPage(current, limit);
        //查询数据库中所有的数据
        List<SysRole> list =sysRoleMapper.findByPage(sysRoleDto);
        //把数据封装到PageInfo对象中
        PageInfo<SysRole> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }


    //添加用户的方法
    @Override
    public void saveSysRole(SysRole sysRole) {
        sysRoleMapper.save(sysRole);
    }

    @Override
    public void updateSysRole(SysRole sysRole) {
        sysRoleMapper.updateSysRole(sysRole) ;
    }

    @Override
    public void deleteById(Long roleId) {
        sysRoleMapper.deleteById(roleId) ;
    }

    @Override
    public Map<String, Object> findAll(Long userId) {
        //查询所有角色
        List<SysRole> roleList = sysRoleMapper.findAll() ;
        //根据用户id查询用户所有角色
        List<Long> RoleList=sysRoleUserMapper.SelectRoleIdByUserId(userId);
        Map<String , Object> map = new HashMap<>() ;
        map.put("allRolesList" , roleList) ;
        map.put("sysUserRoles" , RoleList) ;
        return map;
    }
}
