package com.atguigu.spzx.user.service;

import com.atguigu.spzx.model.entity.user.UserAddress;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> findUserAddressList();

    void updateById(UserAddress userAddress);

    void deleteById(Long userId);

    void saveUserAddress(UserAddress userAddress);

    UserAddress getById(Long id);
}
