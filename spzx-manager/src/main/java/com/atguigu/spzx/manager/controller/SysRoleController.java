package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleService;
import com.atguigu.spzx.model.dto.system.SysRoleDto;
import com.atguigu.spzx.model.entity.system.SysRole;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;
    //查询所有ROLE对象和根据用户id查询角色id
    @GetMapping(value = "/findAllRoles/{userId}")
    public Result findAllRoles(@PathVariable(value = "userId") Long userId) {
        Map<String,Object> map =sysRoleService.findAll(userId);
        return Result.build( map, ResultCodeEnum.SUCCESS) ;
    }

    //删除用户的方法
    @DeleteMapping(value = "/deleteById/{roleId}")
    public Result deleteById(@PathVariable(value = "roleId") Long roleId) {
        sysRoleService.deleteById(roleId) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
    //修改用户的方法
    @PutMapping(value = "/updateSysRole")
    public Result updateSysRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateSysRole(sysRole) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }

    //添加用户的方法
    @PostMapping(value = "/saveSysRole")
    public Result saveSysRole(@RequestBody SysRole sysRole){
        sysRoleService.saveSysRole(sysRole);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }


    //角色的列表接口
    @PostMapping ("/findByPage/{current}/{limit}")
    public Result<PageInfo<SysRole>> findByPage(@PathVariable(value="current") Integer current,
                                      @PathVariable(value="limit") Integer limit,
                                      @RequestBody SysRoleDto sysRoleDto){
        PageInfo<SysRole> pageInfo =sysRoleService.findByPage(sysRoleDto, current, limit);
    return Result.build(pageInfo, ResultCodeEnum.SUCCESS);

    }
}
