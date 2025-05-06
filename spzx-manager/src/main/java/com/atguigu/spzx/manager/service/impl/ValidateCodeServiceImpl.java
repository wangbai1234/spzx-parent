package com.atguigu.spzx.manager.service.impl;

import cn.hutool.Hutool;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.atguigu.spzx.manager.service.ValidateCodeService;
import com.atguigu.spzx.model.vo.system.ValidateCodeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ValidateCodeVo generateValidateCode() {
        //生成验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 48, 1, 6);
        String codeValue = circleCaptcha.getCode();
        String imageBase64 = circleCaptcha.getImageBase64();
        //将数据存入redis中
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set("user:valiCode:" + key, codeValue, 5, TimeUnit.MINUTES);
        //以对象返回数据
        ValidateCodeVo ValidateCodeVo = new ValidateCodeVo();
        ValidateCodeVo.setCodeKey(key);
        ValidateCodeVo.setCodeValue("data:image/png;base64," + imageBase64);


        return ValidateCodeVo;
    }
}
