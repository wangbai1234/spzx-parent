package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.user.UserAddress;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAddressMapper {
    List<UserAddress> findByUserId(Long userId);

    void update(UserAddress userAddress);

    void delete(Long userId);

    void save(UserAddress userAddress);

    UserAddress getById(Long id);
}
