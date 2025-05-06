package com.atguigu.spzx.pay.service;

import com.atguigu.spzx.model.entity.pay.PaymentInfo;

public interface PaymentInfoService {
    PaymentInfo savePaymentInfo(String orderNo);
}
