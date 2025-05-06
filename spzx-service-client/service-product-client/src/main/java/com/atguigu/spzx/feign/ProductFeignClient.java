package com.atguigu.spzx.feign;

import com.atguigu.spzx.model.entity.product.ProductSku;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-product")
public interface ProductFeignClient {
    @GetMapping("/api/product/getBySkuId/{skuId}")
    public ProductSku getBySkuId(
            @Parameter(name = "skuId", description = "商品skuId", required = true)
            @PathVariable Long skuId);
}
