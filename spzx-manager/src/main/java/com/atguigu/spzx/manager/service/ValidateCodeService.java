package com.atguigu.spzx.manager.service;

import com.atguigu.spzx.manager.service.impl.ValidateCodeServiceImpl;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;

public interface ValidateCodeService  {
    ValidateCodeVo generateValidateCode();
}
