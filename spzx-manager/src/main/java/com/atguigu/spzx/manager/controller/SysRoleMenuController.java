package com.atguigu.spzx.manager.controller;

import com.atguigu.spzx.manager.service.SysRoleMenuService;
import com.atguigu.spzx.model.dto.system.AssginMenuDto;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//为角色用户分配菜单
@RestController
@RequestMapping(value = "/admin/system/sysRoleMenu")
public class SysRoleMenuController {

    @Autowired
    private SysRoleMenuService sysRoleMenuService ;

    //根据角色的id查询出其对应的菜单id
    @GetMapping(value = "/findSysRoleMenuByRoleId/{roleId}")
    public Result<Map<String , Object>> findSysRoleMenuByRoleId(@PathVariable(value = "roleId") Long roleId) {
        Map<String , Object> sysRoleMenuList = sysRoleMenuService.findSysRoleMenuByRoleId(roleId) ;
        return Result.build(sysRoleMenuList , ResultCodeEnum.SUCCESS) ;
    }
    //保存角色的菜单关系
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginMenuDto assginMenuDto) {
        sysRoleMenuService.doAssign(assginMenuDto);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
}
