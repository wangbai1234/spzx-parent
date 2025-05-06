package com.atguigu.spzx.user.service.impl;

import com.atguigu.spzx.common.exception.GuiguException;
import com.atguigu.spzx.model.entity.user.UserAddress;
import com.atguigu.spzx.model.vo.common.ResultCodeEnum;
import com.atguigu.spzx.user.mapper.UserAddressMapper;
import com.atguigu.spzx.user.service.UserAddressService;
import com.atguigu.spzx.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public List<UserAddress> findUserAddressList() {
        Long userId = AuthContextUtil.getUserInfo().getId();
        return userAddressMapper.findByUserId(userId);
    }

    @Override
    public void updateById(UserAddress userAddress) {
        userAddressMapper.update(userAddress);
    }

    @Override
    public void deleteById(Long userId) {
        userAddressMapper.delete(userId);
    }



    @Override
    public void saveUserAddress(UserAddress userAddress) {
        if (CollectionUtils.isEmpty(userAddressMapper.findByUserId(userAddress.getUserId()))) {
            throw new GuiguException(ResultCodeEnum.USER_NAME_IS_EXISTS);
        }else{
            userAddressMapper.save(userAddress);
        }
    }

    @Override
    public UserAddress getById(Long id) {
        return userAddressMapper.getById(id);
    }
}
