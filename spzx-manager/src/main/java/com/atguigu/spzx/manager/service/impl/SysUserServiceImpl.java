package com.atguigu.spzx.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.atguigu.spzx.common.exception.GuiguException;

import com.atguigu.spzx.manager.mapper.SysRoleUserMapper;
import com.atguigu.spzx.manager.mapper.SysUserMapper;
import com.atguigu.spzx.manager.service.SysUserService;
import com.atguigu.spzx.model.dto.system.AssginRoleDto;
import com.atguigu.spzx.model.dto.system.LoginDto;
import com.atguigu.spzx.model.dto.system.SysUserDto;
import com.atguigu.spzx.model.entity.system.SysUser;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.model.vo.system.LoginVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper ;

    @Override
    public LoginVo login(LoginDto loginDto) {
        //获取LoginDto的key和验证码
        String captcha = loginDto.getCaptcha();
        String codeKey = loginDto.getCodeKey();
        //获取redis中的key和验证码
        String redisCode = redisTemplate.opsForValue().get("user:valiCode:" + codeKey);
        //判断验证码是否正确
        if (StrUtil.isEmpty(redisCode) || !redisCode.equalsIgnoreCase(captcha)) {
            throw new GuiguException(ResultCodeEnum.VALIDATECODE_ERROR);
        }
        //判断相等则删除redis中的key
        redisTemplate.delete("user:valiCode:" + codeKey);
        // 根据用户名查询用户
        SysUser sysUser = sysUserMapper.selectByUserName(loginDto.getUserName());
        if (sysUser == null) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
            // throw new RuntimeException("用户名或者密码错误") ;
        }

        // 验证密码是否正确
        String inputPassword = loginDto.getPassword();
        String md5InputPassword = DigestUtils.md5DigestAsHex(inputPassword.getBytes());
        if (!md5InputPassword.equals(sysUser.getPassword())) {
            throw new GuiguException(ResultCodeEnum.LOGIN_ERROR);
            // throw new RuntimeException("用户名或者密码错误") ;
        }

        // 生成令牌，保存数据到Redis中
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set("user:login:" + token, JSON.toJSONString(sysUser), 7, TimeUnit.DAYS);

        // 构建响应结果对象
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);

        // 返回
        return loginVo;
    }

    @Override
    public SysUser getUserInfo(String token) {
        String userjson = redisTemplate.opsForValue().get("user:login:" + token);
        SysUser sysUser = JSON.parseObject(userjson, SysUser.class);
        return sysUser;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete("user:login:" + token);
    }

    @Override
    public PageInfo<SysUser> findByPage(SysUserDto sysUserDto, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SysUser> sysUserList = sysUserMapper.findByPage(sysUserDto);
        PageInfo pageInfo = new PageInfo(sysUserList);
        return pageInfo;
    }

    @Override
    public void saveSysUser(SysUser sysUser) {
        // 根据输入的用户名查询用户，用户不能重复
        SysUser dbSysUser = sysUserMapper.selectByUserName(sysUser.getUserName());
        if (dbSysUser != null) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

        // 对密码进行加密
        String password = sysUser.getPassword();
        String digestPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        sysUser.setPassword(digestPassword);

        //设置status的值
        sysUser.setStatus(1);

        sysUserMapper.save(sysUser);


    }

    @Override
    public void updateSysUser(SysUser sysUser) {
        sysUserMapper.update(sysUser);
    }

    @Override
    public void deleteById(Long userId) {
        sysUserMapper.delete(userId);
    }


    @Override
    public void doAssgin(AssginRoleDto assginRoleDto) {
        // 删除之前的所有的用户所对应的角色数据
        sysRoleUserMapper.deleteByUserId(assginRoleDto.getUserId());
        //分配新的数据角色
        List<Long> roleIdList = assginRoleDto.getRoleIdList();
        roleIdList.forEach(roleId -> {
            sysRoleUserMapper.doAssgin(assginRoleDto.getUserId(), roleId);
        });
    }
}
