package com.atguigu.spzx.user.controller;

import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.vo.common.Result;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.user.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户地址接口")
@RestController
@RequestMapping(value="/api/user/userAddress")
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;

    @Operation(summary = "获取地址信息")
    @GetMapping("getUserAddress/{id}")
    public UserAddress getUserAddress(@PathVariable Long id) {
        return userAddressService.getById(id);
    }

    //添加
    @PostMapping(value = "/auth/save`")
    public Result saveUserAddress(@RequestBody UserAddress userAddress) {
        userAddressService.saveUserAddress(userAddress) ;
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
    //删除
@DeleteMapping(value = "/auth/removeById/{userId}")
public Result deleteById(@PathVariable(value = "userId") Long userId) {
    userAddressService.deleteById(userId) ;
    return Result.build(null , ResultCodeEnum.SUCCESS) ;
}
    //修改
    @PutMapping("/auth/updateById")
    public Result updateById(@RequestBody UserAddress userAddress ) {
        userAddressService.updateById(userAddress);
        return Result.build(null , ResultCodeEnum.SUCCESS) ;
    }
    @Operation(summary = "获取用户地址列表")
    @GetMapping("/auth/findUserAddressList")
    public Result<List<UserAddress>> findUserAddressList() {
        List<UserAddress> list = userAddressService.findUserAddressList();
        return Result.build(list , ResultCodeEnum.SUCCESS) ;
    }
}
